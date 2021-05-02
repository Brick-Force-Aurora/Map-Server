package org.playuniverse.brickforce.maprepository.shaded.redis.model.io;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RModel;

public final class RNamedModel {
	
	private final String name;
	private final RModel model;
	
	public RNamedModel(String name, RModel model) {
		this.name = name;
		this.model = model;
	}
	
	public String getName() {
		return name;
	}
	
	public RModel getModel() {
		return model;
	}

}
