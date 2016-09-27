package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIUserLogoutFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)) return null;
        MethodsUser.destroySession(ConsoleAPI.thisSession);
        ConsoleAPI.thisSession = null;
        console.info("You have been successfully deauthed!");
        return null;
    }
}