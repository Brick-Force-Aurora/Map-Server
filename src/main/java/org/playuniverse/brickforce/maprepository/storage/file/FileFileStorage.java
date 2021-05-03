package org.playuniverse.brickforce.maprepository.storage.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.playuniverse.brickforce.maprepository.model.BrickMap;
import org.playuniverse.brickforce.maprepository.model.Geometry;
import org.playuniverse.brickforce.maprepository.model.Preview;
import org.playuniverse.brickforce.maprepository.model.util.ModelHandler;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RModel;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RType;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.io.RIOModel;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.io.RNamedModel;
import org.playuniverse.brickforce.maprepository.storage.FileStorage;
import org.playuniverse.brickforce.maprepository.storage.StoreState;
import org.playuniverse.brickforce.maprepository.storage.utils.CacheMap;
import org.playuniverse.console.Console;

import com.syntaxphoenix.syntaxapi.command.ArgumentMap;
import com.syntaxphoenix.syntaxapi.command.ArgumentType;
import com.syntaxphoenix.syntaxapi.logging.LogTypeId;
import com.syntaxphoenix.syntaxapi.utils.java.Files;
import com.syntaxphoenix.syntaxapi.utils.java.Streams;

public class FileFileStorage extends FileStorage {

	public static final String FILE_FORMAT = "%d.crm";

	private final File directory;
	private final CacheMap<Long, FiledMap> cache = new CacheMap<>(300, 1000);

	private final Console console;

	public FileFileStorage(Console console) {
		this(console, "storage/file");
	}

	public FileFileStorage(Console console, ArgumentMap map) {
		this(console, map.get("storage-path", ArgumentType.STRING).map(base -> base.asString().getValue()).orElse("storage/file"));
	}

	public FileFileStorage(Console console, String path) {
		this.directory = new File(path);
		this.console = console;
	}

	public File getDirectory() {
		return directory;
	}

	public File getFile(long id) {
		return new File(String.format(FILE_FORMAT, id));
	}

	@Override
	public StoreState store(BrickMap map) {
		if (map == null) {
			return StoreState.FAILED;
		}
		long id = map.getId();
		FiledMap filed = cache.get(id);
		File file = filed == null ? getFile(id) : filed.setMap(map).getFile();
		byte[] data = RIOModel.MODEL.write(new RNamedModel("root", map.asCompound()));
		boolean exists = file.exists();
		if (!exists) {
			Files.createFile(file);
		}
		try (OutputStream stream = new GZIPOutputStream(new FileOutputStream(file))) {
			stream.write(data);
		} catch (IOException exp) {
			console.log(LogTypeId.ERROR, "Failed to write map '" + id + "' to file!");
			console.log(exp);
			return StoreState.FAILED;
		}
		return exists ? StoreState.UPDATED : StoreState.UPDATED;
	}

	@Override
	public boolean hasMap(long id) {
		return cache.containsKey(id) || getFile(id).exists();
	}

	@Override
	public BrickMap getMap(long id) {
		FiledMap map = cache.get(id);
		if (map != null) {
			return map.getMap();
		}
		File file = getFile(id);
		if (!file.exists()) {
			return null; // Map doesn't exist
		}
		byte[] data = ModelHandler.EMPTY_ARRAY;
		try (InputStream stream = new GZIPInputStream(new FileInputStream(file))) {
			data = Streams.toByteArray(stream);
		} catch (IOException exp) {
			console.log(LogTypeId.ERROR, "Failed to load map '" + id + "' from file!");
			console.log(exp);
			return null; // Unable to load map
		}
		RNamedModel namedModel;
		try {
			namedModel = RIOModel.MODEL.read(data);
		} catch (IndexOutOfBoundsException exp) {
			console.log(LogTypeId.ERROR, "Failed to load map '" + id + "' from RedisIO model!");
			console.log(exp);
			namedModel = null;
		}
		if (namedModel == null) {
			console.log(LogTypeId.WARNING, "Deleting map '" + id + "' because its corrupted!");
			file.delete();
			return null;
		}
		RModel model = namedModel.getModel();
		if (model == null || model.getType() != RType.COMPOUND) {
			console.log(LogTypeId.WARNING, "Deleting map '" + id + "' because its invalid!");
			file.delete();
			return null;
		}
		BrickMap brickMap = new BrickMap((RCompound) model);
		cache.set(brickMap.getId(), new FiledMap(file, brickMap));
		return brickMap;
	}

	@Override
	public Preview getPreview(long id) {
		BrickMap map = getMap(id);
		return map == null ? null : map.getPreview();
	}

	@Override
	public Geometry getGeometry(long id) {
		BrickMap map = getMap(id);
		return map == null ? null : map.getGeometry();
	}

}
