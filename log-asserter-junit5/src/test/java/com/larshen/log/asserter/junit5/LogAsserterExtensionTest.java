package com.larshen.log.asserter.junit5;


import com.larshen.log.asserter.core.LogLevel;
import com.larshen.log.asserter.core.internal.exceptions.UnmatchedLoggingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static com.larshen.log.asserter.core.internal.exceptions.UnmatchedLoggingException.formatMessages;

@ExtendWith(LogAsserterExtension.class)
class LogAsserterExtensionTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAsserterExtensionTest.class);
    private static final String DEFAULT_LOG_MESSAGE = "my log message";

    @RegisterExtension
    private final LogAsserterExtension logAsserter = new LogAsserterExtension();

    @RegisterExtension
    private final LogAsserterExtension logAsserterClass = new LogAsserterExtension(LogAsserterExtensionTest.class);

    @RegisterExtension
    private final LogAsserterExtension logAsserterStringClass = new LogAsserterExtension(LogAsserterExtensionTest.class.getName());

    @Test
    void testDefaultLog(){
        LOGGER.debug(DEFAULT_LOG_MESSAGE);
        logAsserter.logged(LogLevel.DEBUG, DEFAULT_LOG_MESSAGE);
        logAsserterClass.logged(LogLevel.DEBUG, DEFAULT_LOG_MESSAGE);
        logAsserterStringClass.logged(LogLevel.DEBUG, DEFAULT_LOG_MESSAGE);
    }

    @Test
    void testUnmatchedLoggingException(){
        LOGGER.info(DEFAULT_LOG_MESSAGE);

        String[] messages = new String[]{DEFAULT_LOG_MESSAGE, "test1", "test2", "test3",
                "test4", "test5", "test6", "test7", "test8", "test9"};
        String expectedMessage = formatMessages(Arrays.asList(messages));

        Assertions.assertThrows(UnmatchedLoggingException.class,
                () -> logAsserter.logged(LogLevel.INFO, messages),
                new UnmatchedLoggingException(expectedMessage).getMessage());

        Assertions.assertThrows(UnmatchedLoggingException.class,
                () -> logAsserterClass.logged(LogLevel.INFO, messages),
                new UnmatchedLoggingException(expectedMessage).getMessage());

        Assertions.assertThrows(UnmatchedLoggingException.class,
                () -> logAsserterStringClass.logged(LogLevel.INFO, messages),
                new UnmatchedLoggingException(expectedMessage).getMessage());
    }

    @Test
    void testLogFromFiles() throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        Path messages = Paths.get(Objects.requireNonNull(classloader.getResource("messages.txt")).toURI());
        Path test = Paths.get(Objects.requireNonNull(classloader.getResource("test.txt")).toURI());

        Assertions.assertThrows(UnmatchedLoggingException.class,
                () -> logAsserter.logged(LogLevel.INFO, messages, test));

        LOGGER.info("message1");
        LOGGER.info("message2");
        LOGGER.info("message3");
        LOGGER.info("partially message4");

        LOGGER.info("test1");
        LOGGER.info("test2");
        LOGGER.info("test3");
        LOGGER.info("partially test4");
        logAsserter.logged(LogLevel.INFO, messages, test);
    }
}
