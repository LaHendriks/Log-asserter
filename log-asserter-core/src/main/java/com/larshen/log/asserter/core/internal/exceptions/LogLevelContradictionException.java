package com.larshen.log.asserter.core.internal.exceptions;

import ch.qos.logback.classic.Logger;
import com.larshen.log.asserter.core.LogLevel;

public class LogLevelContradictionException extends RuntimeException {

    public LogLevelContradictionException(Logger logger, LogLevel level){
        super(String.format("Logger(%s) is not set to log messages at the %s level!", logger.getName(), level.name()));
    }
}
