package com.ileonidze.jchat;

class ConsoleAPIUserModify extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(4, commandParts))
            return new ConsoleResponse("error", "Internal error");
        VDBUser currentUserData = MethodsUser.getUserByUsername(ConsoleAPI.thisSession, null);
        if (currentUserData == null) {
            return new ConsoleResponse("error", "Your session expired. Please, sign in again.");
        }
        if (currentUserData.getAccessLevel() > 0 && commandParts.length > 4) {
            if (MethodsUser.modifyUser(ConsoleAPI.thisSession, commandParts[4], commandParts[2], commandParts[3])) {
                return new ConsoleResponse("info", "User #" + commandParts[4] + " successfully modified!");
            } else {
                return new ConsoleResponse("error", "Can\'t modify user #" + commandParts[4]);
            }
        }
        if (MethodsUser.modifyUser(ConsoleAPI.thisSession, currentUserData.getID(), commandParts[2], commandParts[3])) {
            return new ConsoleResponse("info", "Your settings successfully modified!");
        } else {
            return new ConsoleResponse("error", "Can\'t modify your settings. Check session token and try again.");
        }
    }
}