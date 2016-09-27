package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIUserBanFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(3,commandParts)||ConsoleAPI.isCaughtIncorrectAccessLevel(1)) return null;
        if(MethodsUser.setBanStateUser(ConsoleAPI.thisSession,commandParts[2],true)){
            console.info("User #"+commandParts[2]+" banned successfully.");
        }else{
            console.error("Can\'t ban user. Check session token or userID.");
        }
        return null;
    }
}