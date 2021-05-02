package org.playuniverse.brickforce.maprepository.model;

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

}
