package org.playuniverse.brickforce.maprepository.model.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RByte;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RShort;

public final class ModelConversion {

	private static byte[] EMPTY_ARRAY = new byte[0];

	private ModelConversion() {}

	public static RCompound asCompound(LocalDateTime date) {
		RCompound compound = new RCompound();
		compound.set("year", new RShort((short) date.getYear()));
		compound.set("month", new RByte((byte) date.getMonthValue()));
		compound.set("day", new RByte((byte) date.getDayOfMonth()));
		compound.set("hour", new RByte((byte) date.getHour()));
		compound.set("minute", new RByte((byte) date.getMinute()));
		compound.set("second", new RByte((byte) date.getSecond()));
		return compound;
	}

	public static LocalDateTime fromCompound(RCompound compound) {
		int year = (short) compound.get("year").getValue();
		int month = (byte) compound.get("month").getValue();
		int day = (byte) compound.get("day").getValue();
		int hour = (byte) compound.get("hour").getValue();
		int minute = (byte) compound.get("minute").getValue();
		int second = (byte) compound.get("second").getValue();
		return LocalDateTime.of(year, month, day, hour, minute, second);
	}

	public static byte[] toByteArray(BufferedImage image) {
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

	public static BufferedImage fromByteArray(byte[] bytes) {
		if(bytes.length == 0) {
			return null;
		}
		try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
			return ImageIO.read(stream);
		} catch (IOException exp) {
			return null;
		}
	}

}
