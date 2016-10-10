package com.ileonidze.jchat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class VDBUser implements Serializable {
    private static MD5 md5 = new MD5();
    private String id;
    private String login;
    private String password;
    private String name;
    private String email;
    private int gender; // male - 1, female - 2
    private int registeredTime = Math.round(new Date().getTime() / 1000);
    private int loggedInTime;
    private int lastOnlineTime;
    private List<String> chatsIDs = new ArrayList<String>();
    private int accessLevel = 0; // 0 - user, 1 - moderator, 2 - admin
    private boolean bannedState = false;

    VDBUser init(String login, String password, String name, String email, String gender) {
        this.login = login;
        this.password = md5.proceed(password);
        this.name = name;
        this.email = email;
        setGender(gender);
        this.id = md5.proceed(Math.random() + login + email + password + name + new Date().getTime() + gender);
        return this;
    }

    VDBUser setID(String login) {
        this.id = id;
        return this;
    }

    String getID() {
        return id;
    }

    VDBUser setLogin(String login) {
        this.login = login;
        return this;
    }

    String getLogin() {
        return login;
    }

    VDBUser setPassword(String password) {
        this.password = md5.proceed(password);
        return this;
    }

    boolean isPasswordsEqual(String password) {
        return this.password.equals(md5.proceed(password));
    }

    VDBUser setName(String name) {
        this.name = name;
        return this;
    }

    String getName() {
        return name;
    }

    VDBUser setEmail(String email) {
        this.email = email;
        return this;
    }

    String getEmail() {
        return email;
    }

    int getRegisteredTime() {
        return registeredTime;
    }

    VDBUser updateLoggedInTime() {
        this.loggedInTime = Math.round(new Date().getTime() / 1000);
        return this;
    }

    int getLoggedInTime() {
        return loggedInTime;
    }

    VDBUser updateLastOnlineTime() {
        this.lastOnlineTime = Math.round(new Date().getTime() / 1000);
        return this;
    }

    int getLastOnlineTime() {
        return lastOnlineTime;
    }

    VDBUser pushToChatsIDs(String chatID) {
        if (!chatsIDs.contains(chatID)) chatsIDs.add(chatID);
        return this;
    }

    VDBUser removeFromChatsIDs(String chatID) {
        if (chatsIDs.contains(chatID)) chatsIDs.remove(chatID);
        return this;
    }

    List<String> getChatsIDs() {
        return chatsIDs;
    }

    VDBUser setAccessLevel(int access_level) {
        this.accessLevel = access_level;
        return this;
    }

    int getAccessLevel() {
        return accessLevel;
    }

    VDBUser setBannedState(boolean banned_state) {
        this.bannedState = banned_state;
        return this;
    }

    boolean getBannedState() {
        return bannedState;
    }

    int getGenderNumeric() {
        return gender;
    }

    String getGenderStringLatin() {
        return gender == 1 ? "male" : "female";
    }

    String getGenderStringCyrrilic() {
        return gender == 1 ? "мужчина" : "женщина";
    }

    VDBUser setGender(String gender) {
        this.gender = gender.equals("male") ? 1 : 2;
        return this;
    }

    boolean isMale() {
        return gender == 1;
    }

    boolean isFemale() {
        return gender == 2;
    }
}
