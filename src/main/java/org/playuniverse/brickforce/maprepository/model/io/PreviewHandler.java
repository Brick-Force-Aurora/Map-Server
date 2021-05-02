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

	@Override
	public Preview fromStream(InputStream stream) throws IOException {
		return fromBytes(Streams.toByteArray(stream));
	}

	@Override
	public Preview fromBytes(byte[] bytes) throws IOException {
		ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
		short slot = buffer.readUnsignedByte();
		String alias = Buffer.readString(buffer);
		int bricks = buffer.readInt();

		int year = buffer.readByte();
		int month = buffer.readByte();
		int day = buffer.readByte();
		int hour = buffer.readByte();
		int minute = buffer.readByte();
		int second = buffer.readByte();
		LocalDateTime date = LocalDateTime.of(year, month, day, hour, minute, second);

		int length = buffer.readInt();
		BufferedImage image = null;
		if (length > 0) {
			byte[] imageData = new byte[length];
			for (int index = 0; index < length; index++) {
				imageData[index] = buffer.readByte();
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
		buf.writeByte(preview.getSlot());
		Buffer.writeString(buf, preview.getAlias());
		buf.writeInt(preview.getBricks());
		LocalDateTime date = preview.getDate();
		buf.writeByte(date.getYear());
		buf.writeByte(date.getMonthValue());
		buf.writeByte(date.getDayOfMonth());
		buf.writeByte(date.getHour());
		buf.writeByte(date.getMinute());
		buf.writeByte(date.getSecond());
		if (preview.getImage() == null) {
			buf.writeInt(0);
			return buf.array();
		}
		byte[] image;
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			ImageIO.write(preview.getImage(), "jpeg", stream);
			image = stream.toByteArray();
		}
		buf.writeInt(image.length);
		buf.writeBytes(image);
		return buf.array();
	}

	@Override
	public byte[] toBytes(Preview preview, int capacity) throws IOException {
		ByteBuf buf = Unpooled.buffer(capacity);
		buf.writeByte(preview.getSlot());
		Buffer.writeString(buf, preview.getAlias());
		buf.writeInt(preview.getBricks());
		LocalDateTime date = preview.getDate();
		buf.writeByte(date.getYear());
		buf.writeByte(date.getMonthValue());
		buf.writeByte(date.getDayOfMonth());
		buf.writeByte(date.getHour());
		buf.writeByte(date.getMinute());
		buf.writeByte(date.getSecond());
		if (preview.getImage() == null) {
			buf.writeInt(0);
			return buf.array();
		}
		byte[] image;
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			ImageIO.write(preview.getImage(), "jpeg", stream);
			image = stream.toByteArray();
		}
		buf.writeInt(image.length);
		buf.writeBytes(image);
		return buf.array();
	}

}
