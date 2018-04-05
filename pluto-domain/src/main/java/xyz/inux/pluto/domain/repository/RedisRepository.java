package xyz.inux.pluto.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.inux.pluto.domain.support.ConstantContext;

@Service("RedisRepository")
public class RedisRepository {

    @Autowired
    private ConstantContext constantContext;

    public String get(String key) {
        // 使用方法baidu查询RedisTemplate
        return constantContext.getRedisTemplate().opsForValue().get(key).toString();
    }
}
