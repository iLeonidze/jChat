package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIChatRenameFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(4,commandParts)) return null;
        String renameError = MethodsChat.getRenameError(ConsoleAPI.thisSession,commandParts[2],commandParts[3]);
        if(renameError == null){
            console.info("Successfully renamed chat #"+commandParts[2]);
        }else{
            console.error(renameError);
        }
        return null;
    }
}