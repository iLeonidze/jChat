package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIMessageForwardFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(5,commandParts)) return null;
        String messageForwardError = MethodsMessage.getForwardError(ConsoleAPI.thisSession,commandParts[2],commandParts[3],commandParts[4]);
        if(messageForwardError != null){
            console.error(messageForwardError);
        }else{
            console.info("Forwarded");
        }
        return null;
    }
}