package com.ileonidze.jchat;

import java.util.Scanner;
import org.apache.log4j.Logger;

public class ConsoleListener {
    private static ConsoleMethodsSupport methods = new ConsoleMethodsSupport();
    private static final Logger console = Logger.getLogger(ConsoleMethodsSupport.class);
    private Scanner scanner = new Scanner(System.in);
    private static boolean state = false;
    public boolean run(){
        if(!state) state = true;
        console.debug("Command:");
        String command = scanner.next();
        console.debug("User prompted: "+command);
        methods.parce(command.toLowerCase());
        if(state){
            run();
        }
        return true;
    }
    public boolean stop(){
        if(state){
            state = false;
        }
        return !state;
    }
    public boolean is_listening(){
        return state;
    }
}