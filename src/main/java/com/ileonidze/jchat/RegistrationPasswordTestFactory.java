package com.ileonidze.jchat;

public class RegistrationPasswordTestFactory extends RegistrationTestFactory {
    public String test(String login, String password, String name, String email, String gender) {
        if(password==null) return "Incorrect password value";
        if(password.length() < 8) return "Too small password";
        if(password.length() > 32) return "Password is too long";
        if(password.matches("^[a-zA-Z][a-zA-Z0-9-_\\.]$")) return "Password have incorrect symbols";
        return null;
    }
}