package org.playuniverse.brickforce.maprepository.model;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

import org.playuniverse.brickforce.maprepository.model.util.ModelConversion;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RByteArray;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RCompound;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RInt;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RShort;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.RString;

public final class Preview {

	private final short slot; // Unsigned byte
	private final String alias;
	private final int bricks; // int32
	private final LocalDateTime date;
	private final BufferedImage image; // Thumbnail

	public Preview(RCompound compound) {
		this.slot = (short) compound.get("slot").getValue();
		this.alias = (String) compound.get("alias").getValue();
		this.bricks = (int) compound.get("bricks").getValue();
		this.date = ModelConversion.fromCompound((RCompound) compound.get("date"));
		this.image = ModelConversion.fromByteArray(((RByteArray) compound.get("image")).getValue());
	}

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

	public RCompound asCompound() {
		RCompound compound = new RCompound();
		compound.set("slot", new RShort(slot));
		compound.set("alias", new RString(alias));
		compound.set("bricks", new RInt(bricks));
		compound.set("date", ModelConversion.asCompound(date));
		compound.set("image", new RByteArray(ModelConversion.toByteArray(image)));
		return compound;
	}

}
