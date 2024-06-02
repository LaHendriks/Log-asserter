package com.larshen.log.asserter.core;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.larshen.log.asserter.core.internal.exceptions.LogLevelContradictionException;
import com.larshen.log.asserter.core.internal.utils.VerificationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class VerificationUtilsTest {

    public static Stream<Logger> getLoggers() {
        Logger logger = (Logger) LoggerFactory.getLogger(VerificationUtils.class);
        Logger classNamelogger = (Logger) LoggerFactory.getLogger("VerificationUtils");
        Logger rootLogger = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);
        return Stream.of(logger, classNamelogger, rootLogger);
    }

    @ParameterizedTest
    @MethodSource("getLoggers")
    public void testVerifyLogger(Logger logger){
        logger.setLevel(Level.INFO);

        Assertions.assertThrows(LogLevelContradictionException.class,
                () -> VerificationUtils.verifyLogLevel(logger, LogLevel.TRACE));
        Assertions.assertThrows(LogLevelContradictionException.class,
                () -> VerificationUtils.verifyLogLevel(logger, LogLevel.DEBUG));
        Assertions.assertDoesNotThrow(() -> VerificationUtils.verifyLogLevel(logger, LogLevel.INFO));
        Assertions.assertDoesNotThrow(() -> VerificationUtils.verifyLogLevel(logger, LogLevel.WARN));
        Assertions.assertDoesNotThrow(() -> VerificationUtils.verifyLogLevel(logger, LogLevel.ERROR));
    }
}
