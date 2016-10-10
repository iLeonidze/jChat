package com.ileonidze.jchat;

class ConsoleAPIChatCreate extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(3, commandParts))
            return new ConsoleResponse("error", "Internal error");
        String chatName = null;
        if (commandParts.length > 3) chatName = commandParts[3];
        String newChatID = MethodsChat.createChat(ConsoleAPI.thisSession, commandParts[2], chatName);
        if (newChatID != null) {
            return new ConsoleResponse("info", "Successfully created chat #" + newChatID + " with the user #" + commandParts[2]);
        } else {
            return new ConsoleResponse("error", "Can\'t create new chat with the user #" + commandParts[2]);
        }
    }
}