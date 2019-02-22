package com.example.belatrix;

/**
 * EnumAppender.
 *
 * @author Luis Alonso Ballena Garcia
 */

public enum  EnumAppender {
    CONSOLE,FILE,DATABASE;

    public static EnumAppender getByName(String name){
        for(EnumAppender appender : EnumAppender.values()){
            if(name.equalsIgnoreCase(appender.name())){
                return appender;
            }
        }
        return null;
    }

}
