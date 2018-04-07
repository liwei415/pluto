package xyz.inux.pluto.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
@RequestMapping(value="/pluto")
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger("WEB.LOG");

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

    public void writeWarnLog(final String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    public void writeInfoLog(final String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public void writeDebugLog(final String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }


}
