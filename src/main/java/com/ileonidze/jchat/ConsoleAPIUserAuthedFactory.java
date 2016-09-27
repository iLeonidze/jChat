package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIUserAuthedFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        console.info(ConsoleAPI.thisSession!=null&&MethodsUser.isSessionExists(ConsoleAPI.thisSession));
        return null;
    }
}