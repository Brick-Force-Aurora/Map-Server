package org.playuniverse.brickforce.maprepository.model.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.playuniverse.brickforce.maprepository.model.Geometry;

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
	public void toStream(Geometry preview, OutputStream stream) throws IOException {
		stream.write(toBytes(preview));
	}

	@Override
	public byte[] toBytes(Geometry preview) throws IOException {
		ByteBuf buf = Unpooled.buffer();
		
		return buf.array();
	}

	@Override
	public byte[] toBytes(Geometry preview, int capacity) throws IOException {
		ByteBuf buf = Unpooled.buffer(capacity);

		return buf.array();
	}

}
