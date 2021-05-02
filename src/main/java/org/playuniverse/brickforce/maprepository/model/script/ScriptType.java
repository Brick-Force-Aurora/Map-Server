package org.playuniverse.brickforce.maprepository.model.script;

public enum ScriptType {

	ENABLE("enablescript"),
	SHOW_DIALOG("showdialog"),
	PLAY_SOUND("playsound"),
	SLEEP("sleep"),
	EXIT("exit"),
	SHOW("showscript"),
	GIVE_WEAPON("giveweapon"),
	TAKE_AWAY_ALL("takeawayall"),
	SET_MISSION("setmission");

	private final String typeName;

	private ScriptType(String typeName) {
		this.typeName = typeName;
	}

	public String typeName() {
		return typeName;
	}

	public static ScriptType fromString(String value) {
		for (ScriptType type : values()) {
			if (type.name().equalsIgnoreCase(value) || type.typeName().equalsIgnoreCase(value)) {
				return type;
			}
		}
		return null;
	}

}
