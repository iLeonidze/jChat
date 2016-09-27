package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIChatRemoveFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(3,commandParts)) return null;
        if(MethodsChat.removeChat(ConsoleAPI.thisSession,commandParts[2])){
            console.info("Successfully removed chat #"+commandParts[2]);
        }else{
            console.error("Can\'t remove chat #"+commandParts[2]);
        }
        return null;
    }
}