package xyz.inux.pluto.domain.repository.impl;

import org.springframework.stereotype.Service;

import xyz.inux.pluto.domain.repository.SampleRepository;
import xyz.inux.pluto.domain.repository.bo.sample.RedisInBo;
import xyz.inux.pluto.domain.repository.bo.sample.RedisOutBo;

@Service("SampleRepository")
public class SampleRepositoryImpl implements SampleRepository {

    public RedisOutBo sRedis(RedisInBo redisInBo) {
        RedisOutBo redisOutBo = new RedisOutBo();
        redisOutBo.setValue("outBo");
        return redisOutBo;
    }
}
