package xyz.inux.pluto.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import xyz.inux.pluto.common.lang.WdRuntimeException;
import xyz.inux.pluto.model.enums.ResultEnum;
import xyz.inux.pluto.web.support.Result;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger("WEB.LOG");

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<Object> defaultErrorHandler(final HttpServletRequest req, HttpServletResponse resp, final Exception e) throws Exception {
        //设置状态码
        resp.setStatus(HttpStatus.OK.value());
        //设置ContentType
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //避免乱码
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-cache, must-revalidate");

        ResultEnum resultEnu = null;
        String cause = null;
        if(e instanceof NoHandlerFoundException){
            NoHandlerFoundException ne = (NoHandlerFoundException)e;
            cause = ne.getMessage();

            writeErrorLog("Path Not Found!", ne);
            resultEnu = ResultEnum.PATH_NOT_FOUND;
        } else if (e instanceof WdRuntimeException) {
            WdRuntimeException exception = (WdRuntimeException) e;

            writeErrorLog("ExceptionHandler biz exception", exception);
            resultEnu = ResultEnum.valueOf(exception.getErrorCode());
        } else {
            logger.error("ExceptionHandler sys exception", e);
            resultEnu = ResultEnum.SYS_ERROR;
        }
        return new Result<Object>(resultEnu.getCode(), resultEnu.getDesc(), cause);
    }

    public void writeErrorLog(final String message, final Throwable t) {
        if (logger.isErrorEnabled()) {
            logger.error(message, t);
        }
    }

    public void writeErrorLog(final String message) {
        if (logger.isErrorEnabled()) {
            logger.error(message);
        }
    }
}
