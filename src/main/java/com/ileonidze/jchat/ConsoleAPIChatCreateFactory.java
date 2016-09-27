package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIChatCreateFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(3,commandParts)) return null;
        String chatName = null;
        if(commandParts.length >3) chatName = commandParts[3];
        String newChatID = MethodsChat.createChat(ConsoleAPI.thisSession,commandParts[2],chatName);
        if(newChatID != null){
            console.info("Successfully created chat #"+newChatID+" with the user #"+commandParts[2]);
        }else{
            console.error("Can\'t create new chat with the user #"+commandParts[2]);
        }
        return null;
    }
}