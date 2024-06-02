package com.larshen.log.asserter.core;

import java.nio.file.Path;

public interface LogAsserter {
    /**
     * Testing this
     * @param logLevel of the messages
     * @param messages that should be checked in the log
     */
    void logged(LogLevel logLevel, String... messages);

    void logged(LogLevel logLevel, Path... files);
}
