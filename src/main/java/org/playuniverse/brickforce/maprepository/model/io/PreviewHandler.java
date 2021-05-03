package org.playuniverse.brickforce.maprepository.model.io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;

import org.playuniverse.brickforce.maprepository.model.Preview;
import org.playuniverse.brickforce.maprepository.model.data.Buffer;

import com.syntaxphoenix.syntaxapi.utils.io.Deserializer;
import com.syntaxphoenix.syntaxapi.utils.io.Serializer;
import com.syntaxphoenix.syntaxapi.utils.java.Streams;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PreviewHandler implements Serializer<Preview>, Deserializer<Preview> {
	
	public static final PreviewHandler PREVIEW = new PreviewHandler();
	
	private PreviewHandler() {}
	
	@Override
	public Preview fromStream(InputStream stream) throws IOException {
		return fromBytes(Streams.toByteArray(stream));
	}

	@Override
	public Preview fromBytes(byte[] bytes) throws IOException {
		return fromBuffer(Unpooled.wrappedBuffer(bytes));
	}
	
	public Preview fromBuffer(ByteBuf buf) throws IOException {
		short slot = buf.readUnsignedByte();
		String alias = Buffer.readString(buf);
		int bricks = buf.readInt();

		int year = buf.readShort();
		int month = buf.readByte();
		int day = buf.readByte();
		int hour = buf.readByte();
		int minute = buf.readByte();
		int second = buf.readByte();
		LocalDateTime date = LocalDateTime.of(year, month, day, hour, minute, second);

		int length = buf.readInt();
		BufferedImage image = null;
		if (length > 0) {
			byte[] imageData = new byte[length];
			for (int index = 0; index < length; index++) {
				imageData[index] = buf.readByte();
			}
			try (ByteArrayInputStream stream = new ByteArrayInputStream(imageData)) {
				image = ImageIO.read(stream);
			} catch (IOException e) {
				// Ignore, we just have no image then
			}
		}
		return new Preview(slot, alias, bricks, date, image);
	}

	@Override
	public void toStream(Preview preview, OutputStream stream) throws IOException {
		stream.write(toBytes(preview));
	}

	@Override
	public byte[] toBytes(Preview preview) throws IOException {
		ByteBuf buf = Unpooled.buffer();
		toBuffer(preview, buf);
		return buf.array();
	}

	@Override
	public byte[] toBytes(Preview preview, int capacity) throws IOException {
		ByteBuf buf = Unpooled.buffer(capacity);
		toBuffer(preview, buf);
		return buf.array();
	}
	
	public void toBuffer(Preview preview, ByteBuf buf) throws IOException {
		buf.writeByte(preview.getSlot());
		Buffer.writeString(buf, preview.getAlias());
		buf.writeInt(preview.getBricks());
		LocalDateTime date = preview.getDate();
		buf.writeShort(date.getYear());
		buf.writeByte(date.getMonthValue());
		buf.writeByte(date.getDayOfMonth());
		buf.writeByte(date.getHour());
		buf.writeByte(date.getMinute());
		buf.writeByte(date.getSecond());
		if (preview.getImage() == null) {
			buf.writeInt(0);
			return;
		}
		byte[] image;
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			ImageIO.write(preview.getImage(), "jpeg", stream);
			image = stream.toByteArray();
		}
		buf.writeInt(image.length);
		buf.writeBytes(image);
	}

}
