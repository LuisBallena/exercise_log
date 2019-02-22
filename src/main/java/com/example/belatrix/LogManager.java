package com.example.belatrix;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * LogManager.
 *
 * @author Luis Alonso Ballena Garcia
 */

public class LogManager {
    private List<EnumAppender> enumAppenders = null;
    private List<Appender> appenders = new ArrayList<>();
    private EnumLevel rootLevel;

    public LogManager(String[] appenders, Map dbParams, EnumLevel rootEnumLevel) {
        validateEnumAppender(appenders);
        buildAppenders(dbParams);
        this.rootLevel = rootEnumLevel;
    }

    public void log(String messageText, EnumLevel messageLevel) {
        messageText = parseMessage(messageText, messageLevel);
        if (registerMessage(messageLevel)) {
            for (Appender appender : appenders) {
                appender.write(messageText, messageLevel);
            }
        }
    }

    private String parseMessage(String message, EnumLevel messageLevel) {
        return String.format("%s %s %s", messageLevel, DateFormat.getDateInstance(DateFormat.LONG).format(new Date())
                , message);
    }

    private void validateEnumAppender(String[] appender) {
        enumAppenders = findEnumAppender(appender);
        if (enumAppenders == null || enumAppenders.isEmpty()) {
            throw new RuntimeException("Invalid appender");
        }
    }

    private List<EnumAppender> findEnumAppender(String[] appenders) {
        List<EnumAppender> list = new ArrayList();
        if (appenders != null && appenders.length > 0) {
            for (String appender : appenders) {
                EnumAppender enumAppender = EnumAppender.getByName(appender);
                if (enumAppender != null) {
                    list.add(enumAppender);
                }
            }
        }
        return list;
    }

    private void buildAppenders(Map dbParams) {
        for (EnumAppender enumAppender : enumAppenders) {
            Appender appender = AppenderFactory.createAppender(enumAppender, dbParams);
            appender.init();
            appenders.add(appender);
        }
    }

    private boolean registerMessage(EnumLevel messageLevel) {
        boolean validate = true;
        switch (rootLevel) {
            case WARNING:
                validate = !messageLevel.equals(EnumLevel.INFO);
                break;
            case ERROR:
                validate = messageLevel.equals(EnumLevel.ERROR);
                break;
        }
        return validate;
    }

}
