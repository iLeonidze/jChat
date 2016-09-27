package com.ileonidze.jchat;

import org.apache.log4j.Logger;

import java.util.Date;

class ConsoleAPIMessageInfoFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)|| ConsoleAPI.isCaughtIncorrectCommandPartsLength(4,commandParts)) return null;
        VDBMessage messageObject = MethodsMessage.getMessageInfo(ConsoleAPI.thisSession,commandParts[2],commandParts[3]);
        if(messageObject != null){
            console.info(""+
                    "\nMessage ID: "+messageObject.getID()+
                    "\nChat ID: "+messageObject.getChatID()+
                    "\nOwner user ID: "+messageObject.getOwnerID()+
                    "\nCreator user ID: "+messageObject.getCreatorID()+
                    "\nMessage body: "+messageObject.getBody()+
                    "\nForwarded: "+messageObject.isForwarded()+
                    "\nSent time: "+new Date(messageObject.getSentTime())+
                    "");
        }else{
            console.error("Can\'t get message #"+commandParts[2]);
        }
        return null;
    }
}