package com.ileonidze.jchat;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.util.*;

import com.google.gson.Gson;
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
    private static Gson gson = new Gson();
    private static String[] executionArguments = null;
    private static ConsoleListener consoleListening = new ConsoleListener();
    private static final Logger console = Logger.getLogger(Main.class);
    private static boolean workingStatus = false;
    private static SelfService service = new SelfService();
    private static final Thread selfServiceThread = new Thread(() -> {
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                if(!workingStatus){
                    workingStatus = true;
                    service.proceed(executionArguments);
                    workingStatus = false;
                }
            }
        },0,100);
    });
    //LOG4J: FATAL,ERROR,WARN,INFO,DEBUG;TRACE
    private static long VDBRestore(){
        String[] snapshotParts = {"chats","users","sessions"};
        long usageAnaliticsMTimestampStart = new Date().getTime();
        for(String snapshotPartName: snapshotParts){
            if(Arrays.asList(executionArguments).contains("snapshot")) {
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
                }catch(FileNotFoundException ex){
                    console.warn("No "+snapshotPartName+" VDB-snapshot was found");
                }catch(AccessDeniedException ex){
                    console.warn(snapshotPartName+" VDB-snapshot is locked by other program. "+snapshotPartName+" will be reset.");
                }catch(ClassNotFoundException ex) {
                    console.warn("Incorrect "+snapshotPartName+" VDB snapshot structure - no such class was found");
                }catch(EOFException ex){
                    console.warn(snapshotPartName+" snapshot is incompatible with this jChat version. "+snapshotPartName+" will be reset.");
                }catch(IOException ex){
                    console.warn("Database "+snapshotPartName+" restoring error");
                    ex.printStackTrace();
                }
            }
            if(Arrays.asList(executionArguments).contains("json")){
                try {
                    switch (snapshotPartName){
                        case "chats":
                            VDB.chats = gson.fromJson(new FileReader("vdb/vdb.chats.json"), VDB.chats.getClass());
                            break;
                        case "users":
                            VDB.users = gson.fromJson(new FileReader("vdb/vdb.users.json"), VDB.users.getClass());
                            break;
                        case "sessions":
                            VDB.sessions = gson.fromJson(new FileReader("vdb/vdb.sessions.json"), VDB.sessions.getClass());
                            break;
                        }
                }catch(FileNotFoundException ex){
                    console.warn("No "+snapshotPartName+" VDB-snapshot was found");
                }
            }
        }
        return new Date().getTime()-usageAnaliticsMTimestampStart;
    }

    public static void main(String[] args) {
        executionArguments = args;
        // Here jchat determine what type of run user wants
        console.info("jchat 1.0.10");
        console.debug("Startup arguments: "+Arrays.toString(args));
        if(Arrays.asList(args).contains("vdb")){
            console.info("Restoring VDB from snapshot...");
            long restoringTime = VDBRestore();
            console.info("Users: " + VDB.users.size());
            console.info("Sessions: " + VDB.sessions.size());
            console.info("Chats: " + VDB.chats.size());
            console.info("Restoring was performed for " + (restoringTime / 1000) + " seconds");
            if (!Arrays.asList(args).contains("noSelfService")) {
                selfServiceThread.start();
            }
        }
        if(Arrays.asList(args).contains("console")) { // here we are switching into console mode
            consoleListening.run();
        }else if(Arrays.asList(args).contains("server")){ // here we are running server // TODO NIO
            console.warn("Server mode is not implemented yet");
        }else{
            console.error("Startup arguments is not specified. For console mode - specify \"console\" argument, otherwise for server - user \"server\" argument.");
        }
    }
}