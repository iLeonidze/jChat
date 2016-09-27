package com.ileonidze.jchat;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

class ConsoleAPIChatMessagesFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(3,commandParts)) return null;
        List<VDBMessage> messagesList = MethodsChat.getMessages(ConsoleAPI.thisSession,commandParts[2]);
        if(messagesList != null){
            String consoleMessagesOutput = "";
            if(messagesList.size()>0){
                for(VDBMessage message: messagesList){
                    VDBUser messageSender = MethodsUser.getUserByID(ConsoleAPI.thisSession,message.getOwnerID());
                    if(messageSender != null && messageSender.getName() != null) consoleMessagesOutput += "\n"+message.getID()+" "+message.getOwnerID()+" "+messageSender.getName()+" ("+new Date(message.getSentTime())+"): "+message.getBody();
                }
                console.info(consoleMessagesOutput);
            }else{
                console.info("No messages");
            }
        }else{
            console.error("Can\'t load messages from chat #"+commandParts[2]);
        }
        return null;
    }
}