package com.ileonidze.jchat;

import java.io.Serializable;
import java.util.Date;

class VDBMessage implements Serializable {
    private String id;
    private String ownerID;
    private String creatorID;
    private String chatID;
    private boolean forwarded = false;
    private int sentTime = Math.round(new Date().getTime()/1000);
    private String body;

    VDBMessage init(String id, String ownerID, String chatID, String body){
        this.id = id;
        this.ownerID = ownerID;
        if(creatorID == null){
            creatorID = ownerID;
        }else{
            forwarded = true;
        }
        this.chatID = chatID;
        this.body = body;
        return this;
    }

    VDBMessage setID(String id){
        this.id = id;
        return this;
    }
    String getID(){ return id; }

    VDBMessage setOwnerID(String ownerID){
        this.ownerID = ownerID;
        if(creatorID == null){
            creatorID = ownerID;
        }else{
            forwarded = true;
        }
        return this;
    }
    String getOwnerID(){ return ownerID; }

    String getCreatorID(){ return creatorID; }

    VDBMessage setChatID(String chatID){
        this.chatID = chatID;
        return this;
    }
    String getChatID(){ return chatID; }

    int getSentTime(){
        return sentTime;
    }

    boolean isForwarded(){
        return forwarded;
    }
    VDBMessage setForwarded(boolean forwarded){
        this.forwarded = forwarded;
        return this;
    }

    VDBMessage setBody(String body){
        this.body = body;
        return this;
    }
    String getBody(){ return body; }
}
