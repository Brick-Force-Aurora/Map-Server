package org.playuniverse.brickforce.maprepository.model.data;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public final class Buffer {

	private Buffer() {}

	public static ByteBuf writePackedInt(ByteBuf buffer, int value) {
		return writePackedUnsignedInt(buffer, Integer.toUnsignedLong(value));
	}

	public static ByteBuf writePackedUnsignedInt(ByteBuf buffer, long value) {
		return setPackedUnsignedInt(buffer, buffer.readerIndex(), value);
	}

	public static ByteBuf setPackedInt(ByteBuf buffer, int index, int value) {
		return setPackedUnsignedInt(buffer, index, Integer.toUnsignedLong(value));
	}

	public static ByteBuf setPackedUnsignedInt(ByteBuf buffer, int index, long value) {
		do {
			short b = (short) (value & 0xFF);
			if (value >= 0x80) {
				b |= 0x80;
			}
			buffer.setByte(index++, b);
			value >>= 7;
		} while (value > 0);
		return buffer;
	}

	public static int readPackedInt(ByteBuf buffer) {
		return (int) readPackedUnsignedInt(buffer);
	}

	public static long readPackedUnsignedInt(ByteBuf buffer) {
		return getPackedUnsignedInt(buffer, new Int(buffer.readerIndex()));
	}

	public static int getPackedInt(ByteBuf buffer, Int index) {
		return (int) getPackedUnsignedInt(buffer, index);
	}

	public static long getPackedUnsignedInt(ByteBuf buffer, Int index) {
		boolean readMore = true;
		int shift = 0;
		long output = 0;
		while (readMore) {
			short value = buffer.getUnsignedByte(index.incGet());
			if (value >= 0x80) {
				readMore = true;
				value ^= 0x80;
			} else {
				readMore = false;
			}
			output |= (long) (value << shift);
			shift += 7;
		}
		return output;
	}

	public static ByteBuf writeString(ByteBuf buffer, String value) {
		return writeString(buffer, value, CharsetUtil.UTF_8);
	}

	public static ByteBuf writeString(ByteBuf buffer, String value, Charset charset) {
		return setString(buffer, buffer.writerIndex(), value, charset);
	}

	public static ByteBuf setString(ByteBuf buffer, int index, String value, Charset charset) {
		ByteBuf buf = Unpooled.buffer();
		buf.writeCharSequence(value, charset);
		byte[] data = buf.array();
		buf.release();
		buf.writeInt(data.length);
		byte[] dataLength = buf.array();
		int length = data.length + dataLength.length;
		buf.release();
		buffer.ensureWritable(length);
		buffer.setBytes(index, dataLength);
		buffer.setBytes(index + dataLength.length, data);
		return buffer;
	}

	public static String readString(ByteBuf buffer) {
		return readString(buffer, CharsetUtil.UTF_8);
	}

	public static String readString(ByteBuf buffer, Charset charset) {
		return getString(buffer, buffer.readerIndex(), CharsetUtil.UTF_8);
	}

	public static String getString(ByteBuf buffer, int index, Charset charset) {
		int length = buffer.getInt(index);
		CharSequence sequence = buffer.getCharSequence(index + 4, length, charset);
		return sequence instanceof String ? (String) sequence : sequence.toString();
	}

	public static ByteBuf writeHazelString(ByteBuf buffer, String value) {
		return writeHazelString(buffer, value, CharsetUtil.UTF_8);
	}

	public static ByteBuf writeHazelString(ByteBuf buffer, String value, Charset charset) {
		return setHazelString(buffer, buffer.writerIndex(), value, charset);
	}

	public static ByteBuf setHazelString(ByteBuf buffer, int index, String value, Charset charset) {
		ByteBuf buf = Unpooled.buffer();
		buf.writeCharSequence(value, charset);
		byte[] data = buf.array();
		buf.release();
		writePackedInt(buf, data.length);
		byte[] dataLength = buf.array();
		int length = data.length + dataLength.length;
		buf.release();
		buffer.ensureWritable(length);
		buffer.setBytes(index, dataLength);
		buffer.setBytes(index + dataLength.length, data);
		return buffer;
	}

	public static String readHazelString(ByteBuf buffer) {
		return readHazelString(buffer, CharsetUtil.UTF_8);
	}

	public static String readHazelString(ByteBuf buffer, Charset charset) {
		return getHazelString(buffer, buffer.readerIndex(), CharsetUtil.UTF_8);
	}

	public static String getHazelString(ByteBuf buffer, int rawIndex, Charset charset) {
		Int index = new Int(rawIndex);
		int length = getPackedInt(buffer, index);
		CharSequence sequence = buffer.getCharSequence(index.get(), length, charset);
		return sequence instanceof String ? (String) sequence : sequence.toString();
	}

	public static ByteBuf toHazelBuffer(ByteBuf buffer, boolean reliable) {
		ByteBuf buf = Unpooled.buffer(2048);
		if (reliable) {
			buf.writeByte((byte) 0);
		} else {
			int length = buffer.resetReaderIndex().readableBytes();
			buf.writeByte((byte) length);
			buf.writeByte((byte) (length >> 8));
			buf.writeByte((byte) 1);
		}
		buf.writeBytes(buffer);
		return buf;
	}

}
