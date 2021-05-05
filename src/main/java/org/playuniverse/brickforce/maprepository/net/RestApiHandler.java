package org.playuniverse.brickforce.maprepository.net;

import java.io.IOException;

import org.playuniverse.brickforce.maprepository.MapRepository;
import org.playuniverse.brickforce.maprepository.model.BrickMap;
import org.playuniverse.brickforce.maprepository.model.io.BrickMapHandler;
import org.playuniverse.brickforce.maprepository.storage.FileStorage;
import org.playuniverse.brickforce.maprepository.storage.StoreState;
import org.playuniverse.brickforce.maprepository.storage.utils.Ref;

import com.syntaxphoenix.syntaxapi.json.value.JsonLong;
import com.syntaxphoenix.syntaxapi.net.http.HttpSender;
import com.syntaxphoenix.syntaxapi.net.http.HttpWriter;
import com.syntaxphoenix.syntaxapi.net.http.JsonAnswer;
import com.syntaxphoenix.syntaxapi.net.http.NamedAnswer;
import com.syntaxphoenix.syntaxapi.net.http.ReceivedRequest;
import com.syntaxphoenix.syntaxapi.net.http.RequestHandler;
import com.syntaxphoenix.syntaxapi.net.http.RequestType;
import com.syntaxphoenix.syntaxapi.net.http.ResponseCode;
import com.syntaxphoenix.syntaxapi.net.http.StandardContentType;
import com.syntaxphoenix.syntaxapi.net.http.StandardNamedType;

public class RestApiHandler implements RequestHandler {

	private final FileStorage storage;

	public RestApiHandler(MapRepository repository) {
		this.storage = repository.getStorage();
	}

	@Override
	public boolean handleRequest(HttpSender sender, HttpWriter writer, ReceivedRequest request) throws Exception {
		long id = validateApiPath(request.getPath());
		if (id == -10) {
			new JsonAnswer(StandardContentType.JSON).code(ResponseCode.NOT_FOUND).respond("error", "Unknown api path").write(writer);
			return true;
		}
		if (id == -9) {
			new JsonAnswer(StandardContentType.JSON).code(ResponseCode.BAD_REQUEST).respond("error", "Invalid id").write(writer);
			return true;
		}
		if (id == -8) {
			new JsonAnswer(StandardContentType.JSON).code(ResponseCode.BAD_REQUEST).respond("error", "An id may only be positive, 0 or -1").write(writer);
			return true;
		}
		if (request.getType() == RequestType.POST) {
			handleUpload(sender, writer, request, id);
			return true;
		}
		handleGet(sender, writer, request, id);
		return true;
	}

	private long validateApiPath(String[] path) {
		if (path.length < 2 && !path[0].equalsIgnoreCase("map")) {
			return -10;
		}
		if (path[1].equalsIgnoreCase("get") || path[1].equalsIgnoreCase("has")) {
			if (path.length < 3) {
				return -9;
			}
			try {
				long value = Long.parseLong(path[2]);
				return value < -1 ? -8 : value;
			} catch (NumberFormatException exp) {
				return -9;
			}
		}
		if (path[1].equalsIgnoreCase("upload")) {
			if (path.length < 3) {
				return -1;
			}
			try {
				long value = Long.parseLong(path[2]);
				return value < -1 ? -8 : value;
			} catch (NumberFormatException exp) {
				return -9;
			}
		}
		return -10;
	}

	private void handleGet(HttpSender sender, HttpWriter writer, ReceivedRequest request, long id) throws Exception {
		if (id == -1) {
			new JsonAnswer(StandardContentType.JSON).code(ResponseCode.BAD_REQUEST).respond("error", "A map can only have an id higher or equal to zero")
				.write(writer);
			return;
		}
		if (!storage.hasMap(id)) {
			new NamedAnswer(StandardNamedType.PLAIN).code(ResponseCode.NOT_FOUND).write(writer);
			return;
		}
		if (request.getPath()[1].equalsIgnoreCase("has")) {
			new NamedAnswer(StandardNamedType.PLAIN).code(ResponseCode.OK).write(writer);
			return;
		}
		Ref<String> error = new Ref<>();
		BrickMap map = storage.getMap(id, error);
		if (error.has()) {
			new JsonAnswer(StandardContentType.JSON).code(ResponseCode.GONE).respond("error", error.get()).write(writer);
			return;
		}
		new MapAnswer().setResponse(map).code(ResponseCode.OK).write(writer);
	}

	private void handleUpload(HttpSender sender, HttpWriter writer, ReceivedRequest request, long id) throws Exception {
		id = (id == -1) ? storage.generateId() : id;
		byte[] bytes = (byte[]) request.getData().getValue();
		BrickMap map;
		try {
			map = BrickMapHandler.BRICK_MAP.fromBytes(bytes);
		} catch (IOException exp) {
			new JsonAnswer(StandardContentType.JSON).code(ResponseCode.NOT_ACCEPTABLE).respond("error", "Invalid content").write(writer);
			return;
		}
		if (map == null) {
			new JsonAnswer(StandardContentType.JSON).code(ResponseCode.NOT_ACCEPTABLE).respond("error", "Invalid content").write(writer);
			return;
		}
		StoreState state = storage.store(map.copyWithId(id));
		if (state == StoreState.FAILED) {
			new JsonAnswer(StandardContentType.JSON).code(ResponseCode.INTERNAL_SERVER_ERROR).respond("error", "Unable to save map to storage").write(writer);
			return;
		}
		new JsonAnswer(StandardContentType.JSON).code(ResponseCode.OK).respond("state", state.name()).respond("id", new JsonLong(id)).write(writer);
	}

}
