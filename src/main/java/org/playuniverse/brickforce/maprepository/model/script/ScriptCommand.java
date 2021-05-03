package org.playuniverse.brickforce.maprepository.model.script;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RByte;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RModel;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RType;

public abstract class ScriptCommand<E extends RModel> {

	public static final String[] CMD_DELIMITERS = new String[] {
			")(*&",
			"\0"
	};
	public static final String[] ARG_DELIMITERS = new String[] {
			"!Q#$",
			"\0"
	};

	public static ScriptCommand<?> fromCompound(RCompound compound) {
		ScriptType type = ScriptType.values()[(byte) compound.get("type").getValue()];
		ScriptCommand<?> command = getScriptCommandByType(type);
		if (command == null) {
			return null;
		}
		command.loadDataFromModel0(compound.get("data"));
		return command;
	}

	public static ScriptCommand<?> fromArguments(ScriptType type, String[] arguments) {
		ScriptCommand<?> command = getScriptCommandByType(type);
		return (command == null || !command.loadDataFromArguments(arguments)) ? null : command;
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

	public final String asDescription() {
		String[] data = dataAsArguments();
		if (data == null) {
			return type.typeName();
		}
		return type.typeName() + ARG_DELIMITERS[0] + String.join(ARG_DELIMITERS[0], data);
	}

	@SuppressWarnings("unchecked")
	private final void loadDataFromModel0(RModel model) {
		if (model == null || model.getType() != dataModelType) {
			return;
		}
		loadDataFromModel((E) model);
	}

	protected abstract boolean loadDataFromArguments(String[] arguments);

	protected abstract String[] dataAsArguments();

	protected abstract void loadDataFromModel(E model);

	protected abstract E dataAsModel();

}
