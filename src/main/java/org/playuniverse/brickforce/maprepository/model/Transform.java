package org.playuniverse.brickforce.maprepository.model;

public class Transform {
	
	private final short posX;		// Unsigned byte
	private final short posY;		// Unsigned byte
	private final short posZ;		// Unsigned byte
	private final short rotation;	// Unsigned byte
	
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
	
}
