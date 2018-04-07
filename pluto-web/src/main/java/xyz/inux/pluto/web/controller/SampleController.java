package xyz.inux.pluto.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import xyz.inux.pluto.domain.SampleDomain;
import xyz.inux.pluto.model.enums.ResultEnum;
import xyz.inux.pluto.service.SampleService;
import xyz.inux.pluto.model.pojo.dto.sample.RedisInDto;
import xyz.inux.pluto.model.pojo.dto.sample.RedisOutDto;
import xyz.inux.pluto.web.support.Result;
import xyz.inux.pluto.model.pojo.vo.sample.*;

@RestController
@RequestMapping(value = "/sample")
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private SampleDomain sampleDomain;

    // url 带参数
    @RequestMapping(value = {"/get/echo/{words}"}, method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result<GetOutVo> sGet(@PathVariable("words") String wordsIn) {

        // 1 获得入参
        GetInVo getInVo = new GetInVo();
        getInVo.setWords(wordsIn);

        // 2 业务处理
        String wordsOut = getInVo.getWords() + " get !";

        // 3 组装出参
        GetOutVo getOutVo = new GetOutVo();
        getOutVo.setWords(wordsOut);

        // 4 return
        ResultEnum resultEnum = ResultEnum.SUCCESS;
        return new Result<>(resultEnum.getCode(), resultEnum.getDesc(), getOutVo);
    }

    // form 提交
    @RequestMapping(value = {"/post/echo"}, method = {RequestMethod.POST}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result<PostOutVo> sPost(PostInVo postInVo) {

        // 1 获得入参

        // 2 业务处理
        String wordsOut = postInVo.getWords() + " post !";

        // 3 组装出参
        PostOutVo postOutVo = new PostOutVo();
        postOutVo.setWords(wordsOut);

        // 4 return
        ResultEnum resultEnum = ResultEnum.SUCCESS;
        return new Result<>(resultEnum.getCode(), resultEnum.getDesc(), postOutVo);
    }

    @RequestMapping(value = {"/redis/{key}"}, method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result<RedisOutVo> sRedis(@PathVariable("key") String key) {
        // 1 获得入参
        RedisInVo redisInVo = new RedisInVo();
        redisInVo.setKey(key);

        // 2 业务处理
        RedisInDto redisInDto = new RedisInDto();
        redisInDto.setKey(redisInVo.getKey());
        RedisOutDto redisOutDto = sampleService.sRedis(redisInDto);

        // 3 组装出参
        RedisOutVo redisOutVo = new RedisOutVo();
        redisOutVo.setValue(redisOutDto.getValue());

        // 4 return
        ResultEnum resultEnum = ResultEnum.SUCCESS;
        return new Result<>(resultEnum.getCode(), resultEnum.getDesc(), redisOutVo);
    }

    @RequestMapping(value = {"/db/{id}"}, method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result<DbOutVo> sDb(@PathVariable("id") String id) {

        DbOutVo dbOutVo = new DbOutVo();
        dbOutVo.setData(sampleService.sDb(id));

        ResultEnum resultEnum = ResultEnum.SUCCESS;
        return new Result<>(resultEnum.getCode(), resultEnum.getDesc(), dbOutVo);
    }

    @RequestMapping(value = {"/dbTrans/{id}"}, method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result<DbOutVo> sDbTrans(@PathVariable("id") String id) {

        DbOutVo dbOutVo = new DbOutVo();
        dbOutVo.setData(sampleDomain.sDbTrans(id));

        ResultEnum resultEnum = ResultEnum.SUCCESS;
        return new Result<>(resultEnum.getCode(), resultEnum.getDesc(), dbOutVo);
    }

}
