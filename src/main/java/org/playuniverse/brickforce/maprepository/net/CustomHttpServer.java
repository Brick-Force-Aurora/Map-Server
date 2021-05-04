package org.playuniverse.brickforce.maprepository.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import com.syntaxphoenix.syntaxapi.net.AsyncSocketServer;
import com.syntaxphoenix.syntaxapi.net.http.CustomRequestData;
import com.syntaxphoenix.syntaxapi.net.http.HttpSender;
import com.syntaxphoenix.syntaxapi.net.http.HttpWriter;
import com.syntaxphoenix.syntaxapi.net.http.NamedAnswer;
import com.syntaxphoenix.syntaxapi.net.http.ReceivedRequest;
import com.syntaxphoenix.syntaxapi.net.http.RequestContent;
import com.syntaxphoenix.syntaxapi.net.http.RequestExecution;
import com.syntaxphoenix.syntaxapi.net.http.RequestGate;
import com.syntaxphoenix.syntaxapi.net.http.RequestState;
import com.syntaxphoenix.syntaxapi.net.http.RequestType;
import com.syntaxphoenix.syntaxapi.net.http.RequestValidator;
import com.syntaxphoenix.syntaxapi.net.http.ResponseCode;
import com.syntaxphoenix.syntaxapi.net.http.StandardNamedType;

public abstract class CustomHttpServer extends AsyncSocketServer {

	protected final HashSet<RequestType> supported = new HashSet<>();

	protected RequestGate gate;
	protected RequestValidator validator;

	public CustomHttpServer() {
		super();
	}

	public CustomHttpServer(ThreadFactory factory) {
		super(factory);
	}

	public CustomHttpServer(ExecutorService service) {
		super(service);
	}

	public CustomHttpServer(ThreadFactory factory, ExecutorService service) {
		super(factory, service);
	}

	public CustomHttpServer(int port) {
		super(port);
	}

	public CustomHttpServer(int port, ThreadFactory factory) {
		super(port, factory);
	}

	public CustomHttpServer(int port, ExecutorService service) {
		super(port, service);
	}

	public CustomHttpServer(int port, ThreadFactory factory, ExecutorService service) {
		super(port, factory, service);
	}

	public CustomHttpServer(int port, InetAddress address) {
		super(port, address);
	}

	public CustomHttpServer(int port, InetAddress address, ThreadFactory factory) {
		super(port, address, factory);
	}

	public CustomHttpServer(int port, InetAddress address, ExecutorService service) {
		super(port, address, service);
	}

	public CustomHttpServer(int port, InetAddress address, ThreadFactory factory, ExecutorService service) {
		super(port, address, factory, service);
	}

	/*
	 * Getter
	 */

	public RequestGate getGate() {
		return gate;
	}

	public RequestValidator getValidator() {
		return validator;
	}

	/*
	 * Setter
	 */

	public CustomHttpServer setGate(RequestGate gate) {
		this.gate = gate;
		return this;
	}

	public CustomHttpServer setValidator(RequestValidator validator) {
		this.validator = validator;
		return this;
	}

	/*
	 * RequestType management
	 */

	public CustomHttpServer addType(RequestType type) {
		supported.add(type);
		return this;
	}

	public CustomHttpServer addTypes(RequestType... types) {
		for (int index = 0; index < types.length; index++) {
			supported.add(types[index]);
		}
		return this;
	}

	public CustomHttpServer removeType(RequestType type) {
		supported.remove(type);
		return this;
	}

	public CustomHttpServer removeTypes(RequestType... types) {
		for (int index = 0; index < types.length; index++) {
			supported.remove(types[index]);
		}
		return this;
	}

	public CustomHttpServer clearTypes() {
		supported.clear();
		return this;
	}

	public RequestType[] getTypes() {
		return supported.toArray(new RequestType[0]);
	}

	/*
	 * Handle clients
	 */

	@Override
	protected void handleClientAsync(Socket socket) throws Throwable {

		HttpWriter writer = new HttpWriter(new PrintStream(socket.getOutputStream()));

		InputStream stream = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

		String line = reader.readLine();

		if (line == null) {
			new NamedAnswer(StandardNamedType.PLAIN).code(ResponseCode.NO_CONTENT).write(writer);
			socket.close();
			return;
		}

		String[] info = line.split(" ");
		String[] path = info[1].startsWith("/") ? info[1].substring(1).split("/") : info[1].split("/");
		String[] parameters = null;

		if (path[path.length - 1].contains("?")) {
			parameters = path[path.length - 1].split("\\?");
			path[path.length - 1] = parameters[0];
			parameters = parameters[1].contains("&") ? parameters[1].split("&")
				: new String[] {
						parameters[1]
				};
		}

		ReceivedRequest request = new ReceivedRequest(RequestType.fromString(info[0]), path);

		if (parameters != null) {
			request.parseParameters(parameters);
		}

		while ((line = reader.readLine()) != null) {
			if (line.isEmpty()) {
				break;
			}
			request.parseHeader(line);
		}

		if (!supported.isEmpty() && !supported.contains(request.getType())) {
			new NamedAnswer(StandardNamedType.PLAIN).setResponse("Unsupported request method!").code(ResponseCode.BAD_REQUEST).write(writer);
			reader.close();
			writer.close();
			socket.close();
			return;
		}

		if (gate != null) {
			RequestState state = gate.acceptRequest(writer, request);
			if (state.accepted()) {
				if (request.hasHeader("expect")) {
					if (((String) request.getHeader("expect")).contains("100-continue")) {
						new NamedAnswer(StandardNamedType.PLAIN).code(ResponseCode.CONTINUE).write(writer);
					}
				}
			} else {
				if (!state.message()) {
					new NamedAnswer(StandardNamedType.PLAIN).setResponse("Method or contenttype is not supported").code(ResponseCode.BAD_REQUEST).write(writer);
				}
				reader.close();
				writer.close();
				socket.close();
				return;
			}
		} else {
			if (request.hasHeader("expect")) {
				if (((String) request.getHeader("expect")).contains("100-continue")) {
					new NamedAnswer(StandardNamedType.PLAIN).setResponse("No content length given!").code(ResponseCode.LENGTH_REQUIRED).write(writer);
				}
			}
		}

		/*
		 * 
		 */

		RequestContent content = RequestContent.UNNEEDED;
		if (validator != null) {
			content = validator.parseContent(writer, request);
		}

		if (!content.ignore()) {

			if (content.message()) {
				new NamedAnswer(StandardNamedType.PLAIN).setResponse("No content length given!").code(ResponseCode.LENGTH_REQUIRED).write(writer);
				reader.close();
				writer.close();
				socket.close();
				return;
			}

			int length = ((Number) request.getHeader("Content-Length")).intValue();
			
			byte[] data = new byte[length];
			for (int index = 0; index < length; index++) {
				data[index] = (byte) stream.read();
			}
			request.setData(new CustomRequestData<>(byte[].class, data));
		}

		RequestExecution execution = null;
		try {
			execution = handleHttpRequest(new HttpSender(socket, reader), writer, request);
		} catch (Exception e) {
			execution = RequestExecution.error(e);
		}

		if ((execution == null ? RequestExecution.CLOSE : execution).close()) {
			reader.close();
			writer.close();
			socket.close();
			if (execution.hasThrowable()) {
				throw execution.getThrowable();
			}
		}

	}

	protected RequestExecution handleHttpRequest(HttpSender sender, HttpWriter writer, ReceivedRequest request) throws Exception {
		return RequestExecution.CLOSE;
	}

}
