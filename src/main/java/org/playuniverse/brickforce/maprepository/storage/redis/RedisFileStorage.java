package org.playuniverse.brickforce.maprepository.storage.redis;

import org.playuniverse.brickforce.maprepository.model.BrickMap;
import org.playuniverse.brickforce.maprepository.model.Geometry;
import org.playuniverse.brickforce.maprepository.model.Preview;
import org.playuniverse.brickforce.maprepository.storage.FileStorage;

public class RedisFileStorage extends FileStorage {

	@Override
	public void store(BrickMap map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(long id, Geometry geometry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasMap(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BrickMap getMap(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Preview getPreview(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Geometry getGeometry(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void save() {
		
	}

}
