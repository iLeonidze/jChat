package com.ileonidze.jchat;

class Validate {
    static RegistrationTestFactory RegistrationFactories[] = {
            new RegistrationLoginTestFactory(),
            new RegistrationPasswordTestFactory(),
            new RegistrationNameTestFactory(),
            new RegistrationEmailTestFactory(),
            new RegistrationGenderTestFactory()
    };

    String RegistrationCredentials(String login, String password, String name, String email, String gender) {
        for (RegistrationTestFactory factory : RegistrationFactories) {
            String verificationResult = factory.test(login, password, name, email, gender);
            if (verificationResult != null) return verificationResult;
        }
        return null;
    }
}