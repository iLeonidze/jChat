package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIUserRegisterFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(false)||ConsoleAPI.isCaughtIncorrectCommandPartsLength(7,commandParts)) return null;
        String login = commandParts[2];
        String password = commandParts[3];
        String name = commandParts[4];
        String email = commandParts[5];
        String gender = commandParts[6];
        String registrationResult = MethodsUser.register(login,password,name,email,gender);
        if(registrationResult==null){
            console.info("User @"+login+" have been successfully registered!");
        }else{
            console.warn("An error occurred: "+registrationResult);
        }
        return null;
    }
}