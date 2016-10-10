package com.ileonidze.jchat;

import org.apache.log4j.Logger;

import java.util.*;

class ConsoleAPI {
    private static final Logger console = Logger.getLogger(ConsoleAPI.class);
    static String thisSession = null;

    private static final List<ConsoleAPIProto> consoleAPIMethods = new ArrayList<>();

    static {
        consoleAPIMethods.add(new ConsoleAPIProto(null, "exit", new ConsoleAPIExit()));
        consoleAPIMethods.add(new ConsoleAPIProto(null, "help", new ConsoleAPIHelp()));
        consoleAPIMethods.add(new ConsoleAPIProto(null, "test", new ConsoleAPITest()));
        consoleAPIMethods.add(new ConsoleAPIProto("user", "register", new ConsoleAPIUserRegister()));
        consoleAPIMethods.add(new ConsoleAPIProto("user", "login", new ConsoleAPIUserLogin()));
        consoleAPIMethods.add(new ConsoleAPIProto("user", "logout", new ConsoleAPIUserLogout()));
        consoleAPIMethods.add(new ConsoleAPIProto("user", "authed", new ConsoleAPIUserAuthed()));
        consoleAPIMethods.add(new ConsoleAPIProto("user", "info", new ConsoleAPIUserInfo()));
        consoleAPIMethods.add(new ConsoleAPIProto("user", "modify", new ConsoleAPIUserModify()));
        consoleAPIMethods.add(new ConsoleAPIProto("user", "remove", new ConsoleAPIUserRemove()));
        consoleAPIMethods.add(new ConsoleAPIProto("user", "ban", new ConsoleAPIUserBan()));
        consoleAPIMethods.add(new ConsoleAPIProto("user", "unban", new ConsoleAPIUserUnban()));
        consoleAPIMethods.add(new ConsoleAPIProto("user", "op", new ConsoleAPIUserOp()));
     /* consoleAPIMethods.add(new ConsoleAPIProto("user",      "contacts", new ConsoleAPIUserContacts())); */
        consoleAPIMethods.add(new ConsoleAPIProto("user", "chats", new ConsoleAPIUserChats()));
     /* consoleAPIMethods.add(new ConsoleAPIProto("user",      "edit",     new ConsoleAPIUserEdit())); */
        consoleAPIMethods.add(new ConsoleAPIProto("chat", "create", new ConsoleAPIChatCreate()));
        consoleAPIMethods.add(new ConsoleAPIProto("chat", "remove", new ConsoleAPIChatRemove()));
        consoleAPIMethods.add(new ConsoleAPIProto("chat", "info", new ConsoleAPIChatInfo()));
        consoleAPIMethods.add(new ConsoleAPIProto("chat", "rename", new ConsoleAPIChatRename()));
        consoleAPIMethods.add(new ConsoleAPIProto("chat", "members", new ConsoleAPIChatMembers()));
        consoleAPIMethods.add(new ConsoleAPIProto("chat", "invite", new ConsoleAPIChatInvite()));
        consoleAPIMethods.add(new ConsoleAPIProto("chat", "leave", new ConsoleAPIChatLeave()));
        consoleAPIMethods.add(new ConsoleAPIProto("chat", "messages", new ConsoleAPIChatMessages()));
        consoleAPIMethods.add(new ConsoleAPIProto("message", "send", new ConsoleAPIMessageSend()));
        consoleAPIMethods.add(new ConsoleAPIProto("message", "info", new ConsoleAPIMessageInfo()));
        consoleAPIMethods.add(new ConsoleAPIProto("message", "forward", new ConsoleAPIMessageForward()));
        consoleAPIMethods.add(new ConsoleAPIProto("message", "delete", new ConsoleAPIMessageDelete()));
        consoleAPIMethods.add(new ConsoleAPIProto("message", "edit", new ConsoleAPIMessageEdit()));
    }

    static boolean isCaughtIncorrectSessionState(boolean authRequired) {
        if (thisSession != null && !authRequired) {
            console.error("To proceed this action you have to sign out and try again.");
            return true;
        } else if (thisSession == null && authRequired) {
            console.error("To proceed this action you have to be logged in. Fix this issue and try again.");
            return true;
        }
        return false;
    }

    static boolean isCaughtIncorrectCommandPartsLength(int requiredLength, String[] array) {
        if (array.length < requiredLength) {
            console.error("Some fields are missing. To get more info use help.");
            return true;
        }
        return false;
    }

    static boolean isCaughtIncorrectAccessLevel(int requiredAccessLevel) {
        VDBUser userObject = MethodsUser.getUserByUsername(thisSession, null);
        if (userObject == null || userObject.getID() == null) {
            console.error("Your session expired. Try to sign in again.");
            return true;
        }
        if (userObject.getAccessLevel() < requiredAccessLevel) {
            console.error("Access denied");
            return true;
        }
        return false;
    }

    void parse(String command) {
        String[] commandParts = command.split("\\s");
        for (int i = 0; i < commandParts.length; i++) {
            commandParts[i] = commandParts[i].replace("\\s", " ").replace("\\n", "\n");
        }

        String queryPart1 = null;
        String queryPart2;
        if (commandParts.length == 1) {
            queryPart2 = commandParts[0].toLowerCase();
        } else {
            queryPart1 = commandParts[0].toLowerCase();
            queryPart2 = commandParts[1].toLowerCase();
        }

        for (ConsoleAPIProto method : consoleAPIMethods) {
            if (((method.section == null && queryPart1 == null && method.key.equals(queryPart2)) || (method.section != null && queryPart1 != null && method.section.equals(queryPart1)) && method.key.equals(queryPart2))) {
                ConsoleResponse response = method.value.proceed(commandParts);
                switch (response.getCode()) {
                    case "error":
                        console.error(response.getMessage());
                        break;
                    case "info":
                        console.info(response.getMessage());
                        break;
                    case "warn":
                        console.warn(response.getMessage());
                        break;
                    default:
                        console.debug(response.getMessage());
                }
                return;
            }
        }
        console.warn("Please, use help to find correct methods.");
    }
}