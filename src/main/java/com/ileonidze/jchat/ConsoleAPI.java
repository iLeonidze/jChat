package com.ileonidze.jchat;

import org.apache.log4j.Logger;

import java.util.*;

class ConsoleAPI {
    private static final Logger console = Logger.getLogger(ConsoleAPI.class);
    private static MethodsUser user = new MethodsUser();
    private static String thisSession = null;


    private static final List<ConsoleAPIFactoriesListProto> consoleAPIMethodsList = new ArrayList<ConsoleAPIFactoriesListProto>();
    static {
        consoleAPIMethodsList.add(new ConsoleAPIFactoriesListProto(null, "exit", new ConsoleAPIExitFactory()));
    }


    private boolean isCatchedIncorrectSessionState(boolean authRequired){
        if(thisSession!=null&&!authRequired){
            console.error("To proceed this action you have to sign out and try again.");
            return true;
        }else if(thisSession==null&&authRequired){
            console.error("To proceed this action you have to be logged in. Fix this issue and try again.");
            return true;
        }
        return false;
    }
    private boolean isCatchedIncorrectCommandPartsLength(int requiredLength,String[] array){
        if(array.length<requiredLength){
            console.error("Some fields are missing. To get more info use help.");
            return true;
        }
        return false;
    }
    private boolean isCatchedIncorrectAccessLevel(int requiredAccessLevel){
        if(user.getUserByUsername(thisSession,null).getAccessLevel()<requiredAccessLevel){
            console.error("Access denied");
            return true;
        }
        return false;
    }

