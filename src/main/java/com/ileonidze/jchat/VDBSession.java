package com.ileonidze.jchat;

import java.io.Serializable;
import java.util.Date;

class VDBSession implements Serializable {
    private String hash;
    private int createdTimestamp = Math.round(new Date().getTime() / 1000);
    private int usedTimestamp = Math.round(new Date().getTime() / 1000);
    private String ip;
    private String userID;

    VDBSession init(String hash, String ip, String userID) {
        this.hash = hash;
        this.ip = ip;
        this.userID = userID;
        return this;
    }

    public VDBSession setHash(String hash) {
        this.hash = hash;
        return this;
    }

    String getHash() {
        return hash;
    }

    public int getCreatedTimestamp() {
        return createdTimestamp;
    }

    int getUsedTimestamp() {
        return usedTimestamp;
    }

    VDBSession updateUsedTimestamp() {
        usedTimestamp = Math.round(new Date().getTime() / 1000);
        return this;
    }

    public VDBSession setIP(String ip) {
        this.ip = ip;
        return this;
    }

    public String getIP() {
        return ip;
    }

    public VDBSession setUserID(String userID) {
        this.userID = userID;
        return this;
    }

    String getUserID() {
        return userID;
    }
}