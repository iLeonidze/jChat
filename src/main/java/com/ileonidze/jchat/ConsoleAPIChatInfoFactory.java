package com.ileonidze.jchat;

import org.apache.log4j.Logger;

import java.util.Date;

class ConsoleAPIChatInfoFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(3,commandParts)) return null;
        VDBChat chatInfo = MethodsChat.getChat(ConsoleAPI.thisSession,commandParts[2]);
        if(chatInfo != null && chatInfo.getID() != null){
            String ownerUsername = MethodsUser.getUserByID(ConsoleAPI.thisSession, chatInfo.getOwnerID()).getName();
            console.info(""+
                    "\nID: "+chatInfo.getID()+
                    "\nName: "+(chatInfo.getName() == null ? "-" : chatInfo.getName())+
                    "\nOwner ID: "+chatInfo.getOwnerID()+
                    "\nOwner Username: "+(ownerUsername != null ? ownerUsername : "???")+
                    "\nCreated Date: "+new Date(chatInfo.getCreatedTime())+
                    "\nTotal messages: "+chatInfo.getMessagesSize()+
                    "\nTotal members: "+chatInfo.getMembersIDs().size()+
                    "\nChat status: "+(chatInfo.isGroup() ? "group" : "private")+" chat"+
                    "");
        }else if(chatInfo != null){
            console.warn("Chat #"+commandParts[2]+" not found");
        }else{
            console.error("Can\'t get info about chat #"+commandParts[2]);
        }
        return null;
    }
}