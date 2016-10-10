package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIExit extends ConsoleAPIEvalProto {
    private Logger console = Logger.getLogger(ConsoleAPI.class);

    public ConsoleResponse proceed(String[] commandParts) {
        console.info("Application exit");
        System.exit(0);
        return new ConsoleResponse("info", "Ended");
    }
}