package com.ileonidze.jchat;

import java.util.Date;

class ConsoleAPIMessageInfo extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(4, commandParts))
            return new ConsoleResponse("error", "Internal error");
        VDBMessage messageObject = MethodsMessage.getMessageInfo(ConsoleAPI.thisSession, commandParts[2], commandParts[3]);
        if (messageObject != null) {
            return new ConsoleResponse("info", "" +
                    "\nMessage ID: " + messageObject.getID() +
                    "\nChat ID: " + messageObject.getChatID() +
                    "\nOwner user ID: " + messageObject.getOwnerID() +
                    "\nCreator user ID: " + messageObject.getCreatorID() +
                    "\nMessage body: " + messageObject.getBody() +
                    "\nForwarded: " + messageObject.isForwarded() +
                    "\nSent time: " + new Date(messageObject.getSentTime()) +
                    "");
        } else {
            return new ConsoleResponse("error", "Can\'t get message #" + commandParts[2]);
        }
    }
}