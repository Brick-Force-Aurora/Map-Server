package org.playuniverse.brickforce.maprepository.model.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.playuniverse.brickforce.maprepository.model.Brick;
import org.playuniverse.brickforce.maprepository.model.script.ScriptCommand;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RByte;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RList;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RShort;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RType;

public final class ModelConversion {

	private ModelConversion() {}

	public static RCompound fromDate(LocalDateTime date) {
		RCompound compound = new RCompound();
		compound.set("year", new RShort((short) date.getYear()));
		compound.set("month", new RByte((byte) date.getMonthValue()));
		compound.set("day", new RByte((byte) date.getDayOfMonth()));
		compound.set("hour", new RByte((byte) date.getHour()));
		compound.set("minute", new RByte((byte) date.getMinute()));
		compound.set("second", new RByte((byte) date.getSecond()));
		return compound;
	}

	public static LocalDateTime toDate(RCompound compound) {
		int year = (short) compound.get("year").getValue();
		int month = (byte) compound.get("month").getValue();
		int day = (byte) compound.get("day").getValue();
		int hour = (byte) compound.get("hour").getValue();
		int minute = (byte) compound.get("minute").getValue();
		int second = (byte) compound.get("second").getValue();
		return LocalDateTime.of(year, month, day, hour, minute, second);
	}
	
	public static RList<RCompound> fromBricks(ArrayList<Brick> list) {
		RList<RCompound> output = new RList<>(RType.COMPOUND);
		for(Brick brick : list) {
			output.add(brick.asCompound());
		}
		return output;
	}
	
	public static ArrayList<Brick> toBricks(RList<RCompound> input) {
		ArrayList<Brick> list = new ArrayList<>();
		for(RCompound compound : input) {
			list.add(new Brick(compound));
		}
		return list;
	}
	
	public static RList<RCompound> fromScriptCommands(ArrayList<ScriptCommand> list) {
		RList<RCompound> output = new RList<>(RType.COMPOUND);
		for(ScriptCommand command : list) {
			output.add(command.asCompound());
		}
		return output;
	}
	
	public static ArrayList<ScriptCommand> toScriptCommands(RList<RCompound> input) {
		ArrayList<ScriptCommand> list = new ArrayList<>();
		for(RCompound compound : input) {
			list.add(new Brick(compound));
		}
		return list;
	}

}
