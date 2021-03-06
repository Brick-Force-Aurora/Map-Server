package org.playuniverse.brickforce.maprepository.model.script;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RString;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RType;

public class GiveWeaponScript extends ScriptCommand<RString> {

	private String code;
	
	public GiveWeaponScript() {
		super(ScriptType.GIVE_WEAPON, RType.STRING);
	}
	
	public String getCode() {
		return code;
	}
	
	@Override
	protected boolean loadDataFromArguments(String[] arguments) {
		this.code = arguments.length < 1 ? "" : arguments[0];
		return true;
	}
	
	@Override
	protected String[] dataAsArguments() {
		return new String[] {
				code
		};
	}

	@Override
	protected void loadDataFromModel(RString model) {
		this.code = model.getValue();
	}

	@Override
	protected RString dataAsModel() {
		return new RString(code);
	}

}
