package com.ileonidze.jchat;

import java.util.Arrays;
import org.apache.log4j.Logger;

public class Main {
    private static ConsoleListener consoleListening;
    private static final Logger console = Logger.getLogger(Main.class);
    //LOG4J: FATAL,ERROR,WARN,INFO,DEBUG;TRACE

    public static void main(String[] args) { // <-
        console.info("jchat 1.0");
        console.debug("Startup arguments: "+Arrays.toString(args));
        if(Arrays.asList(args).contains("console")) {
            consoleListening = new ConsoleListener();
            consoleListening.run();
        }else if(Arrays.asList(args).contains("server")){
            console.warn("Server mode is not implemented yet");
        }else{
            console.error("Startup arguments is not specified. For console mode - specify \"console\" argument, otherwise for server - user \"server\" argument.");
        }
    }
}