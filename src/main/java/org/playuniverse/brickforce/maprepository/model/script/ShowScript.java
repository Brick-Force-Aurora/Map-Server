package org.playuniverse.brickforce.maprepository.model.script;

import org.playuniverse.brickforce.maprepository.model.util.CSharpCompat;
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
	protected boolean loadDataFromArguments(String[] arguments) {
		if (arguments.length < 1) {
			return false;
		}
		this.id = CSharpCompat.parse(arguments[0], 0, Integer::parseInt);
		this.visible = CSharpCompat.parse(arguments[1], false, Boolean::parseBoolean);
		return true;
	}

	@Override
	protected String[] dataAsArguments() {
		return new String[] {
				Integer.toString(id),
				Boolean.toString(visible)
		};
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
