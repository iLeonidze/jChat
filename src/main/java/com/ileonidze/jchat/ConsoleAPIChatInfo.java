package com.ileonidze.jchat;

import java.util.Date;

class ConsoleAPIChatInfo extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(3, commandParts))
            return new ConsoleResponse("error", "Internal error");
        VDBChat chatInfo = MethodsChat.getChat(ConsoleAPI.thisSession, commandParts[2]);
        if (chatInfo != null && chatInfo.getID() != null) {
            String ownerUsername = MethodsUser.getUserByID(ConsoleAPI.thisSession, chatInfo.getOwnerID()).getName();
            return new ConsoleResponse("info", "" +
                    "\nID: " + chatInfo.getID() +
                    "\nName: " + (chatInfo.getName() == null ? "-" : chatInfo.getName()) +
                    "\nOwner ID: " + chatInfo.getOwnerID() +
                    "\nOwner Username: " + (ownerUsername != null ? ownerUsername : "???") +
                    "\nCreated Date: " + new Date(chatInfo.getCreatedTime()) +
                    "\nTotal messages: " + chatInfo.getMessagesSize() +
                    "\nTotal members: " + chatInfo.getMembersIDs().size() +
                    "\nChat status: " + (chatInfo.isGroup() ? "group" : "private") + " chat" +
                    "");
        } else if (chatInfo != null) {
            return new ConsoleResponse("warn", "Chat #" + commandParts[2] + " not found");
        } else {
            return new ConsoleResponse("error", "Can\'t get info about chat #" + commandParts[2]);
        }
    }
}