package org.playuniverse.brickforce.maprepository.net;

import org.playuniverse.brickforce.maprepository.model.BrickMap;
import org.playuniverse.brickforce.maprepository.storage.FileStorage;
import org.playuniverse.brickforce.maprepository.storage.utils.Ref;
import org.playuniverse.console.Console;

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

	private final MapRepository repository;

	private final FileStorage storage;
	private final Console console;

	public RestApiHandler(MapRepository repository) {
		this.repository = repository;
		this.storage = repository.getStorage();
		this.console = repository.getConsole();
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
			new JsonAnswer(StandardContentType.JSON).code(ResponseCode.BAD_REQUEST).respond("error", "An id can only be positive, 0 or -1").write(writer);
			return true;
		}
		if (request.getType() == RequestType.POST) {
			handlePost(sender, writer, request, id);
			return true;
		}
		handleGet(sender, writer, request, id);
		return true;
	}

	private long validateApiPath(String[] path) {
		if (path.length < 2 && !path[0].equalsIgnoreCase("map")) {
			return -10;
		}
		if (path[1].equalsIgnoreCase("get")) {
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
		Ref<String> error = new Ref<>();
		BrickMap map = storage.getMap(id, error);
		if (error.has()) {
			new JsonAnswer(StandardContentType.JSON).code(ResponseCode.GONE).respond("error", error.get()).write(writer);
			return;
		}
	}

	private void handlePost(HttpSender sender, HttpWriter writer, ReceivedRequest request, long id) throws Exception {

	}

}
