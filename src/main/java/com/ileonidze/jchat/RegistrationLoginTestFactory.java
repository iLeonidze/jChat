package com.ileonidze.jchat;

class RegistrationLoginTestFactory extends RegistrationTestFactory {
    public String test(String login, String password, String name, String email, String gender) {
        if(login==null) return "Incorrect login value";
        if(login.length() < 8) return "Too small login";
        if(login.length() > 32) return "Login is too long";
        if(login.matches("^[a-zA-Z][a-zA-Z0-9-_\\.]$")) return "Login have incorrect symbols";
        return null;
    }
}