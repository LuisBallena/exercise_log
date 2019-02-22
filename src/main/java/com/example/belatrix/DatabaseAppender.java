package com.example.belatrix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

/**
 * DatabaseAppender.
 *
 * @author Luis Alonso Ballena Garcia
 */

public class DatabaseAppender extends Appender {

    private Connection connection;

    public DatabaseAppender(Map params) {
        super(params);
    }

    public void init() {
        try {
            connection = initDatabase();
        } catch (SQLException e) {
            throw new RuntimeException("DatabaseAppender failed", e);
        }
    }

    public void write(String message, EnumLevel level) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(String.format("insert into LOG_VALUES(message,log_level) values ('%s','%d');",message,
                    level.ordinal()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection initDatabase() throws SQLException {
        Connection connection = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", super.params.get(ParamAppender.DATABASE_USER));
        connectionProps.put("password", super.params.get(ParamAppender.DATABASE_PASSWORD));
        connection = DriverManager.getConnection("jdbc:" + super.params.get(ParamAppender.DATABASE_URL)
                + ":" + super.params.get(ParamAppender.DATABASE_PORT) + "/", connectionProps);
        return connection;
    }
}
