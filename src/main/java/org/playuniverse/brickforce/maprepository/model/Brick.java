package org.playuniverse.brickforce.maprepository.model;

import org.playuniverse.brickforce.maprepository.model.script.Script;

public final class Brick {

	private final int id;
	private final int code; // Unsigned short
	private final short template; // Unsigned byte

	private final Transform transform;
	private final Script script;

	public Brick(int id, int code, short template, Transform transform, Script script) {
		this.id = id;
		this.code = code;
		this.template = template;
		this.transform = transform;
		this.script = script;
	}

	public int getId() {
		return id;
	}

	public int getCode() {
		return code;
	}

	public short getTemplate() {
		return template;
	}

	public Transform getTransform() {
		return transform;
	}

	public boolean hasScript() {
		return script != null;
	}

	public Script getScript() {
		return script;
	}

}
