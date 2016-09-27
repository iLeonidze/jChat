package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIUserOpFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(4,commandParts)||ConsoleAPI.isCaughtIncorrectAccessLevel(2)) return null;
        if(MethodsUser.opUser(ConsoleAPI.thisSession,commandParts[2],Integer.parseInt(commandParts[3]))){
            console.info("User #"+commandParts[2]+" opped successfully to "+commandParts[3]+" level.");
        }else{
            console.error("Can\'t op user. Check session token or userID.");
        }
        return null;
    }
}