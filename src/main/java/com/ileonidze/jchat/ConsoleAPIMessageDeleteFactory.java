package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIMessageDeleteFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(4,commandParts)) return null;
        String messageDeleteError = MethodsMessage.getDeleteError(ConsoleAPI.thisSession,commandParts[2],commandParts[3]);
        if(messageDeleteError != null){
            console.error(messageDeleteError);
        }else{
            console.info("Deleted");
        }
        return null;
    }
}