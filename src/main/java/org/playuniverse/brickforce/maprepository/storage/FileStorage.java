package org.playuniverse.brickforce.maprepository.storage;

import org.playuniverse.brickforce.maprepository.model.Geometry;
import org.playuniverse.brickforce.maprepository.model.BrickMap;
import org.playuniverse.brickforce.maprepository.model.Preview;
import org.playuniverse.brickforce.maprepository.storage.utils.Ref;

import com.syntaxphoenix.syntaxapi.random.NumberGeneratorType;
import com.syntaxphoenix.syntaxapi.random.RandomNumberGenerator;

public abstract class FileStorage {

	private static final Ref<String> IGNORE = new Ref<>();
	private static final RandomNumberGenerator RANDOM = NumberGeneratorType.MURMUR.create(System.currentTimeMillis());

	public abstract StoreState store(BrickMap map);

	public abstract boolean hasMap(long id);

	public final BrickMap getMap(long id) {
		return getMap(id, IGNORE);
	}

	public abstract BrickMap getMap(long id, Ref<String> ref);

	public abstract Preview getPreview(long id);

	public abstract Geometry getGeometry(long id);

	public long generateId() {
		long id = RANDOM.nextLong();
		while (hasMap(id)) {
			id = RANDOM.nextLong();
		}
		return id;
	}

	public void save() {}

}
