package org.playuniverse.brickforce.maprepository.model.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.playuniverse.brickforce.maprepository.model.script.ScriptCommand;

public final class ModelHandler {

	private static byte[] EMPTY_ARRAY = new byte[0];
	
	private ModelHandler() {}

	public static byte[] fromImage(BufferedImage image) {
		if(image == null) {
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
		if(bytes.length == 0) {
			return null;
		}
		try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
			return ImageIO.read(stream);
		} catch (IOException exp) {
			return null;
		}
	}
	
	public static ArrayList<ScriptCommand> toScriptCommands(String compactCommands) {
		
	}
	
	public static String fromScriptCommands(ArrayList<ScriptCommand> commands) {
		
	}

}
