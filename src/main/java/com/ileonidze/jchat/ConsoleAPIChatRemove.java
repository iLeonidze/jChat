package com.ileonidze.jchat;

class ConsoleAPIChatRemove extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(3, commandParts))
            return new ConsoleResponse("error", "Internal error");
        if (MethodsChat.removeChat(ConsoleAPI.thisSession, commandParts[2])) {
            return new ConsoleResponse("info", "Successfully removed chat #" + commandParts[2]);
        } else {
            return new ConsoleResponse("error", "Can\'t remove chat #" + commandParts[2]);
        }
    }
}