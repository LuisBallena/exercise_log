package com.example.belatrix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
    private static String DATABASE_USER = "user";
    private static String DATABASE_PASSWORD= "password";

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
        String insertSql = "insert into LOG_VALUES(message,log_level) values (?,?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
            preparedStatement.setString(1,message);
            preparedStatement.setInt(2,level.ordinal());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); //practical case
        }
    }

    private Connection initDatabase() throws SQLException {
        Connection connection = null;
        Properties connectionProps = new Properties();
        connectionProps.put(DATABASE_USER, super.params.get(ParamAppender.DATABASE_USER));
        connectionProps.put(DATABASE_PASSWORD, super.params.get(ParamAppender.DATABASE_PASSWORD));
        connection = DriverManager.getConnection("jdbc:" + super.params.get(ParamAppender.DATABASE_URL)
                + ":" + super.params.get(ParamAppender.DATABASE_PORT) + "/", connectionProps);
        return connection;
    }
}
