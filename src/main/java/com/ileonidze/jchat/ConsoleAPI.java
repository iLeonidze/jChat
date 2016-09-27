package com.ileonidze.jchat;

import org.apache.log4j.Logger;
import java.util.*;

class ConsoleAPI {
    private static final Logger console = Logger.getLogger(ConsoleAPI.class);
    static String thisSession = null;

    private static final List<ConsoleAPIFactoriesListProto> consoleAPIMethodsList = new ArrayList<>();
    static {
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto(null,        "exit",     new ConsoleAPIExitFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto(null,        "help",     new ConsoleAPIHelpFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("user",      "register", new ConsoleAPIUserRegisterFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("user",      "login",    new ConsoleAPIUserLoginFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("user",      "logout",   new ConsoleAPIUserLogoutFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("user",      "authed",   new ConsoleAPIUserAuthedFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("user",      "info",     new ConsoleAPIUserInfoFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("user",      "modify",   new ConsoleAPIUserModifyFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("user",      "remove",   new ConsoleAPIUserRemoveFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("user",      "ban",      new ConsoleAPIUserBanFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("user",      "unban",    new ConsoleAPIUserUnbanFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("user",      "op",       new ConsoleAPIUserOpFactory()));
     /* consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("user",      "contacts", new ConsoleAPIUserContactsFactory())); */
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("user",      "chats",    new ConsoleAPIUserChatsFactory()));
     /* consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("user",      "edit",     new ConsoleAPIUserEditFactory())); */
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("chat",      "create",   new ConsoleAPIChatCreateFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("chat",      "remove",   new ConsoleAPIChatRemoveFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("chat",      "info",     new ConsoleAPIChatInfoFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("chat",      "rename",   new ConsoleAPIChatRenameFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("chat",      "members",  new ConsoleAPIChatMembersFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("chat",      "invite",   new ConsoleAPIChatInviteFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("chat",      "leave",    new ConsoleAPIChatLeaveFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("chat",      "messages", new ConsoleAPIChatMessagesFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("message",   "send",     new ConsoleAPIMessageSendFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("message",   "info",     new ConsoleAPIMessageInfoFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("message",   "forward",  new ConsoleAPIMessageForwardFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("message",   "delete",   new ConsoleAPIMessageDeleteFactory()));
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto("message",   "edit",     new ConsoleAPIMessageEditFactory()));
    }

    static boolean isCaughtIncorrectSessionState(boolean authRequired){
        if(thisSession!=null&&!authRequired){
            console.error("To proceed this action you have to sign out and try again.");
            return true;
        }else if(thisSession==null&&authRequired){
            console.error("To proceed this action you have to be logged in. Fix this issue and try again.");
            return true;
        }
        return false;
    }
    static boolean isCaughtIncorrectCommandPartsLength(int requiredLength, String[] array){
        if(array.length<requiredLength){
            console.error("Some fields are missing. To get more info use help.");
            return true;
        }
        return false;
    }
    static boolean isCaughtIncorrectAccessLevel(int requiredAccessLevel){
        VDBUser userObject = MethodsUser.getUserByUsername(thisSession,null);
        if(userObject==null||userObject.getID()==null){
            console.error("Your session expired. Try to sign in again.");
            return true;
        }
        if(userObject.getAccessLevel()<requiredAccessLevel){
            console.error("Access denied");
            return true;
        }
        return false;
    }

    void parse(String command){
        String[] commandParts = command.split("\\s");
        for (int i =0; i < commandParts.length; i++){
            commandParts[i] = commandParts[i].replace("\\s", " ").replace("\\n", "\n");
        }

        String queryPart1 = null;
        String queryPart2;
        if(commandParts.length == 1){
            queryPart2 = commandParts[0].toLowerCase();
        }else{
            queryPart1 = commandParts[0].toLowerCase();
            queryPart2 = commandParts[1].toLowerCase();
        }

        for(ConsoleAPIFactoriesListProto method: consoleAPIMethodsList){
            if(((method.section==null&&queryPart1==null&&method.key.equals(queryPart2))||(method.section!=null&&queryPart1!=null&&method.section.equals(queryPart1))&&method.key.equals(queryPart2))){
                method.value.proceed(commandParts);
                return;
            }
        }

        console.warn("Please, use help to find correct methods.");
    }
}