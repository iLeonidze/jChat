package com.ileonidze.jchat;

class ConsoleAPIUserBan extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(3, commandParts) || ConsoleAPI.isCaughtIncorrectAccessLevel(1))
            return new ConsoleResponse("error", "Internal error");
        if (MethodsUser.setBanStateUser(ConsoleAPI.thisSession, commandParts[2], true)) {
            return new ConsoleResponse("info", "User #" + commandParts[2] + " banned successfully.");
        } else {
            return new ConsoleResponse("error", "Can\'t ban user. Check session token or userID.");
        }
    }
}