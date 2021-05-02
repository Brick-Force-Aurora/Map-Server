package org.playuniverse.brickforce.maprepository.model.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.playuniverse.brickforce.maprepository.model.Brick;
import org.playuniverse.brickforce.maprepository.model.Geometry;
import org.playuniverse.brickforce.maprepository.model.Script;
import org.playuniverse.brickforce.maprepository.model.Transform;
import org.playuniverse.brickforce.maprepository.model.data.Buffer;
import org.playuniverse.brickforce.maprepository.model.script.ScriptCommand;
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
		return null;
	}

	@Override
	public void toStream(Geometry geometry, OutputStream stream) throws IOException {
		stream.write(toBytes(geometry));
	}

	@Override
	public byte[] toBytes(Geometry geometry) throws IOException {
		ByteBuf buf = Unpooled.buffer();
		
		return buf.array();
	}

	@Override
	public byte[] toBytes(Geometry geometry, int capacity) throws IOException {
		ByteBuf buf = Unpooled.buffer(capacity);

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
			
		}
		return new Brick(id, code, template, new Transform(posX, posY, posZ, rotation), script);
	}
	
	public void writeBrick(Brick brick, ByteBuf buffer) {
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
