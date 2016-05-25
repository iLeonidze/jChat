package com.ileonidze.jchat;

import org.apache.log4j.Logger;
import java.util.Scanner;

public class ConsoleMethodsSupport {
    private static final Logger console = Logger.getLogger(ConsoleMethodsSupport.class);
    private Scanner scanner = new Scanner(System.in);
    private static MethodsUser user = new MethodsUser();
    public void parce(String command){
        console.debug("Parcing input command");
        String[] commandprts = command.split(" ");
        switch (commandprts[0]) {
            case "register":
                console.info("New user registration");
                console.info("Login:");
                String login = scanner.next();
                console.info("Password:");
                String password = scanner.next();
                console.info("Name:");
                String name = scanner.next();
                console.info("Email:");
                String email = scanner.next();
                console.info("Gender:");
                String gender = scanner.next();
                if(user.register(login,password,name,email,gender)){
                    console.info("You have been successfully registered!");
                }else{
                    console.warn("Some problems occurred due registration process. Check your fields and try again.");
                }
                break;
            case "login":
                console.info("User logging");
                console.info("Login:");
                String l_login = scanner.next();
                console.info("Password:");
                String l_password = scanner.next();
                if(user.login(l_login,l_password)){
                    console.info("Welcome, you have been logged in!");
                }else{
                    console.warn("Wrong login or password.");
                }
                break;
            case "exit":
                console.info("Application exit");
                System.exit(0);
            case "help":
                console.info("HELP SECTION:\nUse command line according to this syntax: METHOD COMMAND ACTION or METHOD ACTION or ACTION.\nHere the full list: (name [type][permissions])\n\tHELP [action][user] - here you are\n\tEXIT [action][user] - exit app an close cmd\n\tUSER [method][-] - users management\n\t\tREGISTER [action][user] - registration procedure for new user\n\t\tLOGIN [action][user] - procedure for entering in the messenger");
                break;
            default:
                console.warn("Unknown method specified. Try again according to this syntax: METHOD COMMAND ACTION or METHOD ACTION or ACTION. Type help for more info.");
                break;
        }
    }
}