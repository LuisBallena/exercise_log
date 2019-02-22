package com.example.belatrix;

import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * ConsoleAppender.
 *
 * @author Luis Alonso Ballena Garcia
 */

public class ConsoleAppender extends Appender {

    private static Logger logger = Logger.getLogger(ConsoleAppender.class.getName());
    private ConsoleHandler ch;

    public ConsoleAppender(Map params) {
        super(params);
    }

    public void init() {
        ch = new ConsoleHandler();
        ch.setFormatter(new SimpleFormatter());
        logger.addHandler(ch);
        logger.setUseParentHandlers(false);
    }

    public void write(String message, EnumLevel enumLevel) {
        this.logger.log(Level.INFO, message); // level anywhere (practical case)
    }

}
