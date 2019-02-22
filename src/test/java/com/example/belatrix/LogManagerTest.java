package com.example.belatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * LogManagerTest.
 *
 * @author Luis Alonso Ballena Garcia
 */

public class LogManagerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private String DATABASE_URL = "h2:mem:testdb";
    private String DATABASE_PORT = "";
    private String DATABASE_USER = "sa";
    private String DATABASE_PASSWORD = "";

    @Test
    public void shouldViewInConsole() {
        Map<String, String> params = new HashMap();
        EnumLevel rootLevel = EnumLevel.INFO;
        LogManager logManager = new LogManager(new String[]{"Console"}, params, rootLevel);
        logManager.log("HELLO INFO", EnumLevel.INFO);
        Handler[] handlers = Logger.getLogger(ConsoleAppender.class.getName()).getHandlers();
        Handler handler = obtainConsoleHandler(handlers);
        Assert.assertNotNull(handler);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionBecauseInvalidFileParameters() {
        Map<String, String> params = new HashMap();
        EnumLevel rootLevel = EnumLevel.INFO;
        LogManager logManager = new LogManager(new String[]{"File"}, params, rootLevel);
        logManager.log("Hello File INFO", EnumLevel.INFO);
    }

    @Test
    public void shouldViewInFile() throws IOException {
        Map<String, String> params = new HashMap();
        params.put(ParamAppender.DIRECTORY, temporaryFolder.getRoot().getPath());
        EnumLevel rootLevel = EnumLevel.INFO;
        LogManager logManager = new LogManager(new String[]{"File"}, params, rootLevel);
        logManager.log("Hello File INFO", EnumLevel.INFO);
        logManager.log("Hello File WARNING", EnumLevel.WARNING);
        logManager.log("Hello File ERROR", EnumLevel.ERROR);
        File file = new File( temporaryFolder.getRoot().getPath()+"\\logFile.txt");
        Assert.assertTrue(file.exists());
        byte[] data = Files.readAllBytes(Paths.get(file.toURI()));
        Assert.assertTrue(new String(data).contains("Hello File INFO"));
        Assert.assertTrue(new String(data).contains("Hello File WARNING"));
        Assert.assertTrue(new String(data).contains("Hello File ERROR"));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionBecauseInvalidDatabaseParameters() {
        Map<String, String> params = new HashMap();
        EnumLevel rootLevel = EnumLevel.INFO;
        LogManager logManager = new LogManager(new String[]{"Database"}, params, rootLevel);
        logManager.log("Hello Database ERROR", EnumLevel.ERROR);
    }


    @Test
    public void shouldViewInDatabase() throws Exception {
        Map<String, String> params = new HashMap();
        params.put(ParamAppender.DATABASE_URL, DATABASE_URL);
        params.put(ParamAppender.DATABASE_PORT, DATABASE_PORT);
        params.put(ParamAppender.DATABASE_USER, DATABASE_USER);
        params.put(ParamAppender.DATABASE_PASSWORD, DATABASE_PASSWORD);
        EnumLevel rootLevel = EnumLevel.INFO;
        createTable();
        LogManager logManager = new LogManager(new String[]{"Database"}, params, rootLevel);
        logManager.log("Hello Database ERROR", EnumLevel.ERROR);
        List<String> list = readTable();
        truncateTable();
        Assert.assertEquals(1,list.size());
        Assert.assertTrue(list.get(0).contains("Hello Database ERROR"));
    }

    @Test
    public void shouldViewInConsoleAndFile() throws IOException {
        Map<String, String> params = new HashMap();
        params.put(ParamAppender.DIRECTORY, temporaryFolder.getRoot().getPath());
        EnumLevel rootLevel = EnumLevel.INFO;
        LogManager logManager = new LogManager(new String[]{"Console","file"}, params, rootLevel);
        logManager.log("HELLO CONSOLE & File INFO", EnumLevel.INFO);
        Handler[] handlers = Logger.getLogger(ConsoleAppender.class.getName()).getHandlers();
        Handler handler = obtainConsoleHandler(handlers);
        Assert.assertNotNull(handler);
        File file = new File( temporaryFolder.getRoot().getPath()+"\\logFile.txt");
        Assert.assertTrue(file.exists());
        byte[] data = Files.readAllBytes(Paths.get(file.toURI()));
        Assert.assertTrue(new String(data).contains("HELLO CONSOLE & File INFO"));
    }

    @Test
    public void shouldViewInConsoleAndFileAndDatabase() throws Exception {
        Map<String, String> params = new HashMap();
        params.put(ParamAppender.DIRECTORY, temporaryFolder.getRoot().getPath());
        params.put(ParamAppender.DATABASE_URL, DATABASE_URL);
        params.put(ParamAppender.DATABASE_PORT, DATABASE_PORT);
        params.put(ParamAppender.DATABASE_USER, DATABASE_USER);
        params.put(ParamAppender.DATABASE_PASSWORD, DATABASE_PASSWORD);
        EnumLevel rootLevel = EnumLevel.INFO;
        createTable();
        LogManager logManager = new LogManager(new String[]{"Console","file","Database"}, params, rootLevel);
        logManager.log("HELLO CONSOLE & File & Database INFO", EnumLevel.INFO);
        Handler[] handlers = Logger.getLogger(ConsoleAppender.class.getName()).getHandlers();
        Handler handler = obtainConsoleHandler(handlers);
        Assert.assertNotNull(handler);
        File file = new File( temporaryFolder.getRoot().getPath()+"\\logFile.txt");
        Assert.assertTrue(file.exists());
        byte[] data = Files.readAllBytes(Paths.get(file.toURI()));
        Assert.assertTrue(new String(data).contains("HELLO CONSOLE & File & Database"));
        List<String> list = readTable();
        truncateTable();
        Assert.assertEquals(1,list.size());
        Assert.assertTrue(list.get(0).contains("HELLO CONSOLE & File & Database"));
    }

    @Test
    public void shouldValidateRootLevelWarning() throws Exception {
        Map<String, String> params = new HashMap();
        params.put(ParamAppender.DATABASE_URL, DATABASE_URL);
        params.put(ParamAppender.DATABASE_PORT, DATABASE_PORT);
        params.put(ParamAppender.DATABASE_USER, DATABASE_USER);
        params.put(ParamAppender.DATABASE_PASSWORD, DATABASE_PASSWORD);
        EnumLevel rootLevel = EnumLevel.WARNING;
        createTable();
        LogManager logManager = new LogManager(new String[]{"Database"}, params, rootLevel);
        logManager.log("Hello Database ERROR", EnumLevel.ERROR);
        logManager.log("Hello Database INFO", EnumLevel.INFO);
        logManager.log("Hello Database WARN", EnumLevel.WARNING);
        List<String> list = readTable();
        truncateTable();
        Assert.assertEquals(2,list.size());
        Assert.assertTrue(list.get(0).contains("Hello Database ERROR"));
        Assert.assertTrue(list.get(1).contains("Hello Database WARN"));
    }


    @Test
    public void shouldValidateRootLevelError() throws Exception {
        Map<String, String> params = new HashMap();
        params.put(ParamAppender.DATABASE_URL, DATABASE_URL);
        params.put(ParamAppender.DATABASE_PORT, DATABASE_PORT);
        params.put(ParamAppender.DATABASE_USER, DATABASE_USER);
        params.put(ParamAppender.DATABASE_PASSWORD, DATABASE_PASSWORD);
        EnumLevel rootLevel = EnumLevel.ERROR;
        createTable();
        LogManager logManager = new LogManager(new String[]{"Database"}, params, rootLevel);
        logManager.log("Hello Database ERROR", EnumLevel.ERROR);
        logManager.log("Hello Database INFO", EnumLevel.INFO);
        logManager.log("Hello Database WARN", EnumLevel.WARNING);
        List<String> list = readTable();
        truncateTable();
        Assert.assertEquals(1,list.size());
        Assert.assertTrue(list.get(0).contains("Hello Database ERROR"));
    }


    private Handler obtainConsoleHandler(Handler[] handlers) {
        for (Handler handler : handlers) {
            if (handler instanceof ConsoleHandler) {
                return handler;
            }
        }
        return null;
    }

    private void createTable() throws SQLException {
        Connection connection = getConnection();
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("create table if not EXISTS LOG_VALUES(message varchar(500) not null, log_level int not null)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<String> readTable() throws SQLException {
        List<String> list = new ArrayList<>();
        Connection connection = getConnection();
        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM LOG_VALUES");
            while(resultSet.next()){
                list.add(resultSet.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void truncateTable() throws SQLException {
        Connection connection = getConnection();
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("truncate table LOG_VALUES");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        Connection connection = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", DATABASE_USER);
        connectionProps.put("password", DATABASE_PASSWORD);
        connection = DriverManager.getConnection("jdbc:" + DATABASE_URL + ":" + DATABASE_PORT + "/", connectionProps);
        return connection;
    }



}
