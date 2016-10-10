package com.ileonidze.jchat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class VDBChat implements Serializable {
    private String id;
    private String name;
    private String ownerID;
    private List<String> membersIDs = new ArrayList<String>();
    private int createdTime = Math.round(new Date().getTime() / 1000);
    private boolean isGroupStatus = false;
    private List<VDBMessage> messages = new ArrayList<>();

    VDBChat init(String name, String ownerID, String opponentUserID) {
        this.name = name;
        this.ownerID = ownerID;
        pushToMembersIDs(opponentUserID);
        this.id = MD5.proceed(Math.random() + name + ownerID + opponentUserID + new Date().getTime());
        return this;
    }

    VDBChat setID(String id) {
        this.id = id;
        return this;
    }

    String getID() {
        return id;
    }

    VDBChat setName(String name) {
        this.name = name;
        return this;
    }

    String getName() {
        return name;
    }

    VDBChat setOwnerID(String ownerID) {
        this.ownerID = ownerID;
        return this;
    }

    String getOwnerID() {
        return ownerID;
    }

    VDBChat pushToMembersIDs(String memberID) {
        if (!membersIDs.contains(memberID)) membersIDs.add(memberID);
        isGroupStatus = membersIDs.size() > 1;
        return this;
    }

    VDBChat removeFromMembersIDs(String memberID) {
        if (membersIDs.contains(memberID)) membersIDs.remove(memberID);
        isGroupStatus = membersIDs.size() > 1;
        return this;
    }

    List<String> getMembersIDs() {
        return membersIDs;
    }

    int getCreatedTime() {
        return createdTime;
    }

    boolean isGroup() {
        return this.isGroupStatus;
    }

    VDBChat pushToMessages(VDBMessage message) {
        if (!messages.contains(message)) messages.add(message);
        if (messages.size() > 2000) messages = messages.subList(Math.max(2000, 0), messages.size());
        return this;
    }

    VDBChat removeFromMessages(VDBMessage message) {
        if (messages.contains(message)) messages.remove(message);
        return this;
    }

    List<VDBMessage> getMessages(int length) {
        if (length > messages.size()) length = messages.size();
        return messages.subList(Math.max(messages.size() - length, 0), messages.size());
    }

    int getMessagesSize() {
        return messages.size();
    }

    VDBMessage getMessage(String messageID) {
        for (VDBMessage message : messages) {
            if (message.getID().equals(messageID)) {
                return message;
            }
        }
        return null;
    }

    VDBChat editMessage(String messageID, String messageBody) {
        for (VDBMessage message : messages) {
            if (message.getID().equals(messageID)) {
                messages.set(messages.indexOf(message), message.setBody(messageBody));
                return this;
            }
        }
        return this;
    }
}
