package com.ileonidze.jchat;

import org.apache.log4j.Logger;

import java.util.Date;

class ConsoleAPIUserInfoFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
        if(ConsoleAPI.isCaughtIncorrectSessionState(true)) return null;
        VDBUser currentUserInfo = MethodsUser.getUserByUsername(ConsoleAPI.thisSession,null);
        VDBUser userInfo;
        if(commandParts.length==3){
            userInfo = MethodsUser.getUserByUsername(ConsoleAPI.thisSession,commandParts[2]);
        }else{
            userInfo = currentUserInfo;
        }
        if(userInfo==null){
            console.error("Incorrect session token provided");
        }else if(userInfo.getID()==null){
            console.warn("User not found");
        }else{
            String hiddenFields = "";
            if(currentUserInfo!=null&&(currentUserInfo.getAccessLevel()>0||currentUserInfo.equals(userInfo))){
                // TODO: show email if friends
                hiddenFields = "\nEmail: "+userInfo.getEmail()+
                        "\nRegistered timestamp: "+new Date(userInfo.getRegisteredTime())+
                        "\nLogged in timestamp: "+new Date(userInfo.getLoggedInTime());
                if(currentUserInfo.equals(userInfo)){
                    hiddenFields += "\nCookie token: "+ConsoleAPI.thisSession;
                }
            }
            console.info("\nUsername: "+userInfo.getLogin()+
                    "\nID: "+userInfo.getID()+
                    "\nName: "+userInfo.getName()+
                    "\nGender: "+userInfo.getGenderStringLatin()+
                    "\nLast online: "+new Date(userInfo.getLastOnlineTime())+
                    "\nAccess level: "+userInfo.getAccessLevel()+
                    "\nBanned: "+userInfo.getBannedState()+hiddenFields);
        }
        return null;
    }
}