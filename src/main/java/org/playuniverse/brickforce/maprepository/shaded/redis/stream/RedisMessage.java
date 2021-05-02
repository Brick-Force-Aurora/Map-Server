package org.playuniverse.brickforce.maprepository.shaded.redis.stream;

import org.playuniverse.brickforce.maprepository.shaded.redis.model.RModel;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.io.RIOModel;
import org.playuniverse.brickforce.maprepository.shaded.redis.model.io.RNamedModel;
import org.playuniverse.brickforce.maprepository.shaded.redis.utils.DataSerialization;

import redis.clients.jedis.Jedis;

public class RedisMessage {

	private final Jedis jedis;

	public RedisMessage(Jedis jedis) {
		this.jedis = jedis;
	}

	public Jedis getHandle() {
		return jedis;
	}

	public void send(String channel, RModel message) {
		jedis.publish(DataSerialization.fromString(channel), RIOModel.MODEL.write(new RNamedModel("root", message)));
	}

	public void send(String channel, String command, RModel message) {
		jedis.publish(DataSerialization.fromString(channel), RIOModel.MODEL.write(new RNamedModel(command, message)));
	}

}
