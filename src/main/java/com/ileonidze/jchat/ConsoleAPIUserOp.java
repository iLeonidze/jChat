package com.ileonidze.jchat;

class ConsoleAPIUserOp extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(4, commandParts) || ConsoleAPI.isCaughtIncorrectAccessLevel(2))
            return new ConsoleResponse("error", "Internal error");
        if (MethodsUser.opUser(ConsoleAPI.thisSession, commandParts[2], Integer.parseInt(commandParts[3]))) {
            return new ConsoleResponse("info", "User #" + commandParts[2] + " opped successfully to " + commandParts[3] + " level.");
        } else {
            return new ConsoleResponse("error", "Can\'t op user. Check session token or userID.");
        }
    }
}