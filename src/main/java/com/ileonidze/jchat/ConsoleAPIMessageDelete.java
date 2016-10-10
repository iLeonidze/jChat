package com.ileonidze.jchat;

class ConsoleAPIMessageDelete extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(4, commandParts))
            return new ConsoleResponse("error", "Internal error");
        String messageDeleteError = MethodsMessage.getDeleteError(ConsoleAPI.thisSession, commandParts[2], commandParts[3]);
        if (messageDeleteError != null) {
            return new ConsoleResponse("error", messageDeleteError);
        } else {
            return new ConsoleResponse("info", "Deleted");
        }
    }
}