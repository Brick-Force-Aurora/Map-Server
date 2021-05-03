package org.playuniverse.brickforce.maprepository.model.script;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RInt;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RType;

public class PlaySoundScript extends ScriptCommand<RInt> {

	private int index;
	
	public PlaySoundScript() {
		super(ScriptType.PLAY_SOUND, RType.INT);
	}

	public int getIndex() {
		return index;
	}

	@Override
	protected void loadDataFromModel(RInt model) {
		index = model.getValue().intValue();
	}

	@Override
	protected RInt dataAsModel() {
		return new RInt(index);
	}

}
