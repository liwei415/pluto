package xyz.inux.pluto.domain.repository;

import xyz.inux.pluto.domain.repository.bo.sample.RedisInBo;
import xyz.inux.pluto.domain.repository.bo.sample.RedisOutBo;

public interface SampleRepository {

    RedisOutBo sRedis(RedisInBo redisInBo);
}
