package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIUserModifyFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(4,commandParts)) return null;
        VDBUser currentUserData = MethodsUser.getUserByUsername(ConsoleAPI.thisSession,null);
        if(currentUserData == null){
            console.error("Your session expired. Please, sign in again.");
            return null;
        }
        if(currentUserData.getAccessLevel()>0&&commandParts.length>4){
            if(MethodsUser.modifyUser(ConsoleAPI.thisSession, commandParts[4], commandParts[2], commandParts[3])){
                console.info("User #"+commandParts[4]+" successfully modified!");
            }else{
                console.error("Can\'t modify user #"+commandParts[4]);
            }
            return null;
        }
        if(MethodsUser.modifyUser(ConsoleAPI.thisSession, currentUserData.getID(), commandParts[2], commandParts[3])){
            console.info("Your settings successfully modified!");
        }else{
            console.error("Can\'t modify your settings. Check session token and try again.");
        }
        return null;
    }
}