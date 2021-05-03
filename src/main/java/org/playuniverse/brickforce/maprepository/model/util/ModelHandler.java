package org.playuniverse.brickforce.maprepository.model.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.playuniverse.brickforce.maprepository.model.script.ScriptCommand;
import org.playuniverse.brickforce.maprepository.model.script.ScriptType;

public final class ModelHandler {

	public static final byte[] EMPTY_ARRAY = new byte[0];

	private ModelHandler() {}

	public static byte[] fromImage(BufferedImage image) {
		if (image == null) {
			return EMPTY_ARRAY;
		}
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			ImageIO.write(image, "jpeg", stream);
			return stream.toByteArray();
		} catch (IOException exp) {
			return EMPTY_ARRAY;
		}
	}

	public static BufferedImage toImage(byte[] bytes) {
		if (bytes.length == 0) {
			return null;
		}
		try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
			return ImageIO.read(stream);
		} catch (IOException exp) {
			return null;
		}
	}

	public static ArrayList<ScriptCommand<?>> toScriptCommands(String compactCommands) {
		ArrayList<ScriptCommand<?>> commands = new ArrayList<>();
		if (compactCommands == null || compactCommands.isBlank()) {
			return commands;
		}
		String[] descriptions = CSharpCompat.splitRemoveEmpty(compactCommands, ScriptCommand.CMD_DELIMITERS);
		for (String description : descriptions) {
			String[] arguments0 = Arrays.stream(CSharpCompat.splitRemoveEmpty(description, ScriptCommand.ARG_DELIMITERS)).map(String::trim)
				.toArray(String[]::new);
			if (arguments0.length == 0) {
				continue;
			}
			ScriptType type = ScriptType.fromString(arguments0[0].toLowerCase());
			String[] arguments = CSharpCompat.EMPTY_ARRAY;
			if (arguments0.length != 1) {
				arguments = new String[arguments0.length - 1];
				System.arraycopy(arguments0, 1, arguments, 0, arguments.length);
			}
			ScriptCommand<?> command = ScriptCommand.fromArguments(type, arguments0);
			if (command == null) {
				continue;
			}
			commands.add(command);
		}
		return commands;
	}

	public static String fromScriptCommands(ArrayList<ScriptCommand<?>> commands) {
		if (commands.isEmpty()) {
			return "";
		}
		int size = commands.size();
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < size; index++) {
			builder.append(commands.get(index));
			if (index + 1 != size) {
				builder.append(ScriptCommand.ARG_DELIMITERS[0]);
			}
		}
		return builder.toString();
	}

}
