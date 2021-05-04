package org.playuniverse.brickforce.maprepository.net;

import java.net.InetAddress;

import org.playuniverse.brickforce.maprepository.storage.FileStorage;
import org.playuniverse.console.Console;

import com.syntaxphoenix.syntaxapi.command.ArgumentMap;
import com.syntaxphoenix.syntaxapi.logging.LogTypeId;
import com.syntaxphoenix.syntaxapi.net.http.RequestType;

public class MapRepository {

	private final CustomRestApiServer server;
	private final FileStorage storage;
	private final Console console;

	private final ArgumentMap arguments;

	public MapRepository(ArgumentMap arguments, Console console, FileStorage storage, int port, InetAddress address) {
		this.arguments = arguments;
		this.console = console;
		this.storage = storage;
		this.server = address == null ? new CustomRestApiServer(port) : new CustomRestApiServer(port, address);
		server.addTypes(RequestType.POST, RequestType.GET);
		server.setHandler(new RestApiHandler(this));
	}

	public ArgumentMap getArguments() {
		return arguments;
	}

	public CustomRestApiServer getServer() {
		return server;
	}

	public FileStorage getStorage() {
		return storage;
	}

	public Console getConsole() {
		return console;
	}

	public void start() {
		try {
			server.start();
		} catch (Exception exp) {
			console.log(LogTypeId.ERROR, exp);
			console.shutdown();
		}
	}

	public void stop() {
		try {
			storage.save();
			server.stop();
		} catch (Exception exp) {
			console.log(LogTypeId.WARNING, exp);
		}
	}

}
