package com.ileonidze.jchat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.Date;

class SelfService {
    private static final Logger console = Logger.getLogger(Main.class);
    private static final String[] snapshotParts = {"chats","users","sessions"};
    private static Gson gson = null;
    private int CollectDBGarbage(){
        int currentTimestamp = Math.round(new Date().getTime()/1000);
        int collected = 0;
        for(int i=0;i<VDB.sessions.size();i++){
            VDBSession someSession = VDB.sessions.get(i);
            if(currentTimestamp-someSession.getUsedTimestamp()>259200){ // 259200 seconds = 3 days
                collected += ObjectSizeFetcher.getObjectSize(someSession);
                VDB.sessions.remove(i);
            }
        }
        return collected;
    }
    public long proceed(String[] executionArguments){
        if(gson==null){
            if(Arrays.asList(executionArguments).contains("beautifulJson")){
                gson = new GsonBuilder().setPrettyPrinting().create();
            }else{
                gson = new Gson();
            }
        }
        CollectDBGarbage();
        long usageAnalyticsMTimestampStart = new Date().getTime();
            for(String snapshotPartName: snapshotParts){
                if(Arrays.asList(executionArguments).contains("snapshot")) {
                    try {
                        FileOutputStream fos = new FileOutputStream("vdb/vdb." + snapshotPartName + ".snapshot");
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        switch (snapshotPartName) {
                            case "chats":
                                oos.writeObject(VDB.chats);
                                break;
                            case "users":
                                oos.writeObject(VDB.users);
                                break;
                            case "sessions":
                                oos.writeObject(VDB.sessions);
                                break;
                        }
                        fos.close();
                        oos.close();
                    } catch (IOException ex) {
                        console.error("Database save error");
                        ex.printStackTrace();
                    }
                }
                if(Arrays.asList(executionArguments).contains("json")){
                    try {
                        try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("vdb/vdb."+snapshotPartName+".json"), "utf-8"))) {
                            switch (snapshotPartName) {
                                case "chats":
                                    writer.write(gson.toJson(VDB.chats));
                                    break;
                                case "users":
                                    writer.write(gson.toJson(VDB.users));
                                    break;
                                case "sessions":
                                    writer.write(gson.toJson(VDB.sessions));
                                    break;
                            }
                            writer.close();
                        }
                    }catch(IOException e){
                        console.error("Database save error");
                        e.printStackTrace();
                    }
                }
            }
        return new Date().getTime()-usageAnalyticsMTimestampStart;
    }
}