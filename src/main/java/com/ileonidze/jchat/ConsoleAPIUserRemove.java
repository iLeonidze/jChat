package com.ileonidze.jchat;

class ConsoleAPIUserRemove extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(3, commandParts) || ConsoleAPI.isCaughtIncorrectAccessLevel(2))
            return new ConsoleResponse("error", "Internal error");
        if (MethodsUser.removeUser(ConsoleAPI.thisSession, commandParts[2])) {
            return new ConsoleResponse("info", "User #" + commandParts[2] + " successfully removed!");
        } else {
            return new ConsoleResponse("error", "Can\'t remove user. Check session token or userID.");
        }
    }
}