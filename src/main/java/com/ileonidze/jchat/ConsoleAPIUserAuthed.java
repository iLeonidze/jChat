package com.ileonidze.jchat;

class ConsoleAPIUserAuthed extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        return new ConsoleResponse("info", ConsoleAPI.thisSession != null && MethodsUser.isSessionExists(ConsoleAPI.thisSession) ? "Yes" : "No");
    }
}