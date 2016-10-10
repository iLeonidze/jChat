package com.ileonidze.jchat;

import java.util.Date;
import java.util.List;

class ConsoleAPIChatMessages extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(3, commandParts))
            return new ConsoleResponse("error", "Internal error");
        List<VDBMessage> messagesList = MethodsChat.getMessages(ConsoleAPI.thisSession, commandParts[2]);
        if (messagesList != null) {
            String consoleMessagesOutput = "";
            if (messagesList.size() > 0) {
                for (VDBMessage message : messagesList) {
                    VDBUser messageSender = MethodsUser.getUserByID(ConsoleAPI.thisSession, message.getOwnerID());
                    if (messageSender != null && messageSender.getName() != null)
                        consoleMessagesOutput += "\n" + message.getID() + " " + message.getOwnerID() + " " + messageSender.getName() + " (" + new Date(message.getSentTime()) + "): " + message.getBody();
                }
                return new ConsoleResponse("info", consoleMessagesOutput);
            } else {
                return new ConsoleResponse("info", "No messages");
            }
        } else {
            return new ConsoleResponse("error", "Can\'t load messages from chat #" + commandParts[2]);
        }
    }
}