package com.ileonidze.jchat;

import java.util.Date;

class ConsoleAPIUserInfo extends ConsoleAPIEvalProto {
    public ConsoleResponse proceed(String[] commandParts) {
        if (ConsoleAPI.isCaughtIncorrectSessionState(true)) return new ConsoleResponse("error", "Internal error");
        VDBUser currentUserInfo = MethodsUser.getUserByUsername(ConsoleAPI.thisSession, null);
        VDBUser userInfo;
        if (commandParts.length == 3) {
            userInfo = MethodsUser.getUserByUsername(ConsoleAPI.thisSession, commandParts[2]);
        } else {
            userInfo = currentUserInfo;
        }
        if (userInfo == null) {
            return new ConsoleResponse("error", "Incorrect session token provided");
        } else if (userInfo.getID() == null) {
            return new ConsoleResponse("warn", "User not found");
        } else {
            String hiddenFields = "";
            if (currentUserInfo != null && (currentUserInfo.getAccessLevel() > 0 || currentUserInfo.equals(userInfo))) {
                // TODO: show email if friends
                hiddenFields = "\nEmail: " + userInfo.getEmail() +
                        "\nRegistered timestamp: " + new Date(userInfo.getRegisteredTime()) +
                        "\nLogged in timestamp: " + new Date(userInfo.getLoggedInTime());
                if (currentUserInfo.equals(userInfo)) {
                    hiddenFields += "\nCookie token: " + ConsoleAPI.thisSession;
                }
            }
            return new ConsoleResponse("info", "\nUsername: " + userInfo.getLogin() +
                    "\nID: " + userInfo.getID() +
                    "\nName: " + userInfo.getName() +
                    "\nGender: " + userInfo.getGenderStringLatin() +
                    "\nLast online: " + new Date(userInfo.getLastOnlineTime()) +
                    "\nAccess level: " + userInfo.getAccessLevel() +
                    "\nBanned: " + userInfo.getBannedState() + hiddenFields);
        }
    }
}