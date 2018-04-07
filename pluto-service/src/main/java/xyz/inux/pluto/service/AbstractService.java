package xyz.inux.pluto.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.inux.pluto.model.LogModel;

/**
 * @Author: DanielCao
 * @Date:   2017年5月8日
 * @Time:   下午2:46:00
 *
 */
public class AbstractService {
    protected static Logger logger = LoggerFactory.getLogger("SERVICE.LOG");

    protected void writeLog(LogModel lm) {
        if (logger.isInfoEnabled()) {
            logger.info(lm.toJson());
        }
    }

    protected void writeLog(LogModel lm, boolean isClear) {
        if (logger.isInfoEnabled()) {
            logger.info(lm.toJson(isClear));
        }
    }

    protected void writeLog(String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    protected void writeErrorLog(LogModel lm, Throwable e) {
        if (logger.isErrorEnabled()) {
            logger.error(lm.toJson(false), e);
        }
    }

    protected void writeLog(Logger logger, LogModel lm) {
        if (logger.isInfoEnabled()) {
            logger.info(lm.toJson());
        }
    }

    protected void writeLog(Logger logger, LogModel lm, boolean isClear) {
        if (logger.isInfoEnabled()) {
            logger.info(lm.toJson(isClear));
        }
    }

    protected void writeLog(Logger logger, String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    protected void writeErrorLog(Logger logger, LogModel lm, Throwable e) {
        if (logger.isErrorEnabled()) {
            logger.error(lm.toJson(false), e);
        }
    }

}