package com.ileonidze.jchat;
/**
 * Start or stop CMD listening
 * On input send command to {@link com.ileonidze.jchat.ConsoleAPI#parce Console API Parcer}
 * @autor iLeonidze
 * @since 0.1
 * @version 0.1
 * @link https://github.com/iLeonidze/jchat_console
 */

import java.util.Scanner;
import org.apache.log4j.Logger;

public class ConsoleListener {
    private static ConsoleAPI consoleAPI = new ConsoleAPI();
    private static final Logger console = Logger.getLogger(ConsoleAPI.class);
    private Scanner scanner = new Scanner(System.in);

    /**
     * Variable handle listening state
     * @see ConsoleListener#run
     * @see ConsoleListener#stop
     * @value true/false
     */
    private static boolean state = false;

    /**
     * Start listening console input and send all commands to {@link com.ileonidze.jchat.ConsoleAPI#parce Console API Parcer}
     * It exists till {@link ConsoleListener#stop stop} called or process die
     * @see com.ileonidze.jchat.ConsoleAPI
     * @return always true
     */
    public boolean run(){
        if(!state) state = true;
        do{
            console.trace("Command:");
            String command = scanner.nextLine();
            console.trace("User prompted: "+command);
            consoleAPI.parce(command);
            if(state){ // This "if" stop console in "false" case
                run();
            }
        }while(state);
        return true;
    }

    /**
     * Non-forced console listening stop.
     * @see ConsoleListener#run
     * @return if listening was stopped or not - true or false
     */
    public boolean stop(){
        if(state){
            state = false;
        }
        return !state;
    }

    /**
     * Method say listening state
     * @see ConsoleListener#run
     * @see ConsoleListener#stop
     * @return true/false
     */
    public boolean is_listening(){
        return state;
    }
}