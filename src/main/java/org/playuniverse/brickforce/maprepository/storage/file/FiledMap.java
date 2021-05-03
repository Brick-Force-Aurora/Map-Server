package org.playuniverse.brickforce.maprepository.storage.file;

import java.io.File;

import org.playuniverse.brickforce.maprepository.model.BrickMap;

public final class FiledMap {

	private final File file;
	private BrickMap map;

	public FiledMap(File file, BrickMap map) {
		this.file = file;
		this.map = map;
	}

	public File getFile() {
		return file;
	}

	public BrickMap getMap() {
		return map;
	}

	public FiledMap setMap(BrickMap map) {
		this.map = map;
		return this;
	}

}
