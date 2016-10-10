package com.ileonidze.jchat;

class ConsoleAPIMessageEdit extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(5, commandParts))
            return new ConsoleResponse("error", "Internal error");
        String messageModifyError = MethodsMessage.getModifyError(ConsoleAPI.thisSession, commandParts[2], commandParts[3], commandParts[4]);
        if (messageModifyError != null) {
            return new ConsoleResponse("error", messageModifyError);
        } else {
            return new ConsoleResponse("info", "Modified");
        }
    }
}