package com.example.belatrix;

import java.util.Map;

/**
 * Appender.
 *
 * @author Luis Alonso Ballena Garcia
 */

public abstract class Appender {

    protected Map params;

    public Appender(Map params) {
        this.params = params;
    }

    public abstract void init();

    public abstract void write(String message, EnumLevel level);

}
