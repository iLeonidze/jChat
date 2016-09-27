package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIChatInviteFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(4,commandParts)) return null;
        if(MethodsChat.modifyChatMember(ConsoleAPI.thisSession,commandParts[2],commandParts[3],"invite")){
            console.info("Successfully invited user #"+commandParts[3]+" to chat #"+commandParts[2]);
        }else{
            console.error("Can\'t invite user #"+commandParts[3]+" to chat #"+commandParts[2]);
        }
        return null;
    }
}