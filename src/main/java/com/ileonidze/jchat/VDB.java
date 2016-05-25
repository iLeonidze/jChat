package com.ileonidze.jchat;

import java.util.ArrayList;

public class VDB {
    private class VDBUserObject {
        public String id;
        public String login;
        public String password;
        public String name;
        public String email;
        public int gender;
        public int registered_time;
        public int loggedin_time;
        public int lastonline_time;
        public String[] chats_ids;
        public int access_level = 1;
        public boolean banned_state = false;
    }
    public static ArrayList<VDBUserObject> users = new ArrayList<>();
    private class VDBMessageObject {
        public String id;
        public String owner_id;
        public String chat_id;
        public int sent_time;
        public String body;
    }
    private class VDBChatObject {
        public String id;
        public String name;
        public String owner_id;
        public String[] users_ids;
        public int created_time;
        public boolean nonprivate = false;
        public ArrayList<VDBMessageObject> messages = new ArrayList<>();
    }
    public static ArrayList<VDBChatObject> chats = new ArrayList<>();
}
