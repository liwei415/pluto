package xyz.inux.pluto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.inux.pluto.domain.repository.SampleRepository;
import xyz.inux.pluto.domain.repository.bo.sample.RedisInBo;
import xyz.inux.pluto.domain.repository.bo.sample.RedisOutBo;
import xyz.inux.pluto.service.SampleService;
import xyz.inux.pluto.service.dto.sample.RedisInDto;
import xyz.inux.pluto.service.dto.sample.RedisOutDto;

@Service("SampleService")
public class SampleServiceImpl implements SampleService {

    @Autowired
    private SampleRepository sampleRepository;

    public RedisOutDto sRedis(RedisInDto redisInDto) {

        // 1 入参处理
        RedisInBo redisInBo = new RedisInBo();
        redisInBo.setKey(redisInDto.getKey());

        // 2 调用repo
        RedisOutBo redisOutBo = sampleRepository.sRedis(redisInBo);

        // 3 返回处理
        RedisOutDto redisOutDto = new RedisOutDto();
        redisOutDto.setValue(redisOutBo.getValue() + " vvv");

        return redisOutDto;
    }

}
