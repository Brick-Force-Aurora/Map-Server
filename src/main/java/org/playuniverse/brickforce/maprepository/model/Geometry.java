package org.playuniverse.brickforce.maprepository.model;

import java.util.ArrayList;

import org.playuniverse.brickforce.maprepository.model.util.ModelConversion;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RInt;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RList;

public final class Geometry {

	private final int map;
	private final int skybox;
	
	private final ArrayList<Brick> bricks;
	
	@SuppressWarnings("unchecked")
	public Geometry(RCompound compound) {
		this.map = (int) compound.get("map").getValue();
		this.skybox = (int) compound.get("skybox").getValue();
		this.bricks = ModelConversion.toBricks((RList<RCompound>) compound.get("bricks"));
	}
	
	public Geometry(int map, int skybox, ArrayList<Brick> bricks) {
		this.map = map;
		this.skybox = skybox;
		this.bricks = bricks;
	}
	
	public int getMap() {
		return map;
	}
	
	public int getSkybox() {
		return skybox;
	}
	
	public ArrayList<Brick> getBricks() {
		return bricks;
	}

	public RCompound asCompound() {
		RCompound compound = new RCompound();
		compound.set("map", new RInt(map));
		compound.set("skybox", new RInt(skybox));
		compound.set("bricks", ModelConversion.fromBricks(bricks));
		return compound;
	}

}
