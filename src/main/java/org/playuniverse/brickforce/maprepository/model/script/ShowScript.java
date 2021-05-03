package org.playuniverse.brickforce.maprepository.model.script;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RType;

public class ShowScript extends ScriptCommand<RCompound> {

	private int id;
	private boolean visible;
	
	public ShowScript() {
		super(ScriptType.SHOW, RType.COMPOUND);
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	@Override
	protected void loadDataFromModel(RCompound model) {
		this.id = (int) model.get("id").getValue();
		this.visible = (boolean) model.get("visible").getValue();
	}
	
	@Override
	protected RCompound dataAsModel() {
		RCompound compound = new RCompound();
		compound.set("id", id);
		compound.set("visible", visible);
		return compound;
	}
	
}
