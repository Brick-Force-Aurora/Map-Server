package org.playuniverse.brickforce.maprepository.shaded.redis.stream;

import org.playuniverse.brickforce.maprepository.shaded.redis.event.stream.RedisStreamMessageEvent;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.io.RIOModel;
import org.playuniverse.brickforce.maprepository.shaded.redis.utils.DataSerialization;

import com.syntaxphoenix.syntaxapi.event.EventManager;

import redis.clients.jedis.BinaryJedisPubSub;

class RedisStreamListener extends BinaryJedisPubSub {

	private final EventManager manager;

	public RedisStreamListener(EventManager manager) {
		this.manager = manager;
	}

	@Override
	public void onMessage(byte[] channel, byte[] message) {
		manager.call(new RedisStreamMessageEvent(DataSerialization.asString(channel), RIOModel.MODEL.read(message)));
	}

}
