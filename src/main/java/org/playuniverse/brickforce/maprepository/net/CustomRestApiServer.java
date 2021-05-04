package org.playuniverse.brickforce.maprepository.net;

import static com.syntaxphoenix.syntaxapi.net.http.RequestValidator.DEFAULT_VALIDATOR;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import com.syntaxphoenix.syntaxapi.net.http.HttpSender;
import com.syntaxphoenix.syntaxapi.net.http.HttpWriter;
import com.syntaxphoenix.syntaxapi.net.http.NamedAnswer;
import com.syntaxphoenix.syntaxapi.net.http.ReceivedRequest;
import com.syntaxphoenix.syntaxapi.net.http.RequestExecution;
import com.syntaxphoenix.syntaxapi.net.http.RequestHandler;
import com.syntaxphoenix.syntaxapi.net.http.ResponseCode;
import com.syntaxphoenix.syntaxapi.net.http.StandardNamedType;

public class CustomRestApiServer extends CustomHttpServer {

	protected RequestHandler handler;

	public CustomRestApiServer() {
		super();
		setValidator(DEFAULT_VALIDATOR);
	}

	public CustomRestApiServer(ThreadFactory factory) {
		super(factory);
		setValidator(DEFAULT_VALIDATOR);
	}

	public CustomRestApiServer(ExecutorService service) {
		super(service);
		setValidator(DEFAULT_VALIDATOR);
	}

	public CustomRestApiServer(ThreadFactory factory, ExecutorService service) {
		super(factory, service);
		setValidator(DEFAULT_VALIDATOR);
	}

	public CustomRestApiServer(int port) {
		super(port);
		setValidator(DEFAULT_VALIDATOR);
	}

	public CustomRestApiServer(int port, ThreadFactory factory) {
		super(port, factory);
		setValidator(DEFAULT_VALIDATOR);
	}

	public CustomRestApiServer(int port, ExecutorService service) {
		super(port, service);
		setValidator(DEFAULT_VALIDATOR);
	}

	public CustomRestApiServer(int port, ThreadFactory factory, ExecutorService service) {
		super(port, factory, service);
		setValidator(DEFAULT_VALIDATOR);
	}

	public CustomRestApiServer(int port, InetAddress address) {
		super(port, address);
		setValidator(DEFAULT_VALIDATOR);
	}

	public CustomRestApiServer(int port, InetAddress address, ThreadFactory factory) {
		super(port, address, factory);
		setValidator(DEFAULT_VALIDATOR);
	}

	public CustomRestApiServer(int port, InetAddress address, ExecutorService service) {
		super(port, address, service);
		setValidator(DEFAULT_VALIDATOR);
	}

	public CustomRestApiServer(int port, InetAddress address, ThreadFactory factory, ExecutorService service) {
		super(port, address, factory, service);
		setValidator(DEFAULT_VALIDATOR);
	}

	/*
	 * Getter
	 */

	public RequestHandler getHandler() {
		return handler;
	}

	/*
	 * Setter
	 */

	public void setHandler(RequestHandler handler) {
		this.handler = handler;
	}

	/*
	 * Handle HttpRequest
	 */

	@Override
	protected RequestExecution handleHttpRequest(HttpSender sender, HttpWriter writer, ReceivedRequest request) throws Exception {

		if (handler == null) {
			new NamedAnswer(StandardNamedType.PLAIN).setResponse("No message handler was registered, Sorry!").code(ResponseCode.INTERNAL_SERVER_ERROR)
				.write(writer);
			return RequestExecution.error(new IllegalStateException("Handler can't be null!"));
		}

		try {
			return RequestExecution.of(handler.handleRequest(sender, writer, request));
		} catch (Exception e) {
			new NamedAnswer(StandardNamedType.PLAIN).setResponse("Something went wrong handling your request!").code(ResponseCode.INTERNAL_SERVER_ERROR)
				.write(writer);
			throw e;
		}
	}

}
