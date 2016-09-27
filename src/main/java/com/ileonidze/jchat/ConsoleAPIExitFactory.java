package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIExitFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        console.info("Application exit");
        System.exit(0);
        return null;
    }
}