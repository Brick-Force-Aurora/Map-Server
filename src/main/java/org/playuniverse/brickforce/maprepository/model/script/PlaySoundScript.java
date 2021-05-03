package org.playuniverse.brickforce.maprepository.model.script;

import org.playuniverse.brickforce.maprepository.model.util.CSharpCompat;
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
	protected boolean loadDataFromArguments(String[] arguments) {
		if(arguments.length < 1) {
			return false;
		}
		this.index = CSharpCompat.parse(arguments[0], 0, Integer::parseInt);
		return true;
	}
	
	@Override
	protected String[] dataAsArguments() {
		return new String[] {
				Integer.toString(index)
		};
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
