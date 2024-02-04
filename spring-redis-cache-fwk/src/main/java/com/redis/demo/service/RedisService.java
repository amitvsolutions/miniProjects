package com.redis.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    public void setString(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }
    }

    public String getString(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public void addToList(String key, String... values) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(key, values);
        }
    }

    public List<String> getList(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrange(key, 0, -1);
        }
    }
	
}
