package com.ileonidze.jchat;

class ConsoleAPIUserRegister extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(false) || ConsoleAPI.isCaughtIncorrectCommandPartsLength(7, commandParts))
            return new ConsoleResponse("error", "Internal error");
        String login = commandParts[2];
        String password = commandParts[3];
        String name = commandParts[4];
        String email = commandParts[5];
        String gender = commandParts[6];
        String registrationResult = MethodsUser.register(login, password, name, email, gender);
        if (registrationResult == null) {
            return new ConsoleResponse("info", "User @" + login + " have been successfully registered!");
        } else {
            return new ConsoleResponse("warn", "An error occurred: " + registrationResult);
        }
    }
}