package org.playuniverse.brickforce.maprepository.model;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RLong;

public final class BrickMap {
	
	private final long id;
	
	private final Preview preview;
	private final Geometry geometry;
	
	public BrickMap(long id, Preview preview, Geometry geometry) {
		this.id = id;
		this.preview = preview;
		this.geometry = geometry;
	}
	
	public long getId() {
		return id;
	}
	
	public Geometry getGeometry() {
		return geometry;
	}
	
	public Preview getPreview() {
		return preview;
	}
	
	public RCompound asCompound() {
		RCompound compound = new RCompound();
		compound.set("id", new RLong(id));
		compound.set("preview", preview.asCompound());
		compound.set("geometry", geometry.asCompound());
		return compound;
	}

}
