package com.ileonidze.jchat;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

/*
Packages - com.sun.eng
Classes - class ImageSprite
Interfaces - interface RasterDelegate
Methods - getBackground()
Variables - float myWidth
Constants - static final int GET_THE_CPU = 1;
 */

public class Main {
    private static ConsoleListener consoleListening;
    private static final Logger console = Logger.getLogger(Main.class);
    private static final Thread selfServiceThread = new Thread(() -> {
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                new SelfService().proceed();
            }
        },0,5000);
    });
    //LOG4J: FATAL,ERROR,WARN,INFO,DEBUG;TRACE

    public static void main(String[] args) {
        // Here jchat determine what type of run user wants
        console.info("jchat 1.0.8");
        console.debug("Startup arguments: "+Arrays.toString(args));
        if(!Arrays.asList(args).contains("noSelfService")){
            selfServiceThread.start();
        }
        if(Arrays.asList(args).contains("console")) { // here we are switching into console mode
            consoleListening = new ConsoleListener();
            consoleListening.run();
        }else if(Arrays.asList(args).contains("server")){ // here we are running server // TODO NIO
            console.warn("Server mode is not implemented yet");
        }else{
            console.error("Startup arguments is not specified. For console mode - specify \"console\" argument, otherwise for server - user \"server\" argument.");
        }
    }
}