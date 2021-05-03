package org.playuniverse.brickforce.maprepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.net.InetAddress;
import java.util.Optional;

import org.playuniverse.brickforce.maprepository.net.RestServer;
import org.playuniverse.brickforce.maprepository.storage.FileStorage;
import org.playuniverse.brickforce.maprepository.storage.file.FileFileStorage;
import org.playuniverse.console.Console;
import org.playuniverse.console.flags.ConsoleFlags;

import com.syntaxphoenix.syntaxapi.command.ArgumentMap;
import com.syntaxphoenix.syntaxapi.command.ArgumentSuperType;
import com.syntaxphoenix.syntaxapi.command.ArgumentType;
import com.syntaxphoenix.syntaxapi.command.helper.JVMArgumentHelper;
import com.syntaxphoenix.syntaxapi.reflection.ClassCache;
import com.syntaxphoenix.syntaxapi.utils.java.Strings;
import com.syntaxphoenix.syntaxapi.utils.java.tools.Container;

public final class Main {

	public static final String STORAGE_PATH = "org.playuniverse.brickforce.maprepository.storage.%s.%s";

	private Main() {}

	private static Container<RestServer> container = Container.of();

	public static void main(String[] args) {
		ArgumentMap arguments = JVMArgumentHelper.DEFAULT.serialize(args);

		Console console = new Console(System.in, System.out, ConsoleFlags.executeOnShutdown(() -> container.ifPresent(server -> server.stop())),
			ConsoleFlags.exitSystemOnShutdown());
		console.setColored(false);
		console.log("Starting...");

		int port = arguments.get("port", ArgumentSuperType.NUMBER).map(base -> base.asNumeric().asNumber()).orElse(80).intValue();
		InetAddress address = arguments.get("host", ArgumentType.STRING).map(base -> base.asString().getValue()).map(Main::parseAddress).orElse(null);
		FileStorage storage = arguments.get("storage", ArgumentType.STRING).map(base -> base.asString().getValue())
			.map(value -> searchStorage(arguments, console, value.toLowerCase())).orElseGet(() -> new FileFileStorage(console));

		container.replace(new RestServer(arguments, console, storage, port, address));
		// START : Setup Commands
		console.getCommandManager().register(null, STORAGE_PATH, args);
		// END : Setup Commands
		container.get().start();
	}

	/*
	 * Argument parsing
	 */

	private static InetAddress parseAddress(String value) {
		try {
			return InetAddress.getByName(value);
		} catch (Exception exp) {
			return null;
		}
	}

	private static FileStorage searchStorage(ArgumentMap arguments, Console console, String value) {
		String path = String.format(STORAGE_PATH, value, Strings.firstLetterToUpperCase(value));
		Optional<Class<?>> option = ClassCache.getOptionalClass(path);
		if (option.isEmpty()) {
			return null;
		}
		Class<?> clazz = option.get();
		Constructor<?>[] constructors = clazz.getConstructors();
		Constructor<?> instance = null;
		int[] positions = null;
		Object[] injectable = new Object[] {
				container,
				arguments,
				console
		};
		int[] tmp = new int[0];
		for (Constructor<?> constructor : constructors) {
			if (instance != null && (tmp = available(instance.getParameterCount(), constructor, injectable)) != null) {
				continue;
			}
			instance = constructor;
			positions = tmp;
		}
		if (instance == null) {
			return null;
		}
		return (FileStorage) build(instance, resort(injectable, positions));
	}

	private static Object build(Constructor<?> constructor, Object[] objects) {
		boolean flag = constructor.canAccess(Main.class);
		if (!flag) {
			constructor.setAccessible(true);
		}
		Object output = null;
		try {
			output = constructor.newInstance(objects);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		}
		if (!flag) {
			constructor.setAccessible(false);
		}
		return output;
	}

	private static Object[] resort(Object[] values, int[] positions) {
		Object[] output = new Object[positions.length];
		for (int index = 0; index < positions.length; index++) {
			output[index] = values[positions[index]];
		}
		return output;
	}

	private static int[] available(int current, Constructor<?> constructor, Object[] injectable) {
		Parameter[] parameters = constructor.getParameters();
		if (parameters.length > injectable.length) {
			return null;
		}
		int[] params = new int[parameters.length];
		for (int index = 0; index < params.length; index++) {
			Class<?> type = parameters[index].getType();
			boolean match = false;
			for (int index0 = 0; index0 < injectable.length; index0++) {
				if (!type.equals(injectable[index0].getClass())) {
					continue;
				}
				params[index] = index0;
				match = true;
				break;
			}
			if (!match) {
				return null;
			}
		}
		return params;
	}

}
