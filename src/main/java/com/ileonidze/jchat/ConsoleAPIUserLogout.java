package com.ileonidze.jchat;

class ConsoleAPIUserLogout extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true)) return new ConsoleResponse("error", "Internal error");
        MethodsUser.destroySession(ConsoleAPI.thisSession);
        ConsoleAPI.thisSession = null;
        return new ConsoleResponse("info", "You have been successfully deauthed!");
    }
}