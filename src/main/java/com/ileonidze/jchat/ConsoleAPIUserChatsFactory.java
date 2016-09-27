package com.ileonidze.jchat;

import org.apache.log4j.Logger;

import java.util.List;

class ConsoleAPIUserChatsFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(2,commandParts)) return null;
        VDBUser currentSessionUser = MethodsUser.getSessionUser(ConsoleAPI.thisSession);
        if(currentSessionUser != null) {
            List<String> currentUserChatsList = currentSessionUser.getChatsIDs();
            if(currentUserChatsList.size()>0) {
                String printedChatsList = "";
                for(String chatID: currentUserChatsList){
                    VDBChat chatObject = MethodsChat.getChat(ConsoleAPI.thisSession,chatID);
                    if(chatObject != null && chatObject.getID() != null){
                        String chatName = "???";
                        if(chatObject.isGroup()){
                            if(chatObject.getName() != null){
                                chatName = chatObject.getName();
                            }else{
                                chatName = (chatObject.getMembersIDs().size()+1)+" users";
                            }
                        }else{
                            VDBUser ownerChatUser = MethodsUser.getUserByID(ConsoleAPI.thisSession,chatObject.getOwnerID());
                            String opponentChatUserID = chatObject.getMembersIDs().get(0);
                            VDBUser opponentChatUserObject = new VDBUser();
                            if(opponentChatUserID!=null) opponentChatUserObject = MethodsUser.getUserByID(ConsoleAPI.thisSession,opponentChatUserID);
                            if(ownerChatUser != null && ownerChatUser.getID() != null && opponentChatUserObject.getID() != null)
                                chatName = currentSessionUser.getID().equals(chatObject.getOwnerID()) ? opponentChatUserObject.getName() : ownerChatUser.getName();
                        }
                        printedChatsList += "\n#"+chatID+" ("+(chatObject.isGroup() ? "group" : "private")+") "+chatName;
                    }
                }
                if(!printedChatsList.equals("")){
                    console.info(printedChatsList);
                }else{
                    console.info("You don\'t have any chats");
                }
            }else{
                console.info("You don\'t have any chats");
            }
        }else{
            console.error("Can\'t get current user data. Try to relogin.");
        }
        return null;
    }
}