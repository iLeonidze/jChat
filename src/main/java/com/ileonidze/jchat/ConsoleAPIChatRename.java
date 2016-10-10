package com.ileonidze.jchat;

class ConsoleAPIChatRename extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(4, commandParts))
            return new ConsoleResponse("error", "Internal error");
        String renameError = MethodsChat.getRenameError(ConsoleAPI.thisSession, commandParts[2], commandParts[3]);
        if (renameError == null) {
            return new ConsoleResponse("info", "Successfully renamed chat #" + commandParts[2]);
        } else {
            return new ConsoleResponse("error", renameError);
        }
    }
}