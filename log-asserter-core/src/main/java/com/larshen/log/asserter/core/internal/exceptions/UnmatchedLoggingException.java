package com.larshen.log.asserter.core.internal.exceptions;

import java.util.List;

public class UnmatchedLoggingException extends AssertionError {

    public UnmatchedLoggingException(String formattedString){
        super(String.format("Could not find a match for the following expected log messages: %s", formattedString));
    }

    public static String formatMessages(List<String> messages){
        StringBuilder stringBuilder = new StringBuilder();
        for(String message : messages){
            stringBuilder.append(System.lineSeparator());
            stringBuilder.append(message);
        }
        return stringBuilder.toString();
    }
}
