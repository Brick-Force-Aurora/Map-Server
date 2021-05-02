package org.playuniverse.brickforce.maprepository.storage;

import org.playuniverse.brickforce.maprepository.model.Geometry;
import org.playuniverse.brickforce.maprepository.model.BrickMap;
import org.playuniverse.brickforce.maprepository.model.Preview;

public abstract class FileStorage {
	
	public abstract void store(BrickMap map);
	
	public abstract void update(long id, Geometry geometry);
	
	public abstract boolean hasMap(long id);
	
	public abstract BrickMap getMap(long id);
	
	public abstract Preview getPreview(long id);
	
	public abstract Geometry getGeometry(long id);
	
	public void save() {}

}
