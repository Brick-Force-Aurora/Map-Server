package org.playuniverse.brickforce.maprepository.model.script;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RType;

public class EnableScript extends ScriptCommand<RCompound> {

	private int id;
	private boolean enable;
	
	public EnableScript() {
		super(ScriptType.ENABLE, RType.COMPOUND);
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isEnable() {
		return enable;
	}
	
	@Override
	protected void loadDataFromModel(RCompound model) {
		this.id = (int) model.get("id").getValue();
		this.enable = (boolean) model.get("enable").getValue();
	}
	
	@Override
	protected RCompound dataAsModel() {
		RCompound compound = new RCompound();
		compound.set("id", id);
		compound.set("enable", enable);
		return compound;
	}

}
