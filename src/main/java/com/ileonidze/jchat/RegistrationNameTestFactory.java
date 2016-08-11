package com.ileonidze.jchat;

public class RegistrationNameTestFactory extends RegistrationTestFactory {
    public String test(String login, String password, String name, String email, String gender){
        if(name==null || !name.matches("^[а-яА-ЯёЁa-zA-Z ]+$")) return "Incorrect name value";
        return null;
    }
}
