package org.playuniverse.brickforce.maprepository.model;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RInt;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RLong;

public final class BrickMap {

	private final long id;

	private final Preview preview;
	private final Geometry geometry;
	
	private int modeMask;

	public BrickMap(RCompound compound) {
		this.id = (long) compound.get("id").getValue();
        this.modeMask = (int) compound.get("modeMask").getValue();
		this.preview = new Preview((RCompound) compound.get("preview"));
		this.geometry = new Geometry((RCompound) compound.get("geometry"));
	}

	public BrickMap(long id, Preview preview, Geometry geometry) {
		this.id = id;
		this.preview = preview;
		this.geometry = geometry;
	}

	public long getId() {
		return id;
	}
	
	public BrickMap setModeMask(int modeMask) {
	    this.modeMask = modeMask;
	    return this;
	}
	
	public int getModeMask() {
        return modeMask;
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
		compound.set("modeMask", new RInt(modeMask));
		compound.set("preview", preview.asCompound());
		compound.set("geometry", geometry.asCompound());
		return compound;
	}
	
	public BrickMap copyWithId(long id) {
		return new BrickMap(id, preview, geometry).setModeMask(modeMask);
	}

}