    void parce(String command){
        String[] commandParts = command.split("\\s");
        for (int i =0; i < commandParts.length; i++){
            commandParts[i] = commandParts[i].replace("\\s", " ").replace("\\n", "\n");
        }
        switch (commandParts[0].toLowerCase()){

            case "user":
                if(commandParts.length<2){
                    console.warn("No method specified. Type help to get more info.");
                    break;
                }
                switch (commandParts[1].toLowerCase()){

                    case "register":
                        // TODO: if admin - grant permissions
                        if(isCatchedIncorrectSessionState(false)||isCatchedIncorrectCommandPartsLength(7,commandParts)) break;
                        String login = commandParts[2];
                        String password = commandParts[3];
                        String name = commandParts[4];
                        String email = commandParts[5];
                        String gender = commandParts[6];
                        String registrationResult = user.register(login,password,name,email,gender);
                        if(registrationResult==null){
                            console.info("User @"+login+" have been successfully registered!");
                        }else{
                            console.warn("An error occurred: "+registrationResult);
                        }
                        break;

                    case "login":
                        // TODO: if admin - switch session
                        if(isCatchedIncorrectSessionState(false)||isCatchedIncorrectCommandPartsLength(4,commandParts)) break;
                        String l_login = commandParts[2];
                        String l_password = commandParts[3];
                        String obtainedSessionKey = user.login(l_login,l_password);
                        if(obtainedSessionKey==null){
                            console.warn("Wrong login or password.");
                        }else{
                            thisSession = obtainedSessionKey;
                            console.info("Welcome, @"+l_login+". You have been logged in!");
                        }
                        break;

                    case "logout":
                        // TODO: if admin - remove all from specified user
                        if(isCatchedIncorrectSessionState(true)) break;
                        user.destroySession(thisSession);
                        thisSession = null;
                        console.info("You have been successfully deauthed!");
                        break;

                    case "authed":
                        console.info(thisSession!=null&&user.isSessionExists(thisSession));
                        break;

                    case "info":
                        if(isCatchedIncorrectSessionState(true)) break;
                        VDBUser currentUserInfo = user.getUserByUsername(thisSession,null);
                        VDBUser userInfo;
                        if(commandParts.length==3){
                            userInfo = user.getUserByUsername(thisSession,commandParts[2]);
                        }else{
                            userInfo = currentUserInfo;
                        }
                        if(userInfo==null){
                            console.error("Incorrect session token provided");
                        }else if(userInfo.getID()==null){
                            console.warn("User not found");
                        }else{
                            String hiddenFields = "";
                            if(currentUserInfo.getAccessLevel()>0||currentUserInfo.equals(userInfo)){
                                // TODO: show email if friends
                                hiddenFields = "\nEmail: "+userInfo.getEmail()+
                                        "\nRegistered timestamp: "+new Date(userInfo.getRegisteredTime())+
                                        "\nLogged in timestamp: "+new Date(userInfo.getLoggedInTime());
                                if(currentUserInfo.equals(userInfo)){
                                    hiddenFields += "\nCookie token: "+thisSession;
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
                        break;

                    case "modify":
                        if(isCatchedIncorrectSessionState(true)||isCatchedIncorrectCommandPartsLength(4,commandParts)) break;

                        VDBUser currentUserData = user.getUserByUsername(thisSession,null);
                        if(currentUserData.getAccessLevel()>0&&commandParts.length>4){
                            if(user.modifyUser(thisSession, commandParts[4], commandParts[2], commandParts[3])){
                                console.info("User #"+commandParts[4]+" successfully modified!");
                            }else{
                                console.error("Can\'t modify user #"+commandParts[4]);
                            }
                            break;
                        }

                        if(user.modifyUser(thisSession, currentUserData.getID(), commandParts[2], commandParts[3])){
                            console.info("Your settings successfully modified!");
                        }else{
                            console.error("Can\'t modify your settings. Check session token and try again.");
                        }

                        break;

                    case "remove":
                        if(isCatchedIncorrectSessionState(true)||isCatchedIncorrectCommandPartsLength(3,commandParts)||isCatchedIncorrectAccessLevel(2)) break;
                        if(user.removeUser(thisSession,commandParts[2])){
                            console.info("User #"+commandParts[2]+" successfully removed!");
                        }else{
                            console.error("Can\'t remove user. Check session token or userID.");
                        }
                        break;

                    case "ban":
                        if(isCatchedIncorrectSessionState(true)||isCatchedIncorrectCommandPartsLength(3,commandParts)||isCatchedIncorrectAccessLevel(1)) break;
                        if(user.setBanStateUser(thisSession,commandParts[2],true)){
                            console.info("User #"+commandParts[2]+" banned successfully.");
                        }else{
                            console.error("Can\'t ban user. Check session token or userID.");
                        }
                        break;

                    case "unban":
                        if(isCatchedIncorrectSessionState(true)||isCatchedIncorrectCommandPartsLength(3,commandParts)||isCatchedIncorrectAccessLevel(1)) break;
                        if(user.setBanStateUser(thisSession,commandParts[2],false)){
                            console.info("User #"+commandParts[2]+" unbanned successfully.");
                        }else{
                            console.error("Can\'t unban user. Check session token or userID.");
                        }
                        break;

                    case "isbanned":
                        if(isCatchedIncorrectSessionState(true)||isCatchedIncorrectCommandPartsLength(3,commandParts)||isCatchedIncorrectAccessLevel(0)) break;
                        console.info(user.getBanStateUser(thisSession,commandParts[2]));
                        break;

                    case "op":
                        if(isCatchedIncorrectSessionState(true)||isCatchedIncorrectCommandPartsLength(4,commandParts)||isCatchedIncorrectAccessLevel(2)) break;
                        if(user.opUser(thisSession,commandParts[2],Integer.parseInt(commandParts[3]))){
                            console.info("User #"+commandParts[2]+" opped successfully to "+commandParts[3]+" level.");
                        }else{
                            console.error("Can\'t op user. Check session token or userID.");
                        }
                        break;

                    case "chats":
                        if(isCatchedIncorrectSessionState(true)||isCatchedIncorrectCommandPartsLength(2,commandParts)) break;
                        VDBUser currentSessionUser = MethodsUser.getSessionUser(thisSession);
                        if(currentSessionUser != null) {
                            List<String> currentUserChatsList = currentSessionUser.getChatsIDs();
                            if(currentUserChatsList.size()>0) {
                                String printedChatsList = "";
                                for(String chatID: currentUserChatsList){
                                    VDBChat chatObject = MethodsChat.getChat(thisSession,chatID);
                                    if(chatObject != null && chatObject.getID() != null){
                                        String chatName = "???";
                                        if(chatObject.isGroup()){
                                            if(chatObject.getName() != null){
                                                chatName = chatObject.getName();
                                            }else{
                                                chatName = chatObject.getMembersIDs().size()+" users";
                                            }
                                        }else{
                                            VDBUser ownerChatUser = MethodsUser.getUserByID(thisSession,chatObject.getOwnerID());
                                            String opponentChatUserID = chatObject.getMembersIDs().get(0);
                                            VDBUser opponentChatUserObject = new VDBUser();
                                            if(opponentChatUserID!=null) opponentChatUserObject = MethodsUser.getUserByID(thisSession,opponentChatUserID);
                                            if(ownerChatUser != null && ownerChatUser.getID() != null && opponentChatUserObject.getID() != null)
                                            chatName = currentSessionUser.getID().equals(chatObject.getOwnerID()) ? opponentChatUserObject.getName() : ownerChatUser.getName();
                                        }
                                        printedChatsList += "\n#"+chatID+" ("+(chatObject.isGroup() ? "group" : "private")+") "+chatName;
                                    }
                                }
                                if(!printedChatsList.equals("")){
                                    console.info(printedChatsList);
                                }else{
                                    console.info("You don\'t have any chats");
                                }
                            }else{
                                console.info("You don\'t have any chats");
                            }
                        }else{
                            console.error("Can\'t get current user data. Try to relogin.");
                        }

                        break;

                    default:
                        console.warn("Unknown method specified. Type help to get more info.");
                }
                break;

            case "chat":
                if(commandParts.length<2){
                    console.warn("No method specified. Type help to get more info.");
                    break;
                }
                switch (commandParts[1].toLowerCase()){
                    case "create":
                        if(isCatchedIncorrectSessionState(true)||isCatchedIncorrectCommandPartsLength(3,commandParts)) break;
                        String chatName = null;
                        if(commandParts.length >3) chatName = commandParts[3];
                        String newChatID = MethodsChat.createChat(thisSession,commandParts[2],chatName);
                        if(newChatID != null){
                            console.info("Successfully created chat #"+newChatID+" with the user #"+commandParts[2]);
                        }else{
                            console.error("Can\'t create new chat with the user #"+commandParts[2]);
                        }
                        break;

                    case "info":
                        if(isCatchedIncorrectSessionState(true)||isCatchedIncorrectCommandPartsLength(3,commandParts)) break;
                        VDBChat chatInfo = MethodsChat.getChat(thisSession,commandParts[2]);
                        if(chatInfo != null && chatInfo.getID() != null){
                            String ownerUsername = MethodsUser.getUserByID(thisSession, chatInfo.getOwnerID()).getName();
                            console.info(""+
                                    "\nID: "+chatInfo.getID()+
                                    "\nName: "+(chatInfo.getName() == null ? "-" : chatInfo.getName())+
                                    "\nOwner ID: "+chatInfo.getOwnerID()+
                                    "\nOwner Username: "+(ownerUsername != null ? ownerUsername : "???")+
                                    "\nCreated Date: "+new Date(chatInfo.getCreatedTime())+
                                    "\nTotal messages: "+chatInfo.getMessagesSize()+
                                    "\nTotal members: "+chatInfo.getMembersIDs().size()+
                                    "\nChat status: "+(chatInfo.isGroup() ? "group" : "private")+" chat"+
                            "");
                        }else if(chatInfo != null){
                            console.warn("Chat #"+commandParts[2]+" not found");
                        }else{
                            console.error("Can\'t get info about chat #"+commandParts[2]);
                        }
                        break;

                    case "members":
                        if(isCatchedIncorrectSessionState(true)||isCatchedIncorrectCommandPartsLength(3,commandParts)) break;
                        List<String> chatMembersIDsList = MethodsChat.getChat(thisSession,commandParts[2]).getMembersIDs();
                        if(chatMembersIDsList != null  && chatMembersIDsList.size() > 0){
                            String printedUsersList = "";
                            for(String userID: chatMembersIDsList){
                                VDBUser userObject = MethodsUser.getUserByID(thisSession,userID);
                                if(userObject != null && userObject.getID() != null) printedUsersList += "\n#"+userID+" @"+userObject.getLogin()+" - "+userObject.getName();
                            }
                            if(printedUsersList.equals("")){
                                console.warn("Chat #"+commandParts[2]+" is empty");
                            }else{
                                console.info(printedUsersList);
                            }
                        }else if(chatMembersIDsList != null){
                            console.warn("Chat #"+commandParts[2]+" is empty");
                        }else{
                            console.error("Can\'t get members list from the chat #"+commandParts[2]);
                        }
                        break;

                    case "invite":
                        if(isCatchedIncorrectSessionState(true)||isCatchedIncorrectCommandPartsLength(4,commandParts)) break;
                        if(MethodsChat.modifyChatMember(thisSession,commandParts[2],commandParts[3],"invite")){
                            console.info("Successfully invited user #"+commandParts[3]+" to chat #"+commandParts[2]);
                        }else{
                            console.error("Can\'t invite user #"+commandParts[3]+" to chat #"+commandParts[2]);
                        }
                        break;

                    case "leave":
                        if(isCatchedIncorrectSessionState(true)||isCatchedIncorrectCommandPartsLength(4,commandParts)) break;
                        VDBUser currentSessionUserObject = MethodsUser.getSessionUser(thisSession);
                        String userToKick = "";
                        if(currentSessionUserObject != null) userToKick = currentSessionUserObject.getID();
                        if(commandParts.length>3) userToKick = commandParts[3];
                        if(MethodsChat.modifyChatMember(thisSession,commandParts[2],userToKick,"kick")){
                            if(commandParts.length>3) {
                                console.info("Successfully leaved user #" + userToKick + " from chat #" + commandParts[2]);
                            }else{
                                console.info("You successfully leaved from chat #"+commandParts[2]);
                            }
                        }else{
                            console.error("Can\'t leave user #"+commandParts[3]+" from chat #"+commandParts[2]);
                        }
                        break;

                    case "remove":
                        if(isCatchedIncorrectSessionState(true)||isCatchedIncorrectCommandPartsLength(3,commandParts)) break;
                        if(MethodsChat.removeChat(thisSession,commandParts[2])){
                            console.info("Successfully removed chat #"+commandParts[2]);
                        }else{
                            console.error("Can\'t remove chat #"+commandParts[2]);
                        }
                        break;

                    default:
                        console.warn("Unknown method specified. Type help to get more info.");
                }
                break;

            case "exit":
                console.info("Application exit");
                System.exit(0);

            case "help":
                console.info("\n\njChat Help:" +
                        "\n\thelp [0] <> - here you are" +
                        "\n\texit [0] <> - stop this application" +
                        "\n\tuser" +
                        "\n\t\tlogin [0] <username> <password> - verify credentials and give you new session" +
                        "\n\t\tregister [0] <username> <password> <fullname> <email> <gender> - create new user with specified credentials" +
                        "\n\t\tlogout - [0,1,2] <> destroy current session"+
                        "\n\t\tauthed - [0] <> true/false if session exists"+
                        "\n\t\tinfo [0,1,2] <username or nothing> - return some other user info or current user if no username specified"+
                        "\n\t\tedit [0,1,2] <settings name> <settings value> <nothing or userid> - update some current user settings. If moderator or admin - change other user data"+
                        "\n\t\tremove [2] <userid> - remove user from database"+
                        "\n\t\tban [1] <userid> - suspend user account"+
                        "\n\t\tunban [1] <userid> - unlock user account"+
                        "\n\t\top [2] <userid> <level> - grant user permissions: 0 - user, 1 - moderator, 2 - admin"+
                        "\n\t\tisbanned [0] <userid> - return user banned state"+
                        "\n\tchat" +
                        "\n\t\tcreate [0] <userid> <name> - create new chat with specified user and name it. Skip name if you want."+
                        "\n\t\tinfo [0] <chatid> - get chat brief information"+
                        "\n\n\tUse \\s to specify whitespace in your parameter, \\n to set new line. Warning: \\\\s or \\\\n don\'t work!\n"
                );
                break;

            default:
                console.warn("Unknown method specified. Type help to get more info.");
                break;
        }
    }
}