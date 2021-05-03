package org.playuniverse.brickforce.maprepository.model.script;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RFloat;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RType;

public class SleepScript extends ScriptCommand<RFloat> {

	private float time;
	
	public SleepScript() {
		super(ScriptType.SLEEP, RType.FLOAT);
	}
	
	public float getTime() {
		return time;
	}
	
	@Override
	protected void loadDataFromModel(RFloat model) {
		this.time = model.getValue();
	}
	
	@Override
	protected RFloat dataAsModel() {
		return new RFloat(time);
	}
	
}
