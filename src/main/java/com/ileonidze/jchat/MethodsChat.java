package com.ileonidze.jchat;

import java.util.List;

class MethodsChat {
    static String createChat(String sessionHash, String opponentUserID, String chatName){
        if(sessionHash==null||!MethodsUser.isSessionExists(sessionHash)) return null;
        VDBUser ownerUserObject = MethodsUser.getSessionUser(sessionHash);
        VDBUser opponentUserObject = MethodsUser.getUserByID(sessionHash, opponentUserID);
        if(opponentUserObject == null || opponentUserObject.getID() == null || ownerUserObject == null || ownerUserObject.getID() == null) return null;
        VDBChat newChat = new VDBChat().init(chatName,ownerUserObject.getID(),opponentUserObject.getID());
        VDB.chats.add(newChat);
        VDB.users.set(VDB.users.indexOf(ownerUserObject),ownerUserObject.pushToChatsIDs(newChat.getID()));
        VDB.users.set(VDB.users.indexOf(opponentUserObject),opponentUserObject.pushToChatsIDs(newChat.getID()));
        return newChat.getID();
    }
    static boolean removeChat(String sessionHash, String chatID){
        if(sessionHash==null||!MethodsUser.isSessionExists(sessionHash)) return false;
        VDBUser sessionUserObject = MethodsUser.getSessionUser(sessionHash);
        if(sessionUserObject == null || sessionUserObject.getID() == null) return false;
        int chatIterator = -1;
        for(int i=0;i<VDB.chats.size();i++){
            VDBChat chat = VDB.chats.get(i);
            if(chat.getID().equals(chatID)){
                chatIterator = i;
            }
        }
        VDBChat thisChat = VDB.chats.get(chatIterator);
        if(chatIterator < 0 || thisChat == null || thisChat.getOwnerID() == null) return false;
        if(sessionUserObject.getID().equals(VDB.chats.get(chatIterator).getOwnerID()) || sessionUserObject.getAccessLevel() > 0){
            if(thisChat.getMembersIDs().size()>0){
                for(String userID: thisChat.getMembersIDs()){
                    VDBUser user = MethodsUser.getUserByID(sessionHash,userID);
                    if(user.getChatsIDs().contains(chatID)) VDB.users.set(VDB.users.indexOf(user),user.removeFromChatsIDs(chatID));
                }
            }
            VDB.chats.remove(chatIterator);
        } else return false;
        return true;
    }
    static boolean isChatExists(String chatID){
        for(VDBChat chat: VDB.chats){
            if(chat.getID().equals(chatID)){
                return true;
            }
        }
        return false;
    }
    static VDBChat getChat(String sessionHash, String chatID){
        if(sessionHash==null||!MethodsUser.isSessionExists(sessionHash)) return null;
        VDBUser sessionUserObject = MethodsUser.getSessionUser(sessionHash);
        if(sessionUserObject == null || sessionUserObject.getID() == null) return null;
        VDBChat chatObject = null;
        for(VDBChat chat: VDB.chats){
            if(chat.getID().equals(chatID)){
                chatObject = chat;
            }
        }
        if(chatObject == null || chatObject.getMembersIDs() == null || chatObject.getOwnerID() == null) return new VDBChat();
        if(sessionUserObject.getID().equals(chatObject.getOwnerID()) || sessionUserObject.getAccessLevel() > 0) return chatObject;
        List<String> chatUsers = chatObject.getMembersIDs();
        for(String userID: chatUsers){
            if(userID.equals(sessionUserObject.getID())){
                return chatObject;
            }
        }
        return new VDBChat();
    }
    static boolean modifyChatMember(String sessionHash, String chatID, String opponentUserID, String action){
        if(sessionHash==null||!MethodsUser.isSessionExists(sessionHash)) return false;
        VDBUser sessionUserObject = MethodsUser.getSessionUser(sessionHash);
        VDBUser opponentUserObject = MethodsUser.getUserByID(sessionHash, opponentUserID);
        if(opponentUserObject == null || opponentUserObject.getID() == null || sessionUserObject == null || sessionUserObject.getID() == null) return false;

        VDBChat chatObject = null;
        int chatIterator = -1;
        for(int i=0;i<VDB.chats.size();i++){
            VDBChat chat = VDB.chats.get(i);
            if(chat.getID().equals(chatID)){
                chatIterator = i;
                chatObject = chat;
            }
        }
        boolean actionGranted = false;
        if(chatIterator < 0 || chatObject.getMembersIDs() == null || chatObject.getOwnerID() == null) return false;
        if(sessionUserObject.getID().equals(chatObject.getOwnerID()) || sessionUserObject.getAccessLevel() > 0) actionGranted = true;
        if(!actionGranted){
            List<String> chatUsers = chatObject.getMembersIDs();
            for(String userID: chatUsers){
                if(userID.equals(sessionUserObject.getID())){
                    actionGranted = true;
                }
            }
        }
        if(!actionGranted) return false;
        switch (action){
            case "invite":
                if(chatObject.getOwnerID().equals(opponentUserID)) return false;
                VDB.chats.set(chatIterator, chatObject.pushToMembersIDs(opponentUserID));
                VDB.users.set(VDB.users.indexOf(opponentUserObject), opponentUserObject.pushToChatsIDs(chatID));
                break;
            case "kick":
                if(!chatObject.getOwnerID().equals(sessionUserObject.getID()) && sessionUserObject.getAccessLevel()<1) return false;
                if(chatObject.getMembersIDs().contains(opponentUserID)) VDB.chats.set(chatIterator, chatObject.removeFromMembersIDs(opponentUserID));
                if(opponentUserObject.getChatsIDs().contains(chatID)) VDB.users.set(VDB.users.indexOf(opponentUserObject), opponentUserObject.removeFromChatsIDs(chatID));
                break;
            default:
                return false;
        }
        return true;
    }
    static List<VDBMessage> getMessages(String sessionHash,String chatID){
        VDBChat selectedChat = getChat(sessionHash,chatID);
        if(selectedChat == null) return null;
        if(selectedChat.getID() == null) return null;
        return selectedChat.getMessages(20);
    }
    static String getRenameError(String thisSession,String chatID,String chatName){
        if(chatName.equals("")) return "Incorrect chat new name value";
        if(thisSession==null||!MethodsUser.isSessionExists(thisSession)) return "Session is invalid. Try to relogin.";
        VDBChat renamingChat = getChat(thisSession,chatID);
        VDBUser userWhoRename = MethodsUser.getSessionUser(thisSession);
        if(renamingChat == null) return "Chat is not exists";
        if(userWhoRename == null || userWhoRename.getID() == null) return "Current session is invalid. Try to relogin.";
        if(!renamingChat.getOwnerID().equals(userWhoRename.getID())&&userWhoRename.getAccessLevel()<1) return "Access denied";
        int chatIterator = -1;
        for(int i=0;i<VDB.chats.size();i++){
            VDBChat chat = VDB.chats.get(i);
            if(chat.getID().equals(chatID)){
                chatIterator = i;
            }
        }
        if(chatIterator < 0) return "Chat is not exists";
        VDB.chats.set(chatIterator,renamingChat.setName(chatName));
        return null;
    }
}