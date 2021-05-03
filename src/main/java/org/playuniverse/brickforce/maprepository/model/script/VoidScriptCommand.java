package org.playuniverse.brickforce.maprepository.model.script;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RModel;

public abstract class VoidScriptCommand extends ScriptCommand<RModel> {

	public VoidScriptCommand(ScriptType type) {
		super(type, null);
	}

	@Override
	protected void loadDataFromModel(RModel model) {

	}

	@Override
	protected RModel dataAsModel() {
		return null;
	}

}
