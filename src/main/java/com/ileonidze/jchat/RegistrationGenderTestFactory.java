package com.ileonidze.jchat;

class RegistrationGenderTestFactory extends RegistrationTestFactory {
    public String test(String login, String password, String name, String email, String gender){
        if(gender==null||(!gender.equals("male")&&!gender.equals("female"))) return "Incorrect gender value";
        return null;
    }
}