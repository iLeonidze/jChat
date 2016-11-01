package com.ileonidze.jchat;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.sql.*;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;

/*
Packages - com.sun.eng
Classes - class ImageSprite
Interfaces - interface RasterDelegate
Methods - getBackground()
Variables - float myWidth
Constants - static final int GET_THE_CPU = 1;
 */

public class Main {
    static Connection connection = null;
    private static final String SQLDBConnectionLink = "jdbc:hsqldb:file:jchat.db";
    private static Gson gson = new Gson();
    static String[] executionArguments = null;
    private static ConsoleListener consoleListening = new ConsoleListener();
    private static final Logger console = Logger.getLogger(Main.class);
    private static boolean workingStatus = false;
    private static SelfService service = new SelfService();
    private static final Thread selfServiceThread = new Thread(() -> {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!workingStatus) {
                    workingStatus = true;
                    service.proceed(executionArguments);
                    workingStatus = false;
                }
            }
        }, 0, 100);
    });


    private static boolean isSQLDBStarted() {
        String login = "SA";
        String password = "";
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection(SQLDBConnectionLink, login, password);
            connection.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //LOG4J: FATAL,ERROR,WARN,INFO,DEBUG;TRACE
    private static long VDBRestore() {
        String[] snapshotParts = {"chats", "users", "sessions"};
        long usageAnaliticsMTimestampStart = new Date().getTime();
        for (String snapshotPartName : snapshotParts) {
            if (Arrays.asList(executionArguments).contains("snapshot")) {
                try {
                    FileInputStream fileIn = new FileInputStream("vdb/vdb." + snapshotPartName + ".snapshot");
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    switch (snapshotPartName) {
                        case "chats":
                            VDB.chats = (ArrayList<VDBChat>) in.readObject();
                            break;
                        case "users":
                            VDB.users = (ArrayList<VDBUser>) in.readObject();
                            break;
                        case "sessions":
                            VDB.sessions = (ArrayList<VDBSession>) in.readObject();
                            break;
                    }
                    in.close();
                    fileIn.close();
                } catch (FileNotFoundException ex) {
                    console.warn("No " + snapshotPartName + " VDB-snapshot was found");
                } catch (AccessDeniedException ex) {
                    console.warn(snapshotPartName + " VDB-snapshot is locked by other program. " + snapshotPartName + " will be reset.");
                } catch (ClassNotFoundException ex) {
                    console.warn("Incorrect " + snapshotPartName + " VDB snapshot structure - no such class was found");
                } catch (EOFException ex) {
                    console.warn(snapshotPartName + " snapshot is incompatible with this jChat version. " + snapshotPartName + " will be reset.");
                } catch (IOException ex) {
                    console.warn("Database " + snapshotPartName + " restoring error");
                    ex.printStackTrace();
                }
            }
            if (Arrays.asList(executionArguments).contains("json")) {
                try {
                    switch (snapshotPartName) {
                        case "chats":
                            VDB.chats = gson.fromJson(new FileReader("vdb/vdb.chats.json"), new TypeToken<ArrayList<VDBChat>>() {
                            }.getType());
                            if (VDB.chats == null) VDB.chats = new ArrayList<>();
                            break;
                        case "users":
                            VDB.users = gson.fromJson(new FileReader("vdb/vdb.users.json"), new TypeToken<ArrayList<VDBUser>>() {
                            }.getType());
                            if (VDB.users == null) VDB.users = new ArrayList<>();
                            break;
                        case "sessions":
                            VDB.sessions = gson.fromJson(new FileReader("vdb/vdb.sessions.json"), new TypeToken<ArrayList<VDBSession>>() {
                            }.getType());
                            if (VDB.sessions == null) VDB.sessions = new ArrayList<>();
                            break;
                    }
                } catch (FileNotFoundException ex) {
                    console.warn("No " + snapshotPartName + " VDB-snapshot was found");
                }
            }
        }
        return new Date().getTime() - usageAnaliticsMTimestampStart;
    }

    public static void main(String[] args) {
        executionArguments = args;
        console.info("jchat 1.0.19");
        console.debug("Startup arguments: " + Arrays.toString(args));

        if (Arrays.asList(args).contains("json") || Arrays.asList(args).contains("snapshot")) {
            console.info("Restoring VDB from " + (Arrays.asList(args).contains("json") ? "JSON" : "Snapshot") + "...");
            long restoringTime = VDBRestore();

            console.info("Users: " + VDB.users.size());
            console.info("Sessions: " + VDB.sessions.size());
            console.info("Chats: " + VDB.chats.size());
            console.info("Restoring was performed for " + (restoringTime / 1000) + " seconds");

            if (!Arrays.asList(args).contains("noSelfService")) {
                selfServiceThread.start();
            }
        } else if (Arrays.asList(args).contains("sql")) {
            console.info("Running in SQL mode, starting SQL thread...");
            if (isSQLDBStarted()) {
                console.info("SQL thread connected");

                boolean chatsTableExists = false;
                boolean messagesTableExists = false;
                boolean sessionsTableExists = false;
                boolean usersTableExists = false;
                try {
                    DatabaseMetaData md = connection.getMetaData();
                    ResultSet rs = md.getTables(null, null, "%", null);
                    while (rs.next()) {
                        if (rs.getString(3).equals("chats")) {
                            chatsTableExists = true;
                            console.info("Chats table exists");
                        }
                        if (rs.getString(3).equals("messages")) {
                            messagesTableExists = true;
                            console.info("Messages table exists");
                        }
                        if (rs.getString(3).equals("sessions")) {
                            sessionsTableExists = true;
                            console.info("Sessions table exists");
                        }
                        if (rs.getString(3).equals("users")) {
                            usersTableExists = true;
                            console.info("Users table exists");
                        }
                    }
                    rs.close();
                } catch (Exception e) {
                    console.error("Unknown exception occurred while trying to check tables health");
                }
                if (!chatsTableExists) {
                    console.info("Chats table is not exists, creating...");
                    try {
                        Statement statement = connection.createStatement();
                        String sql = "CREATE TABLE PUBLIC.\"chats\"\n" +
                                "(\"id\" VARCHAR(32) not null,\n" +
                                "\"name\" VARCHAR(64) not null,\n" +
                                "\"ownerID\" VARCHAR(32) not null,\n" +
                                "\"membersIDs\" VARCHAR(1024) not null,\n" +
                                "\"createdTime\" INTEGER default 0 not null,\n" +
                                "\"isGroupStatus\" BOOLEAN default FALSE not null,\n" +
                                "\"messages\" VARCHAR(16384) not null)";
                        statement.executeUpdate(sql);
                        statement.close();
                        console.info("Chats table created successfully");
                    } catch (Exception e) {
                        console.error("Can\'t create new \"chats\" table");
                    }
                }
                if (!messagesTableExists) {
                    console.info("Messages table is not exists, creating...");
                    try {
                        Statement statement = connection.createStatement();
                        String sql = "CREATE TABLE PUBLIC.\"messages\"\n" +
                                "(\"id\" VARCHAR(32) not null,\n" +
                                "\"ownerID\" VARCHAR(32) not null,\n" +
                                "\"creatorID\" VARCHAR(32) not null,\n" +
                                "\"chatID\" VARCHAR(32) not null,\n" +
                                "\"forwarded\" BOOLEAN default FALSE not null,\n" +
                                "\"sentTime\" INTEGER default 0 not null,\n" +
                                "\"body\" VARCHAR(1024) not null)";
                        statement.executeUpdate(sql);
                        statement.close();
                        console.info("Messages table created successfully");
                    } catch (Exception e) {
                        console.error("Can\'t create new \"messages\" table");
                    }
                }
                if (!sessionsTableExists) {
                    console.info("Sessions table is not exists, creating...");
                    try {
                        Statement statement = connection.createStatement();
                        String sql = "CREATE TABLE PUBLIC.\"sessions\"\n" +
                                "(\"hash\" VARCHAR(32) not null,\n" +
                                "\"ip\" VARCHAR(16) not null,\n" +
                                "\"userID\" VARCHAR(32) not null,\n" +
                                "\"createdTimestamp\" INTEGER default 0 not null,\n" +
                                "\"usedTimestamp\" INTEGER default NULL)";
                        statement.executeUpdate(sql);
                        statement.close();
                        console.info("Sessions table created successfully");
                    } catch (Exception e) {
                        console.error("Can\'t create new \"sessions\" table");
                    }
                }
                if (!usersTableExists) {
                    console.info("Users table is not exists, creating...");
                    try {
                        Statement statement = connection.createStatement();
                        String sql = "CREATE TABLE PUBLIC.\"users\"\n" +
                                "(\"id\" VARCHAR(32) not null,\n" +
                                "\"login\" VARCHAR(16) not null,\n" +
                                "\"password\" VARCHAR(64) not null,\n" +
                                "\"name\" VARCHAR(32) not null,\n" +
                                "\"email\" VARCHAR(32) not null,\n" +
                                "\"gender\" INTEGER default 1 not null,\n" +
                                "\"registeredTime\" INTEGER default 0 not null,\n" +
                                "\"loggedInTime\" INTEGER default NULL,\n" +
                                "\"lastOnlineTime\" INTEGER default NULL,\n" +
                                "\"chatIDs\" VARCHAR(1024) default '' not null,\n" +
                                "\"accessLevel\" INTEGER default 0 not null,\n" +
                                "\"bannedState\" BOOLEAN default FALSE not null)";
                        statement.executeUpdate(sql);
                        statement.close();
                        console.info("Users table created successfully");
                    } catch (Exception e) {
                        console.error("Can\'t create new \"users\" table");
                    }
                }
                try {
                    connection.commit();
                    console.info("Database committed");
                } catch (Exception e) {
                    console.error("Database commit failed");
                }
            } else {
                console.error("SQL Database start failure");
            }
        }
        //try{ connection.close(); }catch(Exception e){}
        if (Arrays.asList(args).contains("console")) { // here we are switching into console mode
            consoleListening.run();
        } else if (Arrays.asList(args).contains("server")) { // here we are running server // TODO NIO
            console.warn("Server mode is not implemented yet");
        } else {
            console.error("Startup arguments is not specified. For console mode - specify \"console\" argument, otherwise for server - user \"server\" argument.");
        }
    }
}