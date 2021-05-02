package org.playuniverse.brickforce.maprepository.model.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.playuniverse.brickforce.maprepository.model.BrickMap;

import com.syntaxphoenix.syntaxapi.utils.io.Deserializer;
import com.syntaxphoenix.syntaxapi.utils.io.Serializer;
import com.syntaxphoenix.syntaxapi.utils.java.Streams;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class BrickMapHandler implements Serializer<BrickMap>, Deserializer<BrickMap> {

	@Override
	public BrickMap fromStream(InputStream stream) throws IOException {
		return fromBytes(Streams.toByteArray(stream));
	}

	@Override
	public BrickMap fromBytes(byte[] bytes) throws IOException {
		return null;
	}

	@Override
	public void toStream(BrickMap map, OutputStream stream) throws IOException {
		stream.write(toBytes(map));
	}

	@Override
	public byte[] toBytes(BrickMap map) throws IOException {
		ByteBuf buf = Unpooled.buffer();
		
		return buf.array();
	}

	@Override
	public byte[] toBytes(BrickMap map, int capacity) throws IOException {
		ByteBuf buf = Unpooled.buffer(capacity);

		return buf.array();
	}

}
