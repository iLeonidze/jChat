package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIUserUnbanFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(3,commandParts)||ConsoleAPI.isCaughtIncorrectAccessLevel(1)) return null;
        if(MethodsUser.setBanStateUser(ConsoleAPI.thisSession,commandParts[2],false)){
            console.info("User #"+commandParts[2]+" unbanned successfully.");
        }else{
            console.error("Can\'t unban user. Check session token or userID.");
        }
        return null;
    }
}