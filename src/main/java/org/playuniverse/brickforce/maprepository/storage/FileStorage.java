package org.playuniverse.brickforce.maprepository.storage;

import org.playuniverse.brickforce.maprepository.model.Geometry;
import org.playuniverse.brickforce.maprepository.model.BrickMap;
import org.playuniverse.brickforce.maprepository.model.Preview;
import org.playuniverse.brickforce.maprepository.storage.utils.Ref;

public abstract class FileStorage {

	private static final Ref<String> IGNORE = new Ref<>();

	public abstract StoreState store(BrickMap map);

	public abstract boolean hasMap(long id);

	public final BrickMap getMap(long id) {
		return getMap(id, IGNORE);
	}

	public abstract BrickMap getMap(long id, Ref<String> ref);

	public abstract Preview getPreview(long id);

	public abstract Geometry getGeometry(long id);

	public void save() {}

}
