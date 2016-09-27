package com.ileonidze.jchat;

import org.apache.log4j.Logger;

import java.util.List;

class ConsoleAPIChatMembersFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(3,commandParts)) return null;
        List<String> chatMembersIDsList = MethodsChat.getChat(ConsoleAPI.thisSession,commandParts[2]).getMembersIDs();
        if(chatMembersIDsList != null  && chatMembersIDsList.size() > 0){
            String printedUsersList = "";
            for(String userID: chatMembersIDsList){
                VDBUser userObject = MethodsUser.getUserByID(ConsoleAPI.thisSession,userID);
                if(userObject != null && userObject.getID() != null) printedUsersList += "\n#"+userID+" @"+userObject.getLogin()+" - "+userObject.getName();
            }
            if(printedUsersList.equals("")){
                console.warn("Chat #"+commandParts[2]+" is empty");
            }else{
                console.info(printedUsersList);
            }
        }else if(chatMembersIDsList != null){
            console.warn("Chat #"+commandParts[2]+" is empty");
        }else{
            console.error("Can\'t get members list from the chat #"+commandParts[2]);
        }
        return null;
    }
}