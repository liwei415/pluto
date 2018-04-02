package xyz.inux.pluto.web.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import xyz.inux.pluto.common.enums.ResultEnum;
import xyz.inux.pluto.web.support.Result;
import xyz.inux.pluto.web.vo.sample.GetIn;
import xyz.inux.pluto.web.vo.sample.GetOut;
import xyz.inux.pluto.web.vo.sample.PostIn;
import xyz.inux.pluto.web.vo.sample.PostOut;

@RestController
@RequestMapping(value = "/sample")
public class SampleController {

    // url 带参数
    @RequestMapping(value = {"/get/echo/{words}"}, method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result<GetOut> sGet(@PathVariable("words") String wordsIn) {

        // 1 获得入参
        GetIn getIn = new GetIn();
        getIn.setWords(wordsIn);

        // 2 业务处理
        String wordsOut = getIn.getWords() + " get !";

        // 3 组装出参
        GetOut getOut = new GetOut();
        getOut.setWords(wordsOut);

        // 4 return
        ResultEnum resultEnum = ResultEnum.SUCCESS;
        return new Result<GetOut>(resultEnum.getCode(), resultEnum.getDesc(), getOut);
    }

    // form 提交
    @RequestMapping(value = {"/post/echo"}, method = {RequestMethod.POST}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result<PostOut> sPost(PostIn postIn) {

        // 1 获得入参

        // 2 业务处理
        String wordsOut = postIn.getWords() + " post !";

        // 3 组装出参
        PostOut postOut = new PostOut();
        postOut.setWords(wordsOut);

        // 4 return
        ResultEnum resultEnum = ResultEnum.SUCCESS;
        return new Result<PostOut>(resultEnum.getCode(), resultEnum.getDesc(), postOut);
    }

    @RequestMapping(value = {"/db/{id}"}, method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String sDb(@PathVariable("id") String id) {
        return "sDb: " + id;
    }

    @RequestMapping(value = {"/redis/{key}"}, method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String sRedis(@PathVariable("key") String key) {
        return "sRedis: " + key;
    }

}
