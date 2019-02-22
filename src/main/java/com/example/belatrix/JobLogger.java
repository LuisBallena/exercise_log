package com.example.belatrix;

import java.util.Map;

/**
 * com.example.belatrix.JobLogger.
 *
 * @author Luis Alonso Ballena Garcia
 */

public class JobLogger {

    private LogManager logManager;

    public JobLogger(String[] logToSources, Map dbParamsMap, EnumLevel rootLevel) {
        logManager = new LogManager(logToSources, dbParamsMap, rootLevel);
    }

    public void logMessage(String messageText, EnumLevel messageLevel) {
        messageText.trim();
        if (messageText == null || messageText.length() == 0) {
            return;
        }
        logManager.log(messageText, messageLevel);
    }

}
