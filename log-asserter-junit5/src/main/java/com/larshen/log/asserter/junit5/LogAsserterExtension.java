package com.larshen.log.asserter.junit5;


import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.larshen.log.asserter.core.LogAsserter;
import com.larshen.log.asserter.core.LogLevel;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static com.larshen.log.asserter.core.internal.utils.FileUtils.getAllLinesFromFiles;
import static com.larshen.log.asserter.core.internal.utils.VerificationUtils.verifyLogLevel;
import static com.larshen.log.asserter.core.internal.utils.VerificationUtils.verifyLogMessages;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;


public class LogAsserterExtension implements LogAsserter, BeforeEachCallback, AfterEachCallback {
    private final Logger logger;
    private ListAppender<ILoggingEvent> listAppender;

    /**
     *  Default constructor that uses the ROOT logger
     */
    public LogAsserterExtension(){
        logger = (Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);
    }

    /**
     * A constructor that uses a custom logger
     * @param logClass is the class that is being logged.
     */
    public LogAsserterExtension(Class<?> logClass) {
        logger = (Logger) LoggerFactory.getLogger(logClass);
    }

    /**
     * A constructor that uses a custom logger
     * @param logClassName is the name of the class being logged.
     */
    public LogAsserterExtension(String logClassName){
        logger = (Logger) LoggerFactory.getLogger(logClassName);
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext){
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext){
        listAppender.stop();
        logger.detachAppender(listAppender);
    }

    @Override
    public void logged(LogLevel logLevel, String... messages) {
        verifyLogLevel(logger, logLevel);
        verifyLogMessages(listAppender.list, messages);
    }

    @Override
    public void logged(LogLevel logLevel, Path... files) {
        verifyLogLevel(logger, logLevel);
        String[] lines = getAllLinesFromFiles(files);
        verifyLogMessages(listAppender.list, lines);
    }
}
