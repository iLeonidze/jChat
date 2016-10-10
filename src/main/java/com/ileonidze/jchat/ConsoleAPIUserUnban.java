package com.ileonidze.jchat;

class ConsoleAPIUserUnban extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(3, commandParts) || ConsoleAPI.isCaughtIncorrectAccessLevel(1))
            return new ConsoleResponse("error", "Internal error");
        if (MethodsUser.setBanStateUser(ConsoleAPI.thisSession, commandParts[2], false)) {
            return new ConsoleResponse("info", "User #" + commandParts[2] + " unbanned successfully.");
        } else {
            return new ConsoleResponse("error", "Can\'t unban user. Check session token or userID.");
        }
    }
}