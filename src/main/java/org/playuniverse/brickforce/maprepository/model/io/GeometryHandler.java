package org.playuniverse.brickforce.maprepository.model.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.playuniverse.brickforce.maprepository.model.Brick;
import org.playuniverse.brickforce.maprepository.model.Geometry;
import org.playuniverse.brickforce.maprepository.model.Script;
import org.playuniverse.brickforce.maprepository.model.Transform;
import org.playuniverse.brickforce.maprepository.model.data.Buffer;
import org.playuniverse.brickforce.maprepository.model.util.BrickModifier;

import com.syntaxphoenix.syntaxapi.utils.io.Deserializer;
import com.syntaxphoenix.syntaxapi.utils.io.Serializer;
import com.syntaxphoenix.syntaxapi.utils.java.Streams;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class GeometryHandler implements Serializer<Geometry>, Deserializer<Geometry> {

	@Override
	public Geometry fromStream(InputStream stream) throws IOException {
		return fromBytes(Streams.toByteArray(stream));
	}

	@Override
	public Geometry fromBytes(byte[] bytes) throws IOException {
		ByteBuf buf = Unpooled.wrappedBuffer(bytes);
		buf.skipBytes(4); // Ignore version, because it never changed
		int map = buf.readInt();
		int skybox = buf.readInt();
		int count = buf.readInt();
		ArrayList<Brick> bricks = new ArrayList<>();
		for(int index = 0; index < count; index++) {
			bricks.add(readBrick(buf));
		}
		return new Geometry(map, skybox, bricks);
	}

	@Override
	public void toStream(Geometry geometry, OutputStream stream) throws IOException {
		stream.write(toBytes(geometry));
	}

	@Override
	public byte[] toBytes(Geometry geometry) throws IOException {
		ByteBuf buf = Unpooled.buffer();
		buf.writeInt(1); // Write version, even tho it never changed
		buf.writeInt(geometry.getMap());
		buf.writeInt(geometry.getSkybox());
		for(Brick brick : geometry.getBricks()) {
			writeBrick(buf, brick);
		}
		return buf.array();
	}

	@Override
	public byte[] toBytes(Geometry geometry, int capacity) throws IOException {
		ByteBuf buf = Unpooled.buffer(capacity);
		buf.writeInt(1); // Write version, even tho it never changed
		buf.writeInt(geometry.getMap());
		buf.writeInt(geometry.getSkybox());
		for(Brick brick : geometry.getBricks()) {
			writeBrick(buf, brick);
		}
		return buf.array();
	}
	
	public Brick readBrick(ByteBuf buffer) {
		int id = buffer.readInt();
		short template = buffer.readUnsignedByte();
		short posX = buffer.readUnsignedByte();
		short posY = buffer.readUnsignedByte();
		short posZ = buffer.readUnsignedByte();
		int code = buffer.readUnsignedShort();
		short rotation = buffer.readUnsignedByte();
		Script script = null;
		if(BrickModifier.isFunctional(template)) {
			String alias = Buffer.readString(buffer);
			boolean enableAwake = buffer.readBoolean();
			boolean visibleAwake = buffer.readBoolean();
			String command = Buffer.readString(buffer);
			script = new Script(alias, enableAwake, visibleAwake, command);
		}
		return new Brick(id, code, template, new Transform(posX, posY, posZ, rotation), script);
	}
	
	public void writeBrick(ByteBuf buffer, Brick brick) {
		buffer.writeInt(brick.getId());
		buffer.writeByte(brick.getTemplate());
		Transform transform = brick.getTransform();
		buffer.writeByte(transform.getPosX());
		buffer.writeByte(transform.getPosY());
		buffer.writeByte(transform.getPosZ());
		buffer.writeShort(brick.getCode());
		buffer.writeByte(transform.getRotation());
		if(brick.hasScript()) {
			Script script = brick.getScript();
			Buffer.writeString(buffer, script.getAlias());
			buffer.writeBoolean(script.isEnableOnAwake());
			buffer.writeBoolean(script.isVisibleOnAwake());
			Buffer.writeString(buffer, script.asCommandString());
		}
	}

}
