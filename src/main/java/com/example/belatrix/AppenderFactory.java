package com.example.belatrix;

import java.util.Map;

/**
 * AppenderFactory.
 *
 * @author Luis Alonso Ballena Garcia
 */

public class AppenderFactory {

    public static Appender createAppender(EnumAppender enumAppender, Map params) {
        Appender appender = null;
        switch (enumAppender) {
            case CONSOLE:
                appender = new ConsoleAppender(params);
                break;
            case FILE:
                appender = new FileAppender(params);
                break;
            case DATABASE:
                appender = new DatabaseAppender(params);
                break;
        }
        return appender;
    }


}
