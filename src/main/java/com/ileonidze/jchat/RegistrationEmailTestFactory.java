package com.ileonidze.jchat;

class RegistrationEmailTestFactory extends RegistrationTestFactory {
    public String test(String login, String password, String name, String email, String gender) {
        if (email == null || !email.matches("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$"))
            return "Incorrect email value";
        if (email.length() > 40) return "Email is too long";
        return null;
    }
}