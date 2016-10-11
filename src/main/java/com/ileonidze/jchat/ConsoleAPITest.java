package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPITest extends ConsoleAPIEvalProto {
    private static final Logger console = Logger.getLogger(ConsoleAPI.class);
    public ConsoleResponse proceed(String[] commandParts) {
        console.info("Running selftest...");
        return null;
    }
}