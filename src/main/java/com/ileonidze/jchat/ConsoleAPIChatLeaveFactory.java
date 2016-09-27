package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIChatLeaveFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(4,commandParts)) return null;
        VDBUser currentSessionUserObject = MethodsUser.getSessionUser(ConsoleAPI.thisSession);
        String userToKick = "";
        if(currentSessionUserObject != null) userToKick = currentSessionUserObject.getID();
        if(commandParts.length>3) userToKick = commandParts[3];
        if(MethodsChat.modifyChatMember(ConsoleAPI.thisSession,commandParts[2],userToKick,"kick")){
            if(commandParts.length>3) {
                console.info("Successfully leaved user #" + userToKick + " from chat #" + commandParts[2]);
            }else{
                console.info("You successfully leaved from chat #"+commandParts[2]);
            }
        }else{
            console.error("Can\'t leave user #"+commandParts[3]+" from chat #"+commandParts[2]);
        }
        return null;
    }
}