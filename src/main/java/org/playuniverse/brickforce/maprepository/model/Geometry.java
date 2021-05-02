package org.playuniverse.brickforce.maprepository.model;

import java.util.ArrayList;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;

public final class Geometry {

	private final int map;
	private final int skybox;
	
	private final ArrayList<Brick> bricks;
	
	public Geometry(int map, int skybox, ArrayList<Brick> bricks) {
		this.map = map;
		this.skybox = skybox;
		this.bricks = bricks;
	}

	public RCompound asCompound() {
		return null;
	}

}
