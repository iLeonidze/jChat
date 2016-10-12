package com.ileonidze.jchat;

class ConsoleAPIUserLogin extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(false) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(4, commandParts))
            return new ConsoleResponse("error", "Internal error");
        String l_login = commandParts[2];
        String l_password = commandParts[3];
        String obtainedSessionKey = MethodsUser.login(l_login, l_password);
        if (obtainedSessionKey == null) {
            return new ConsoleResponse("warn", "Wrong login or password.");
        } else {
            ConsoleAPI.thisSession = obtainedSessionKey;
            return new ConsoleResponse("info", "Welcome, @" + l_login + ". You have been logged in!");
        }
    }
}