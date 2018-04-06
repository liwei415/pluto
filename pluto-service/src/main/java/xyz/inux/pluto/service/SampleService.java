package xyz.inux.pluto.service;

import xyz.inux.pluto.model.pojo.dto.sample.RedisInDto;
import xyz.inux.pluto.model.pojo.dto.sample.RedisOutDto;

public interface SampleService {

    RedisOutDto sRedis(RedisInDto redisInDto);
    String sDb(String id);
}
