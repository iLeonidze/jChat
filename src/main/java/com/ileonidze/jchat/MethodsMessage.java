package com.ileonidze.jchat;

class MethodsMessage {
    static String getSendError(String thisSession, String chatID, String body) {
        if (thisSession == null || !MethodsUser.isSessionExists(thisSession))
            return "Session is invalid. Try to relogin.";
        VDBChat chatToSend = MethodsChat.getChat(thisSession, chatID);
        VDBUser userToSendFrom = MethodsUser.getSessionUser(thisSession);
        if (chatToSend == null) return "Chat is not exists";
        if (userToSendFrom == null || userToSendFrom.getID() == null) return "Session is invalid. Try to relogin.";
        if (!chatToSend.getMembersIDs().contains(userToSendFrom.getID()) && !chatToSend.getOwnerID().equals(userToSendFrom.getID()) && userToSendFrom.getAccessLevel() < 1)
            return "Access denied";
        int chatIterator = -1;
        for (int i = 0; i < VDB.chats.size(); i++) {
            VDBChat chat = VDB.chats.get(i);
            if (chat.getID().equals(chatID)) {
                chatIterator = i;
            }
        }
        if (chatIterator < 0) return "Chat is not exists";
        VDB.chats.set(chatIterator, chatToSend.pushToMessages(new VDBMessage().init(MD5.proceed(Math.random() + " " + userToSendFrom.getID() + chatID + body), userToSendFrom.getID(), chatID, body)));
        return null;
    }

    static VDBMessage getMessageInfo(String thisSession, String chatID, String messageID) {
        if (thisSession == null || !MethodsUser.isSessionExists(thisSession)) return null;
        VDBChat chatToLoadFrom = MethodsChat.getChat(thisSession, chatID);
        VDBUser userWhoLoad = MethodsUser.getSessionUser(thisSession);
        if (chatToLoadFrom == null) return null;
        if (userWhoLoad == null || userWhoLoad.getID() == null) return null;
        if (!chatToLoadFrom.getMembersIDs().contains(userWhoLoad.getID()) && !chatToLoadFrom.getOwnerID().equals(userWhoLoad.getID()) && userWhoLoad.getAccessLevel() < 1)
            return null;
        int chatIterator = -1;
        for (int i = 0; i < VDB.chats.size(); i++) {
            VDBChat chat = VDB.chats.get(i);
            if (chat.getID().equals(chatID)) {
                chatIterator = i;
            }
        }
        if (chatIterator < 0) return null;
        VDBMessage messageToDelete = chatToLoadFrom.getMessage(messageID);
        if (messageToDelete == null) return null;
        if (!messageToDelete.getOwnerID().equals(userWhoLoad.getID()) && !chatToLoadFrom.getOwnerID().equals(userWhoLoad.getID()) && userWhoLoad.getAccessLevel() < 1)
            return null;
        return messageToDelete;
    }

    static String getDeleteError(String thisSession, String chatID, String messageID) {
        if (thisSession == null || !MethodsUser.isSessionExists(thisSession))
            return "Session is invalid. Try to relogin.";
        VDBChat chatToDeleteFrom = MethodsChat.getChat(thisSession, chatID);
        VDBUser userWhoDelete = MethodsUser.getSessionUser(thisSession);
        if (chatToDeleteFrom == null) return "Chat is not exists";
        if (userWhoDelete == null || userWhoDelete.getID() == null)
            return "Current session is invalid. Try to relogin.";
        if (!chatToDeleteFrom.getMembersIDs().contains(userWhoDelete.getID()) && !chatToDeleteFrom.getOwnerID().equals(userWhoDelete.getID()) && userWhoDelete.getAccessLevel() < 1)
            return "Access denied";
        int chatIterator = -1;
        for (int i = 0; i < VDB.chats.size(); i++) {
            VDBChat chat = VDB.chats.get(i);
            if (chat.getID().equals(chatID)) {
                chatIterator = i;
            }
        }
        if (chatIterator < 0) return "Chat is not exists";
        VDBMessage messageToDelete = chatToDeleteFrom.getMessage(messageID);
        if (messageToDelete == null) return "Message is not exists";
        if (!messageToDelete.getOwnerID().equals(userWhoDelete.getID()) && !chatToDeleteFrom.getOwnerID().equals(userWhoDelete.getID()) && userWhoDelete.getAccessLevel() < 1)
            return "Access denied";
        VDB.chats.set(chatIterator, chatToDeleteFrom.removeFromMessages(messageToDelete));
        return null;
    }

