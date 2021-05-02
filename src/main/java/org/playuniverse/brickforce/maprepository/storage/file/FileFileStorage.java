package org.playuniverse.brickforce.maprepository.storage.file;

import java.io.File;

import org.playuniverse.brickforce.maprepository.model.BrickMap;
import org.playuniverse.brickforce.maprepository.model.Geometry;
import org.playuniverse.brickforce.maprepository.model.Preview;
import org.playuniverse.brickforce.maprepository.storage.FileStorage;

import com.syntaxphoenix.syntaxapi.command.ArgumentMap;
import com.syntaxphoenix.syntaxapi.command.ArgumentType;

public class FileFileStorage extends FileStorage {

	private final File directory;

	public FileFileStorage() {
		this("storage/file");
	}

	public FileFileStorage(ArgumentMap map) {
		this(map.get("storage-path", ArgumentType.STRING).map(base -> base.asString().getValue()).orElse("storage/file"));
	}

	public FileFileStorage(String path) {
		this.directory = new File(path);
	}

	public File getDirectory() {
		return directory;
	}
	
	// TODO: Implement File Storage

	@Override
	public void store(BrickMap map) {

	}

	@Override
	public void update(long id, Geometry geometry) {

	}

	@Override
	public boolean hasMap(long id) {
		return false;
	}

	@Override
	public BrickMap getMap(long id) {
		return null;
	}

	@Override
	public Preview getPreview(long id) {
		return null;
	}

	@Override
	public Geometry getGeometry(long id) {
		return null;
	}

}
