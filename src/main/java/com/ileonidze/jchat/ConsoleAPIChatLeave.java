package com.ileonidze.jchat;

class ConsoleAPIChatLeave extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(4, commandParts))
            return new ConsoleResponse("error", "Internal error");
        VDBUser currentSessionUserObject = MethodsUser.getSessionUser(ConsoleAPI.thisSession);
        String userToKick = "";
        if (currentSessionUserObject != null) userToKick = currentSessionUserObject.getID();
        if (commandParts.length > 3) userToKick = commandParts[3];
        if (MethodsChat.modifyChatMember(ConsoleAPI.thisSession, commandParts[2], userToKick, "kick")) {
            if (commandParts.length > 3) {
                return new ConsoleResponse("info", "Successfully leaved user #" + userToKick + " from chat #" + commandParts[2]);
            } else {
                return new ConsoleResponse("info", "You successfully leaved from chat #" + commandParts[2]);
            }
        } else {
            return new ConsoleResponse("error", "Can\'t leave user #" + commandParts[3] + " from chat #" + commandParts[2]);
        }
    }
}