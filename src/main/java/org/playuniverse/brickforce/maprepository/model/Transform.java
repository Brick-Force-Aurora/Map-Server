package org.playuniverse.brickforce.maprepository.model;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RShort;

public class Transform {
	
	private final short posX;		// Unsigned byte
	private final short posY;		// Unsigned byte
	private final short posZ;		// Unsigned byte
	private final short rotation;	// Unsigned byte
	
	public Transform(RCompound compound) {
		this.posX = (short) compound.get("x").getValue();
		this.posY = (short) compound.get("y").getValue();
		this.posZ = (short) compound.get("z").getValue();
		this.rotation = (short) compound.get("rotation").getValue();
	}
	
	public Transform(short posX, short posY, short posZ, short rotation) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.rotation = rotation;
	}
	
	public short getPosX() {
		return posX;
	}
	
	public short getPosY() {
		return posY;
	}
	
	public short getPosZ() {
		return posZ;
	}
	
	public short getRotation() {
		return rotation;
	}
	
	public RCompound asCompound() {
		RCompound compound = new RCompound();
		compound.set("x", new RShort(posX));
		compound.set("y", new RShort(posY));
		compound.set("z", new RShort(posZ));
		compound.set("rotation", new RShort(rotation));
		return compound;
	}
	
}
