package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIUserRemoveFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(3,commandParts)||ConsoleAPI.isCaughtIncorrectAccessLevel(2)) return null;
        if(MethodsUser.removeUser(ConsoleAPI.thisSession,commandParts[2])){
            console.info("User #"+commandParts[2]+" successfully removed!");
        }else{
            console.error("Can\'t remove user. Check session token or userID.");
        }
        return null;
    }
}