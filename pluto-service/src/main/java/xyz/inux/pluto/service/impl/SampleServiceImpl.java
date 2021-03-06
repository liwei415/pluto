package xyz.inux.pluto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.inux.pluto.domain.SampleDomain;
import xyz.inux.pluto.model.pojo.bo.sample.RedisInBo;
import xyz.inux.pluto.model.pojo.bo.sample.RedisOutBo;
import xyz.inux.pluto.service.AbstractService;
import xyz.inux.pluto.service.SampleService;
import xyz.inux.pluto.model.pojo.dto.sample.RedisInDto;
import xyz.inux.pluto.model.pojo.dto.sample.RedisOutDto;

@Service("SampleService")
public class SampleServiceImpl extends AbstractService implements SampleService {

    @Autowired
    private SampleDomain sampleDomain;

    public RedisOutDto sRedis(RedisInDto redisInDto) {

        // 1 入参处理
        RedisInBo redisInBo = new RedisInBo();
        redisInBo.setKey(redisInDto.getKey());

        // 2 调用repo
        RedisOutBo redisOutBo = sampleDomain.sRedis(redisInBo);

        // 3 返回处理
        RedisOutDto redisOutDto = new RedisOutDto();
        redisOutDto.setValue(redisOutBo.getValue() + " vvv");

        return redisOutDto;
    }

    public String sDb(String id) {
        return sampleDomain.sDb(id + "hhhh");
    }
}
