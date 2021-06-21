package org.playuniverse.brickforce.maprepository.net;

import java.io.IOException;

import org.playuniverse.brickforce.maprepository.model.BrickMap;
import org.playuniverse.brickforce.maprepository.model.io.BrickMapHandler;
import org.playuniverse.brickforce.maprepository.model.util.ModelHandler;

import com.syntaxphoenix.syntaxapi.net.http.Answer;

public class MapAnswer extends Answer<BrickMap> {

	private BrickMap map;

	public MapAnswer() {
		super(NetDataType.OCTET_STREAM);
	}

	@Override
	public boolean hasResponse() {
		return map != null;
	}

	public MapAnswer setResponse(BrickMap map) {
		this.map = map;
		header("Mode-Mask", map.getModeMask());
		return this;
	}

	@Override
	public BrickMap getResponse() {
		return map;
	}

	@Override
	public MapAnswer clearResponse() {
		this.map = null;
		return this;
	}

	@Override
	public byte[] serializeResponse() {
		try {
			return BrickMapHandler.BRICK_MAP.toBytes(map);
		} catch (IOException e) {
			return ModelHandler.EMPTY_ARRAY;
		}
	}

}
