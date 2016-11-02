package com.ileonidze.jchat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;

class MethodsUser {
    private static MD5 md5 = new MD5();
    private static Validate tests = new Validate();

    static String register(String userName, String password, String name, String email, String gender) {
        String testsResult = tests.RegistrationCredentials(userName, password, name, email, gender);
        if (testsResult != null) return testsResult;

        if (Arrays.asList(Main.executionArguments).contains("sql")) {
            try {
                PreparedStatement preparedStatementCheck = Main.connection.prepareStatement("SELECT * FROM \"users\" WHERE \"login\" = ?");
                preparedStatementCheck.setString(1, userName.toLowerCase());
                ResultSet rs = preparedStatementCheck.executeQuery();
                if (rs.next()) {
                    return "User already exists";
                } else {
                    rs.close();
                    PreparedStatement preparedStatementUpdate = Main.connection.prepareStatement("INSERT INTO \"users\" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    preparedStatementUpdate.setString(1, md5.proceed(new Date().getTime() + "" + email + userName)); // id
                    preparedStatementUpdate.setString(2, userName.toLowerCase());                              // login
                    preparedStatementUpdate.setString(3, md5.proceed(password));                 // password
                    preparedStatementUpdate.setString(4, name);                                  // name
                    preparedStatementUpdate.setString(5, email.toLowerCase());                   // email
                    preparedStatementUpdate.setInt(6, gender.equals("female") ? 2 : 1);       // gender
                    preparedStatementUpdate.setInt(7, Math.round(new Date().getTime() / 1000)); // registeredTime
                    preparedStatementUpdate.setInt(8, Math.round(new Date().getTime() / 1000)); // loggedInTime
                    preparedStatementUpdate.setInt(9, Math.round(new Date().getTime() / 1000)); // lastOnlineTime
                    preparedStatementUpdate.setString(10, "");                                    // chatsIDs
                    preparedStatementUpdate.setInt(11, 0);                                     // accessLevel
                    preparedStatementUpdate.setBoolean(12, false);                                 // bannedState
                    preparedStatementUpdate.executeUpdate();
                    Main.connection.commit();
                }
            } catch (SQLException se) {
                se.printStackTrace();
                return "Database error";
            }
        }
        if (Arrays.asList(Main.executionArguments).contains("vdb")) {
            for (VDBUser user : VDB.users) {
                if (user.getLogin().toLowerCase().equals(userName.toLowerCase()) || user.getEmail().toLowerCase().equals(email.toLowerCase())) {
                    return "User already exists";
                }
            }

            VDB.users.add(new VDBUser().init(userName, password, name, email.toLowerCase(), gender));
        }

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
