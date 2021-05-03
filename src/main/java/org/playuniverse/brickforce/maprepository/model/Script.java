package org.playuniverse.brickforce.maprepository.model;

import java.util.ArrayList;

import org.playuniverse.brickforce.maprepository.model.script.ScriptCommand;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;

public class Script {
	
	private final String alias;
	private final boolean enableAwake;
	private final boolean visibleAwake;
	private final ArrayList<ScriptCommand> commands;
	
	public Script(RCompound compound) {
		this.alias = (String) compound.get("alias").getValue();
		this.enableAwake = (boolean) compound.get("enable").getValue();
		this.visibleAwake = (boolean) compound.get("visible").getValue();
		this.commands = null;
	}
	
	public Script(String alias, boolean enableAwake, boolean visibleAwake, String compactCommands) {
		this.alias = alias;
		this.enableAwake = enableAwake;
		this.visibleAwake = visibleAwake;
		this.commands = null;
	}

	public String getAlias() {
		return alias;
	}

	public boolean isEnableOnAwake() {
		return enableAwake;
	}

	public boolean isVisibleOnAwake() {
		return visibleAwake;
	}

	public String asCommandString() {
		return "";
	}
	
	public RCompound asCompound() {
		RCompound compound = new RCompound();
		
		return compound;
	}

}
