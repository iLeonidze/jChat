package com.ileonidze.jchat;

class ConsoleAPIMessageForward extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(5, commandParts))
            return new ConsoleResponse("error", "Internal error");
        String messageForwardError = MethodsMessage.getForwardError(ConsoleAPI.thisSession, commandParts[2], commandParts[3], commandParts[4]);
        if (messageForwardError != null) {
            return new ConsoleResponse("error", messageForwardError);
        } else {
            return new ConsoleResponse("info", "Forwarded");
        }
    }
}