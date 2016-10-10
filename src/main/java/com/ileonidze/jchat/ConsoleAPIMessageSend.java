package com.ileonidze.jchat;

class ConsoleAPIMessageSend extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(4, commandParts))
            return new ConsoleResponse("error", "Internal error");
        String messageSendError = MethodsMessage.getSendError(ConsoleAPI.thisSession, commandParts[2], commandParts[3]);
        if (messageSendError != null) {
            return new ConsoleResponse("error", messageSendError);
        } else {
            return new ConsoleResponse("info", "Sent");
        }
    }
}