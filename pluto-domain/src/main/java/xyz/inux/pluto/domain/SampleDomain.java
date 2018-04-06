package xyz.inux.pluto.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.inux.pluto.domain.bo.sample.RedisInBo;
import xyz.inux.pluto.domain.bo.sample.RedisOutBo;
import xyz.inux.pluto.domain.repository.RedisRepository;
import xyz.inux.pluto.domain.repository.SampleRepository;

@Service("SampleDomain")
public class SampleDomain {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private SampleRepository sampleRepository;

    public RedisOutBo sRedis(RedisInBo redisInBo) {

        String value = redisRepository.get(redisInBo.getKey());

        RedisOutBo redisOutBo = new RedisOutBo();
        redisOutBo.setValue(value);
        return redisOutBo;
    }

    public String sDb(String id) {
        return sampleRepository.getUserById(id);
    }


}
