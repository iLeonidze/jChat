package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIUserLoginFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(false)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(4,commandParts)) return null;
        String l_login = commandParts[2];
        String l_password = commandParts[3];
        String obtainedSessionKey = MethodsUser.login(l_login,l_password);
        if(obtainedSessionKey==null){
            console.warn("Wrong login or password.");
        }else{
            ConsoleAPI.thisSession = obtainedSessionKey;
            console.info("Welcome, @"+l_login+". You have been logged in!");
        }
        return null;
    }
}