package com.ileonidze.jchat;

import java.util.Date;

class MethodsUser {
    private static MD5 md5 = new MD5();
    private static Validate tests = new Validate();

    static String register(String userName, String password, String name, String email, String gender) {
        String testsResult = tests.RegistrationCredentials(userName, password, name, email, gender);
        if (testsResult != null) return testsResult;
        for (VDBUser user : VDB.users) {
            if (user.getLogin().toLowerCase().equals(userName.toLowerCase()) || user.getEmail().toLowerCase().equals(email.toLowerCase())) {
                return "User already exists";
            }
        }

        VDB.users.add(new VDBUser().init(userName, password, name, email.toLowerCase(), gender));

        return null;
    }

    static String login(String userName, String password) {
        for (VDBUser user : VDB.users) {
            if (user.getLogin().toLowerCase().equals(userName.toLowerCase()) && user.isPasswordsEqual(password)) {
                user.updateLoggedInTime();
                user.updateLastOnlineTime();
                return createSession(user.getID());
            }
        }
        return null;
    }

    static private String createSession(String userID) {
        // TODO: check userID exists
        for (int i = 0; i < VDB.sessions.size(); i++) {
            VDBSession session = VDB.sessions.get(i);
            if (session.getUserID().equals(userID)) {
                VDB.sessions.set(i, session.updateUsedTimestamp());
            }
        }
        // TODO: add ip support
        String newHash = md5.proceed(userID + new Date().getTime());
        VDB.sessions.add(new VDBSession().init(newHash, "127.0.0.1", userID));
        return newHash;
    }

    static boolean destroySession(String sessionHash) {
        for (int i = 0; i < VDB.sessions.size(); i++) {
            if (VDB.sessions.get(i).getHash().equals(sessionHash)) {
                VDB.sessions.remove(i);
                return true;
            }
        }
        return false;
    }

    static boolean isSessionExists(String sessionHash) {
        for (int i = 0; i < VDB.sessions.size(); i++) {
            VDBSession session = VDB.sessions.get(i);
            if (session.getHash().equals(sessionHash)) {
                return true;
            }
        }
        return false;
    }

    static VDBUser getSessionUser(String sessionHash) {
        String currentUserID = null;
        for (int i = 0; i < VDB.sessions.size(); i++) {
            VDBSession session = VDB.sessions.get(i);
            if (session.getHash().equals(sessionHash)) {
                VDB.sessions.set(i, session.updateUsedTimestamp());
                currentUserID = session.getUserID();
            }
        }
        if (currentUserID == null) return null;
        for (int i = 0; i < VDB.users.size(); i++) {
            VDBUser user = VDB.users.get(i);
            if (user.getID().equals(currentUserID)) {
                VDB.users.set(i, user.updateLastOnlineTime());
                return user;
            }
        }
        return new VDBUser();
    }

    static VDBUser getUserByUsername(String sessionHash, String userName) {
        if (userName == null) {
            VDBUser sessionUser = getSessionUser(sessionHash);
            if (sessionUser == null || sessionUser.getID() == null) return null;
            return sessionUser;
        } else {
            if (!isSessionExists(sessionHash)) return null;
        }
        for (VDBUser user : VDB.users) {
            if (user.getLogin().toLowerCase().equals(userName.toLowerCase())) {
                return user;
            }
        }
        return new VDBUser();
    }

    static VDBUser getUserByID(String sessionHash, String userID) {
        for (VDBUser user : VDB.users) {
            if (user.getID().equals(userID)) {
                return user;
            }
        }
        return new VDBUser();
    }

    static boolean removeUser(String sessionHash, String userID) {
        // TODO: remove user from contacts & chats
        if (sessionHash == null || !isSessionExists(sessionHash)) return false;
        for (int i = 0; i < VDB.users.size(); i++) {
            if (VDB.users.get(i).getID().equals(userID)) {
                VDB.users.remove(i);
                return true;
            }
        }
        return false;
    }

    static boolean setBanStateUser(String sessionHash, String userID, boolean banState) {
        if (sessionHash == null || !isSessionExists(sessionHash)) return false;
        for (int i = 0; i < VDB.users.size(); i++) {
            VDBUser user = VDB.users.get(i);
            if (user.getID().equals(userID)) {
                VDB.users.set(i, user.setBannedState(banState));
                return true;
            }
        }
        return false;
    }

    boolean getBanStateUser(String sessionHash, String userID) {
        if (sessionHash == null || !isSessionExists(sessionHash)) return false;
        for (int i = 0; i < VDB.users.size(); i++) {
            if (VDB.users.get(i).getID().equals(userID)) {
                return VDB.users.get(i).getBannedState();
            }
        }
        return false;
    }

    static boolean opUser(String sessionHash, String userID, int accessLevel) {
        if (sessionHash == null || !isSessionExists(sessionHash)) return false;
        for (int i = 0; i < VDB.users.size(); i++) {
            VDBUser user = VDB.users.get(i);
            if (user.getID().equals(userID)) {
                VDB.users.set(i, user.setAccessLevel(accessLevel));
                return true;
            }
        }
        return false;
    }

    static boolean modifyUser(String sessionHash, String userID, String settingName, String sessionValue) {
        if (sessionHash == null || !isSessionExists(sessionHash)) return false;
        for (int i = 0; i < VDB.users.size(); i++) {
            VDBUser user = VDB.users.get(i);
            if (user.getID().equals(userID)) {
                switch (settingName) {
                    case "name":
                        VDB.users.set(i, null);
                        return true;
                    default:
                        return false;
                }
            }
        }
        return false;
    }
}
