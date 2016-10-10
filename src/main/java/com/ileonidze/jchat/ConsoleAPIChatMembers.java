package com.ileonidze.jchat;

import java.util.List;

class ConsoleAPIChatMembers extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(3, commandParts))
            return new ConsoleResponse("error", "Internal error");
        List<String> chatMembersIDsList = MethodsChat.getChat(ConsoleAPI.thisSession, commandParts[2]).getMembersIDs();
        if (chatMembersIDsList != null && chatMembersIDsList.size() > 0) {
            String printedUsersList = "";
            for (String userID : chatMembersIDsList) {
                VDBUser userObject = MethodsUser.getUserByID(ConsoleAPI.thisSession, userID);
                if (userObject != null && userObject.getID() != null)
                    printedUsersList += "\n#" + userID + " @" + userObject.getLogin() + " - " + userObject.getName();
            }
            if (printedUsersList.equals("")) {
                return new ConsoleResponse("warn", "Chat #" + commandParts[2] + " is empty");
            } else {
                return new ConsoleResponse("info", printedUsersList);
            }
        } else if (chatMembersIDsList != null) {
            return new ConsoleResponse("warn", "Chat #" + commandParts[2] + " is empty");
        } else {
            return new ConsoleResponse("error", "Can\'t get members list from the chat #" + commandParts[2]);
        }
    }
}