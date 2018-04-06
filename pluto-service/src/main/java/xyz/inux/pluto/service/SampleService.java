package xyz.inux.pluto.service;

import xyz.inux.pluto.service.dto.sample.RedisInDto;
import xyz.inux.pluto.service.dto.sample.RedisOutDto;

public interface SampleService {

    RedisOutDto sRedis(RedisInDto redisInDto);
    String sDb(String id);
}
