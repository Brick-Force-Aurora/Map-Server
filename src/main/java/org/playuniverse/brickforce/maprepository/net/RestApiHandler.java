package org.playuniverse.brickforce.maprepository.net;

import com.syntaxphoenix.syntaxapi.net.http.HttpSender;
import com.syntaxphoenix.syntaxapi.net.http.HttpWriter;
import com.syntaxphoenix.syntaxapi.net.http.ReceivedRequest;
import com.syntaxphoenix.syntaxapi.net.http.RequestHandler;

public class RestApiHandler implements RequestHandler {

	private final MapRepository repository;

	public RestApiHandler(MapRepository repository) {
		this.repository = repository;
	}

	@Override
	public boolean handleRequest(HttpSender sender, HttpWriter writer, ReceivedRequest request) throws Exception {

		return true;
	}
	
	private void handleGet(HttpSender sender, HttpWriter writer, ReceivedRequest request) throws Exception {
		
	}
	
	private void handlePost(HttpSender sender, HttpWriter writer, ReceivedRequest request) throws Exception {
		
	}

}
