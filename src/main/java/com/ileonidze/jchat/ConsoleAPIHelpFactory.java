package com.ileonidze.jchat;

import org.apache.log4j.Logger;

class ConsoleAPIHelpFactory extends ConsoleAPIFactory {
    private Logger console = Logger.getLogger(ConsoleAPI.class);
    public String proceed(String[] commandParts){
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
                "\n\t\tinfo [0] <chatid> - get chat brief information."+
                "\n\tmessage" +
                "\n\t\tsend [0] <chatid> <text> - send your message to selected chat."+
                "\n\t\tget [0] <chatid> <messageid> - get full info about message."+
                "\n\t\tdelete [0] <chatid> <messageid> - delete message from selected chat."+
                "\n\n\tUse \\s to specify whitespace in your parameter, \\n to set new line. Warning: \\\\s or \\\\n don\'t work!\n"
        );
        return null;
    }
    /*

    help
    exit
    user register
    user login
    user logout
    user authed
    user info
    user remove
    user ban
    user unban
    user op
    user contacts
    user chats
    user edit
    chats create
    chats remove
    chats info
    chats rename
    chats members
    chats invite
    chats leave
    chats messages
    message send
    message info
    message forward
    message delete
    message edit

     */
}