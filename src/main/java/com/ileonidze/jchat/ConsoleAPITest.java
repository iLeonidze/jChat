package com.ileonidze.jchat;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

class ConsoleAPITest extends ConsoleAPIEvalProto {
    private String alphabet = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    private static final Logger console = Logger.getLogger(ConsoleAPI.class);

    private static String generateString(Random rng, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }
    public ConsoleResponse proceed(String[] commandParts) {
        console.info("Running selftest...");
        int testsPassed = 0, totalTests = 0;
        long timestampStart = new Date().getTime();

        VDB.users = new ArrayList<>();
        VDB.chats = new ArrayList<>();
        VDB.sessions = new ArrayList<>();

        ConsoleAPI.silentMode = true;

        /* AUTHED [1] */
        totalTests++;
        boolean test1 = false;
        ConsoleResponse testResult1 = new ConsoleAPIUserAuthed().proceed(null);
        if (testResult1.getCode().equals("info") && testResult1.getMessage().equals("No")) {
            testsPassed++;
            test1 = true;
        }
        console.info("user.authed[1]: " + (test1 ? "PASSED" : "FAILED") + ", state: " + testResult1.getCode() + ", message: " + testResult1.getMessage());


        /* REGISTER [1] (Empty fields) */
        totalTests++;
        boolean test2 = false;
        String[] testCredentials2 = {null, null};
        ConsoleResponse testResult2 = new ConsoleAPIUserRegister().proceed(testCredentials2);
        if (testResult2.getCode().equals("error") && testResult2.getMessage().equals("Internal error")) {
            testsPassed++;
            test2 = true;
        }
        console.info("user.register[1]: " + (test2 ? "PASSED" : "FAILED") + ", state: " + testResult2.getCode() + ", message: " + testResult2.getMessage());


        /* REGISTER [2] */
        totalTests++;
        boolean test3 = false;
        String userName1 = generateString(new Random(), alphabet, 8);
        String userPassword1 = generateString(new Random(), alphabet, 9);
        String[] testCredentials3 = {null, //0
                null, //1
                userName1, //2 login
                userPassword1, //3 password
                generateString(new Random(), alphabet, 5) + " " + generateString(new Random(), alphabet, 5), //4 name
                generateString(new Random(), alphabet, 5) + "@" + generateString(new Random(), alphabet, 4) + ".ru", // 5 email
                "male"}; // 6 gender
        ConsoleResponse testResult3 = new ConsoleAPIUserRegister().proceed(testCredentials3);
        if (testResult3.getCode().equals("info") && testResult3.getMessage().equals("User @" + userName1 + " have been successfully registered!")) {
            testsPassed++;
            test3 = true;
        }
        console.info("user.register[2]: " + (test3 ? "PASSED" : "FAILED") + ", state: " + testResult3.getCode() + ", message: " + testResult3.getMessage());


        /* REGISTER [3] */
        totalTests++;
        boolean test4 = false;
        ConsoleResponse testResult4 = new ConsoleAPIUserRegister().proceed(testCredentials3);
        if (testResult4.getCode().equals("warn") && testResult4.getMessage().equals("An error occurred: User already exists")) {
            testsPassed++;
            test4 = true;
        }
        console.info("user.register[3]: " + (test4 ? "PASSED" : "FAILED") + ", state: " + testResult4.getCode() + ", message: " + testResult4.getMessage());


        /* LOGIN [1] */
        totalTests++;
        boolean test5 = false;
        String[] testCredentials5 = {null, //0
                null, //1
                "a", //2 login
                "b", //3 password
        };
        ConsoleResponse testResult5 = new ConsoleAPIUserLogin().proceed(testCredentials5);
        if (testResult5.getCode().equals("warn") && testResult5.getMessage().equals("Wrong login or password.")) {
            testsPassed++;
            test5 = true;
        }
        console.info("user.login[1]: " + (test5 ? "PASSED" : "FAILED") + ", state: " + testResult5.getCode() + ", message: " + testResult5.getMessage());


        /* LOGIN [2] */
        totalTests++;
        boolean test6 = false;
        String[] testCredentials6 = {null, //0
                null, //1
                userName1, //2 login
                userPassword1, //3 password
        };
        ConsoleResponse testResult6 = new ConsoleAPIUserLogin().proceed(testCredentials6);
        if (testResult6.getCode().equals("info") && testResult6.getMessage().equals("Welcome, @" + userName1 + ". You have been logged in!")) {
            testsPassed++;
            test6 = true;
        }
        console.info("user.login[2]: " + (test6 ? "PASSED" : "FAILED") + ", state: " + testResult6.getCode() + ", message: " + testResult6.getMessage());


        /* AUTHED [2] */
        totalTests++;
        boolean test7 = false;
        ConsoleResponse testResult7 = new ConsoleAPIUserAuthed().proceed(null);
        if (testResult7.getCode().equals("info") && testResult7.getMessage().equals("Yes")) {
            testsPassed++;
            test7 = true;
        }
        console.info("user.authed[2]: " + (test7 ? "PASSED" : "FAILED") + ", state: " + testResult7.getCode() + ", message: " + testResult7.getMessage());


        /* REGISTER [4] */
        totalTests++;
        boolean test8 = false;
        ConsoleResponse testResult8 = new ConsoleAPIUserRegister().proceed(testCredentials3);
        if (testResult8.getCode().equals("error") && testResult8.getMessage().equals("Internal error")) {
            testsPassed++;
            test8 = true;
        }
        console.info("user.register[4]: " + (test8 ? "PASSED" : "FAILED") + ", state: " + testResult8.getCode() + ", message: " + testResult8.getMessage());


        /* INFO [1] */
        totalTests++;
        boolean test9 = false;
        String[] testCredentials9 = {null, null, userName1};
        ConsoleResponse testResult9 = new ConsoleAPIUserInfo().proceed(testCredentials9);
        String userID1 = testResult9.getCode().equals("info") ? testResult9.getMessage().split("ID: ")[1].split(" ")[0] : null;
        if (testResult9.getCode().equals("info") && userID1 != null) {
            testsPassed++;
            test9 = true;
        }
        console.info("user.info[1]: " + (test9 ? "PASSED" : "FAILED") + ", state: " + testResult9.getCode());




        console.info("");
        console.info("Test result: " + (testsPassed == totalTests ? "SUCCESS" : "FAILURE") + " (" + testsPassed + " PASSED," + (totalTests - testsPassed) + " FAILED)");

        VDB.users = new ArrayList<>();
        VDB.chats = new ArrayList<>();
        VDB.sessions = new ArrayList<>();

        ConsoleAPI.silentMode = false;

        return new ConsoleResponse("info", "Test ended, lasted " + (new Date().getTime() - timestampStart) + "ms");
    }
}