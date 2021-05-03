package org.playuniverse.brickforce.maprepository.model.script;

import org.playuniverse.brickforce.maprepository.model.util.CSharpCompat;
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
	protected boolean loadDataFromArguments(String[] arguments) {
		if (arguments.length < 2) {
			return false;
		}
		this.id = CSharpCompat.parse(arguments[0], 0, Integer::parseInt);
		this.enable = CSharpCompat.parse(arguments[1], false, Boolean::parseBoolean);
		return true;
	}

	@Override
	protected String[] dataAsArguments() {
		return new String[] {
				Integer.toString(id),
				Boolean.toString(enable)
		};
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
