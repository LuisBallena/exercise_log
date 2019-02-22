package com.example.belatrix;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * FileAppender.
 *
 * @author Luis Alonso Ballena Garcia
 */

public class FileAppender extends Appender {

    private static Logger logger = Logger.getLogger(FileAppender.class.getName());
    private FileHandler fh;

    public FileAppender(Map params) {
        super(params);
    }

    public void init() {
        try {
            createFile();
            fh = new FileHandler(String.format("%s%s%s", super.params.get(ParamAppender.DIRECTORY), File.separator,
                    super.params.get(ParamAppender.FILENAME)));
            fh.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            throw new RuntimeException("FileAppender failed", e);
        }
        logger.addHandler(fh);
    }

    public void write(String message, EnumLevel enumLevel) {
        logger.log(Level.INFO, message); // level anywhere (practical case)
    }

    private void createFile() throws IOException {
        File logFile = new File(String.format("%s%s%s", super.params.get(ParamAppender.DIRECTORY), File.separator,
                super.params.get(ParamAppender.FILENAME)));
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
    }

}
