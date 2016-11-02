package com.ileonidze.jchat;

import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

class ConsoleAPIExit extends ConsoleAPIEvalProto {
    private Logger console = Logger.getLogger(ConsoleAPI.class);

    public ConsoleResponse proceed(String[] commandParts) {
        console.info("Application exit");
        if (Arrays.asList(Main.executionArguments).contains("sql")) {
            try {
                Statement statement = Main.connection.createStatement();
                statement.executeUpdate("SHUTDOWN");
                statement.close();
                console.info("SQL DB was shut down");
                Main.connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
                console.error("Database failure");
            }
        }
        System.exit(0);
        return new ConsoleResponse("info", "Ended");
    }
}