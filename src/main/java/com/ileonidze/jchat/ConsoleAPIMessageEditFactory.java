package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIMessageEditFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(5,commandParts)) return null;
        String messageModifyError = MethodsMessage.getModifyError(ConsoleAPI.thisSession,commandParts[2],commandParts[3],commandParts[4]);
        if(messageModifyError != null){
            console.error(messageModifyError);
        }else{
            console.info("Modified");
        }
        return null;
    }
}