package org.playuniverse.brickforce.maprepository.model.script;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RByte;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RModel;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RType;

public abstract class ScriptCommand<E extends RModel> {

	public static ScriptCommand<?> fromCompound(RCompound compound) {
		ScriptType type = ScriptType.values()[(byte) compound.get("type").getValue()];
		ScriptCommand<?> command = getScriptCommandByType(type);
		if (command == null) {
			return null;
		}
		command.loadDataFromModel0(compound.get("data"));
		return command;
	}

	public static ScriptCommand<?> getScriptCommandByType(ScriptType type) {
		switch (type) {
		case ENABLE:
			return new EnableScript();
		case EXIT:
			return new ExitScript();
		case GIVE_WEAPON:
			return new GiveWeaponScript();
		case PLAY_SOUND:
			return new PlaySoundScript();
		case SET_MISSION:
			return new SetMissionScript();
		case SHOW:
			return new ShowScript();
		case SHOW_DIALOG:
			return new ShowDialogScript();
		case SLEEP:
			return new SleepScript();
		case TAKE_AWAY_ALL:
			return new TakeAwayAllScript();
		default:
			return null;
		}
	}

	private final RType dataModelType;
	private final ScriptType type;

	public ScriptCommand(ScriptType type, RType dataModelType) {
		this.dataModelType = dataModelType;
		this.type = type;
	}

	public final ScriptType getType() {
		return type;
	}

	public final RCompound asCompound() {
		RCompound compound = new RCompound();
		compound.set("type", new RByte((byte) getType().ordinal()));
		if (dataModelType != null) {
			compound.set("data", dataAsModel());
		}
		return compound;
	}

	@SuppressWarnings("unchecked")
	private final void loadDataFromModel0(RModel model) {
		if (model == null || model.getType() != dataModelType) {
			return;
		}
		loadDataFromModel((E) model);
	}

	protected abstract void loadDataFromModel(E model);

	protected abstract E dataAsModel();

}
