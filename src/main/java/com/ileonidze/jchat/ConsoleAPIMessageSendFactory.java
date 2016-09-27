package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIMessageSendFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(4,commandParts)) return null;
        String messageSendError = MethodsMessage.getSendError(ConsoleAPI.thisSession,commandParts[2],commandParts[3]);
        if(messageSendError != null){
            console.error(messageSendError);
        }else{
            console.info("Sent");
        }
        return null;
    }
}