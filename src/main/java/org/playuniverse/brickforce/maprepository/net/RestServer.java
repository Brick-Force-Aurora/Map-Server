package org.playuniverse.brickforce.maprepository.net;

import java.net.InetAddress;

import org.playuniverse.brickforce.maprepository.storage.FileStorage;
import org.playuniverse.console.Console;

import com.syntaxphoenix.syntaxapi.command.ArgumentMap;
import com.syntaxphoenix.syntaxapi.logging.LogTypeId;
import com.syntaxphoenix.syntaxapi.net.http.RestApiServer;

public class RestServer {

	private final RestApiServer server;
	private final FileStorage storage;
	private final Console console;

	private final ArgumentMap arguments;

	public RestServer(ArgumentMap arguments, Console console, FileStorage storage, int port, InetAddress address) {
		this.arguments = arguments;
		this.console = console;
		this.storage = storage;
		this.server = address == null ? new RestApiServer(port) : new RestApiServer(port, address);
	}

	public ArgumentMap getArguments() {
		return arguments;
	}

	public RestApiServer getServer() {
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
