package com.ileonidze.jchat;

class ConsoleAPIChatInvite extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(4, commandParts))
            return new ConsoleResponse("error", "Internal error");
        if (MethodsChat.modifyChatMember(ConsoleAPI.thisSession, commandParts[2], commandParts[3], "invite")) {
            return new ConsoleResponse("info", "Successfully invited user #" + commandParts[3] + " to chat #" + commandParts[2]);
        } else {
            return new ConsoleResponse("error", "Can\'t invite user #" + commandParts[3] + " to chat #" + commandParts[2]);
        }
    }
}