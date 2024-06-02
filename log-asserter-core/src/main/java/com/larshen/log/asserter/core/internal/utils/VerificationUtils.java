package com.larshen.log.asserter.core.internal.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.larshen.log.asserter.core.LogLevel;
import com.larshen.log.asserter.core.internal.exceptions.LogLevelContradictionException;
import com.larshen.log.asserter.core.internal.exceptions.UnmatchedLoggingException;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.larshen.log.asserter.core.internal.exceptions.UnmatchedLoggingException.formatMessages;

public class VerificationUtils {

    public static void verifyLogLevel(Logger logger, LogLevel logLevel) {
        if (!logger.isEnabledFor(Level.toLevel(logLevel.name()))) {
            throw new LogLevelContradictionException(logger, logLevel);
        }
    }

    public static void verifyLogMessages(List<ILoggingEvent> events, String... messages) {
        List<String> matched = Arrays.stream(messages)
                .filter(expected -> events.stream()
                        .anyMatch(event -> event.getMessage().contains(expected)))
                .collect(Collectors.toList());

        List<String> unmatched = new ArrayList<>(Arrays.asList(messages));
        unmatched.removeAll(matched);

        if(!unmatched.isEmpty()){
            throw new UnmatchedLoggingException(formatMessages(unmatched));
        }

        Assertions.assertNotNull(unmatched);
    }
}
