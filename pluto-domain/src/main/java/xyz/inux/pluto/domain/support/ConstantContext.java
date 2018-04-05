package xyz.inux.pluto.domain.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service("constantContext")
public class ConstantContext {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}
}
