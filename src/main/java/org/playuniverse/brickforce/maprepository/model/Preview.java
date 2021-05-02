package org.playuniverse.brickforce.maprepository.model;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

public final class Preview {
	
	private final short slot; 			// Unsigned byte
	private final String alias;		
	private final int bricks;			// int32
	private final LocalDateTime date;
	private final BufferedImage image;	// Thumbnail
	
	public Preview(short slot, String alias, int bricks, LocalDateTime date, BufferedImage image) {
		this.slot = slot;
		this.alias = alias;
		this.bricks = bricks;
		this.date = date;
		this.image = image;
	}
	
	public short getSlot() {
		return slot;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public int getBricks() {
		return bricks;
	}
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public BufferedImage getImage() {
		return image;
	}

}
