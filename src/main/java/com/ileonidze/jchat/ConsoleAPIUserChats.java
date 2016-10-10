package com.ileonidze.jchat;

import java.util.List;

class ConsoleAPIUserChats extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(2, commandParts))
            return new ConsoleResponse("error", "Internal error");
        VDBUser currentSessionUser = MethodsUser.getSessionUser(ConsoleAPI.thisSession);
        if (currentSessionUser != null) {
            List<String> currentUserChatsList = currentSessionUser.getChatsIDs();
            if (currentUserChatsList.size() > 0) {
                String printedChatsList = "";
                for (String chatID : currentUserChatsList) {
                    VDBChat chatObject = MethodsChat.getChat(ConsoleAPI.thisSession, chatID);
                    if (chatObject != null && chatObject.getID() != null) {
                        String chatName = "???";
                        if (chatObject.isGroup()) {
                            if (chatObject.getName() != null) {
                                chatName = chatObject.getName();
                            } else {
                                chatName = (chatObject.getMembersIDs().size() + 1) + " users";
                            }
                        } else {
                            VDBUser ownerChatUser = MethodsUser.getUserByID(ConsoleAPI.thisSession, chatObject.getOwnerID());
                            String opponentChatUserID = chatObject.getMembersIDs().get(0);
                            VDBUser opponentChatUserObject = new VDBUser();
                            if (opponentChatUserID != null)
                                opponentChatUserObject = MethodsUser.getUserByID(ConsoleAPI.thisSession, opponentChatUserID);
                            if (ownerChatUser != null && ownerChatUser.getID() != null && opponentChatUserObject.getID() != null)
                                chatName = currentSessionUser.getID().equals(chatObject.getOwnerID()) ? opponentChatUserObject.getName() : ownerChatUser.getName();
                        }
                        printedChatsList += "\n#" + chatID + " (" + (chatObject.isGroup() ? "group" : "private") + ") " + chatName;
                    }
                }
                if (!printedChatsList.equals("")) {
                    return new ConsoleResponse("info", printedChatsList);
                } else {
                    return new ConsoleResponse("info", "You don\'t have any chats");
                }
            } else {
                return new ConsoleResponse("info", "You don\'t have any chats");
            }
        } else {
            return new ConsoleResponse("error", "Can\\'t get current user data. Try to relogin.");
        }
    }
}