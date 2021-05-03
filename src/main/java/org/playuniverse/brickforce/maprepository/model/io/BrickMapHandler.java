package org.playuniverse.brickforce.maprepository.model.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.playuniverse.brickforce.maprepository.model.BrickMap;
import org.playuniverse.brickforce.maprepository.model.Geometry;
import org.playuniverse.brickforce.maprepository.model.Preview;

import com.syntaxphoenix.syntaxapi.utils.io.Deserializer;
import com.syntaxphoenix.syntaxapi.utils.io.Serializer;
import com.syntaxphoenix.syntaxapi.utils.java.Streams;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class BrickMapHandler implements Serializer<BrickMap>, Deserializer<BrickMap> {

	public static final BrickMapHandler BRICK_MAP = new BrickMapHandler();

	private BrickMapHandler() {}

	@Override
	public BrickMap fromStream(InputStream stream) throws IOException {
		return fromBytes(Streams.toByteArray(stream));
	}

	@Override
	public BrickMap fromBytes(byte[] bytes) throws IOException {
		return fromBuffer(Unpooled.wrappedBuffer(bytes));
	}

	public BrickMap fromBuffer(ByteBuf buf) throws IOException  {
		long id = buf.readLong();
		Preview preview = PreviewHandler.PREVIEW.fromBuffer(buf);
		Geometry geometry = GeometryHandler.GEOMETRY.fromBuffer(buf);
		return new BrickMap(id, preview, geometry);
	}
	
	@Override
	public void toStream(BrickMap map, OutputStream stream) throws IOException {
		stream.write(toBytes(map));
	}

	@Override
	public byte[] toBytes(BrickMap map) throws IOException {
		ByteBuf buf = Unpooled.buffer();
		toBuffer(map, buf);
		return buf.array();
	}

	@Override
	public byte[] toBytes(BrickMap map, int capacity) throws IOException {
		ByteBuf buf = Unpooled.buffer(capacity);
		toBuffer(map, buf);
		return buf.array();
	}
	
	public void toBuffer(BrickMap map, ByteBuf buf) throws IOException {
		buf.writeLong(map.getId());
		PreviewHandler.PREVIEW.toBuffer(map.getPreview(), buf);
		GeometryHandler.GEOMETRY.toBuffer(map.getGeometry(), buf);
	}

}
