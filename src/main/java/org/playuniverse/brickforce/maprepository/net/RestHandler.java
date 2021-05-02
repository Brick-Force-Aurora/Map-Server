package org.playuniverse.brickforce.maprepository.net;

import com.syntaxphoenix.syntaxapi.net.http.HttpSender;
import com.syntaxphoenix.syntaxapi.net.http.HttpWriter;
import com.syntaxphoenix.syntaxapi.net.http.ReceivedRequest;
import com.syntaxphoenix.syntaxapi.net.http.RequestHandler;

public class RestHandler implements RequestHandler {

	private final RestServer server;

	public RestHandler(RestServer server) {
		this.server = server;
	}

	@Override
	public boolean handleRequest(HttpSender sender, HttpWriter writer, ReceivedRequest request) throws Exception {
		
		
		// TODO: Add RestApi
		
		return false;
	}

}