    static String getModifyError(String thisSession, String chatID, String messageID, String body) {
        if (thisSession == null || !MethodsUser.isSessionExists(thisSession))
            return "Session is invalid. Try to relogin.";
        VDBChat chatToModifyFrom = MethodsChat.getChat(thisSession, chatID);
        VDBUser userWhoModify = MethodsUser.getSessionUser(thisSession);
        if (chatToModifyFrom == null) return "Chat is not exists";
        if (userWhoModify == null || userWhoModify.getID() == null)
            return "Current session is invalid. Try to relogin.";
        if (!chatToModifyFrom.getMembersIDs().contains(userWhoModify.getID()) && !chatToModifyFrom.getOwnerID().equals(userWhoModify.getID()) && userWhoModify.getAccessLevel() < 1)
            return "Access denied";
        int chatIterator = -1;
        for (int i = 0; i < VDB.chats.size(); i++) {
            VDBChat chat = VDB.chats.get(i);
            if (chat.getID().equals(chatID)) {
                chatIterator = i;
            }
        }
        if (chatIterator < 0) return "Chat is not exists";
        VDBMessage messageToModify = chatToModifyFrom.getMessage(messageID);
        if (messageToModify == null) return "Message is not exists";
        if (!messageToModify.getOwnerID().equals(userWhoModify.getID()) && !chatToModifyFrom.getOwnerID().equals(userWhoModify.getID()) && userWhoModify.getAccessLevel() < 1)
            return "Access denied";
        VDB.chats.set(chatIterator, chatToModifyFrom.editMessage(messageID, body));
        return null;
    }

    static String getForwardError(String thisSession, String chatForwardFromID, String messageID, String chatForwardToID) {
        if (thisSession == null || !MethodsUser.isSessionExists(thisSession))
            return "Session is invalid. Try to relogin.";
        VDBChat chatForwardingFrom = MethodsChat.getChat(thisSession, chatForwardFromID);
        VDBUser userWhoForward = MethodsUser.getSessionUser(thisSession);
        if (chatForwardingFrom == null) return "Forwarding chat #" + chatForwardFromID + " is not exists";
        if (userWhoForward == null || userWhoForward.getID() == null)
            return "Current session is invalid. Try to relogin.";
        if (!chatForwardingFrom.getMembersIDs().contains(userWhoForward.getID()) && !chatForwardingFrom.getOwnerID().equals(userWhoForward.getID()) && userWhoForward.getAccessLevel() < 1)
            return "Access to chat #" + chatForwardFromID + " denied";
        VDBMessage messageToModify = chatForwardingFrom.getMessage(messageID);
        if (messageToModify == null) return "Message is not exists";

        VDBChat chatForwardTo;
        if (chatForwardFromID.equals(chatForwardToID)) {
            chatForwardTo = chatForwardingFrom;
        } else {
            chatForwardTo = MethodsChat.getChat(thisSession, chatForwardToID);
            if (chatForwardTo == null) return "Forwarding chat #" + chatForwardToID + " is not exists";
            if (!chatForwardTo.getMembersIDs().contains(userWhoForward.getID()) && !chatForwardTo.getOwnerID().equals(userWhoForward.getID()) && userWhoForward.getAccessLevel() < 1)
                return "Access to chat #" + chatForwardToID + " denied";
        }

        int chatIterator = -1;
        for (int i = 0; i < VDB.chats.size(); i++) {
            VDBChat chat = VDB.chats.get(i);
            if (chat.getID().equals(chatForwardTo.getID())) {
                chatIterator = i;
            }
        }
        if (chatIterator < 0) return "Chat #" + chatForwardTo.getID() + " is not exists";
        VDB.chats.set(chatIterator, chatForwardTo.pushToMessages(messageToModify.setOwnerID(userWhoForward.getID()).setForwarded(true)));
        return null;
    }
}
