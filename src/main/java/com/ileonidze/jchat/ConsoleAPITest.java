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
        String userID1 = testResult9.getCode().equals("info") ? testResult9.getMessage().split("ID: ")[1].split("\n")[0] : null;
        if (testResult9.getCode().equals("info") && userID1 != null) {
            testsPassed++;
            test9 = true;
        }
        console.info("user.info[1]: " + (test9 ? "PASSED" : "FAILED") + ", state: " + testResult9.getCode() + ", message: " + testResult9.getMessage());


        /* LOGOUT [1] */
        totalTests++;
        boolean test10 = false;
        String[] testCredentials10 = {null};
        ConsoleResponse testResult10 = new ConsoleAPIUserLogout().proceed(testCredentials10);
        if (testResult10.getCode().equals("info") && testResult10.getMessage().equals("You have been successfully deauthed!")) {
            testsPassed++;
            test10 = true;
        }
        console.info("user.logout[1]: " + (test10 ? "PASSED" : "FAILED") + ", state: " + testResult10.getCode() + ", message: " + testResult10.getMessage());


        /* AUTHED [3] */
        totalTests++;
        boolean test11 = false;
        ConsoleResponse testResult11 = new ConsoleAPIUserAuthed().proceed(null);
        if (testResult11.getCode().equals("info") && testResult11.getMessage().equals("No")) {
            testsPassed++;
            test11 = true;
        }
        console.info("user.authed[3]: " + (test11 ? "PASSED" : "FAILED") + ", state: " + testResult11.getCode() + ", message: " + testResult11.getMessage());


        /* REGISTER [5] */
        totalTests++;
        boolean test12 = false;
        String userName2 = generateString(new Random(), alphabet, 8);
        String userPassword2 = generateString(new Random(), alphabet, 9);
        String[] testCredentials12 = {null, //0
                null, //1
                userName2, //2 login
                userPassword2, //3 password
                generateString(new Random(), alphabet, 5) + " " + generateString(new Random(), alphabet, 5), //4 name
                generateString(new Random(), alphabet, 5) + "@" + generateString(new Random(), alphabet, 4) + ".ru", // 5 email
                "male"}; // 6 gender
        ConsoleResponse testResult12 = new ConsoleAPIUserRegister().proceed(testCredentials12);
        if (testResult12.getCode().equals("info") && testResult12.getMessage().equals("User @" + userName2 + " have been successfully registered!")) {
            testsPassed++;
            test12 = true;
        }
        console.info("user.register[2]: " + (test12 ? "PASSED" : "FAILED") + ", state: " + testResult12.getCode() + ", message: " + testResult12.getMessage());


        /* LOGIN [3] */
        totalTests++;
        boolean test13 = false;
        String[] testCredentials13 = {null, //0
                null, //1
                userName2, //2 login
                userPassword2, //3 password
        };
        ConsoleResponse testResult13 = new ConsoleAPIUserLogin().proceed(testCredentials13);
        if (testResult13.getCode().equals("info") && testResult13.getMessage().equals("Welcome, @" + userName2 + ". You have been logged in!")) {
            testsPassed++;
            test13 = true;
        }
        console.info("user.login[3]: " + (test13 ? "PASSED" : "FAILED") + ", state: " + testResult13.getCode() + ", message: " + testResult13.getMessage());


        /* INFO [2] */
        totalTests++;
        boolean test14 = false;
        String[] testCredentials14 = {null, null, userName2};
        ConsoleResponse testResult14 = new ConsoleAPIUserInfo().proceed(testCredentials14);
        String userID2 = testResult14.getCode().equals("info") ? testResult14.getMessage().split("ID: ")[1].split("\n")[0] : null;
        if (testResult14.getCode().equals("info") && userID2 != null) {
            testsPassed++;
            test14 = true;
        }
        console.info("user.info[2]: " + (test14 ? "PASSED" : "FAILED") + ", state: " + testResult14.getCode() + ", message: " + testResult14.getMessage());


        /* INFO [3] */
        totalTests++;
        boolean test15 = false;
        String[] testCredentials15 = {null, null, userName1};
        ConsoleResponse testResult15 = new ConsoleAPIUserInfo().proceed(testCredentials15);
        if (testResult15.getCode().equals("info") && testResult15.getMessage().split("Email: ").length == 1) {
            testsPassed++;
            test15 = true;
        }
        console.info("user.info[3]: " + (test15 ? "PASSED" : "FAILED") + ", state: " + testResult15.getCode());


        /* CHAT CREATE [1] */
        totalTests++;
        boolean test16 = false;
        String[] testCredentials16 = {null, null, userID1};
        ConsoleResponse testResult16 = new ConsoleAPIChatCreate().proceed(testCredentials16);
        String chatID1 = testResult16.getCode().equals("info") ? testResult16.getMessage().split("#")[1].split(" ")[0] : null;
        if (testResult16.getCode().equals("info") && chatID1 != null) {
            testsPassed++;
            test16 = true;
        }
        console.info("chat.create[1]: " + (test16 ? "PASSED" : "FAILED") + ", state: " + testResult16.getCode() + ", message: " + testResult16.getMessage());


        /* CHAT CREATE [2] */
        totalTests++;
        boolean test17 = false;
        String[] testCredentials17 = {null, null, userID2};
        ConsoleResponse testResult17 = new ConsoleAPIChatCreate().proceed(testCredentials17);
        String chatID2 = testResult17.getCode().equals("info") ? testResult17.getMessage().split("#")[1].split(" ")[0] : null;
        if (testResult17.getCode().equals("info") && chatID2 != null) {
            testsPassed++;
            test17 = true;
        }
        console.info("chat.create[2]: " + (test17 ? "PASSED" : "FAILED") + ", state: " + testResult17.getCode() + ", message: " + testResult17.getMessage());


        /* CHAT INFO [1] */
        totalTests++;
        boolean test18 = false;
        String[] testCredentials18 = {null, null, chatID1, generateString(new Random(), alphabet, 8)};
        ConsoleResponse testResult18 = new ConsoleAPIChatInfo().proceed(testCredentials18);
        if (testResult18.getCode().equals("info")) {
            testsPassed++;
            test18 = true;
        }
        console.info("chat.info[1]: " + (test18 ? "PASSED" : "FAILED") + ", state: " + testResult18.getCode() + ", message: " + testResult18.getMessage());


        /* CHAT INFO [2] */
        totalTests++;
        boolean test19 = false;
        String[] testCredentials19 = {null, null, chatID2, generateString(new Random(), alphabet, 8)};
        ConsoleResponse testResult19 = new ConsoleAPIChatInfo().proceed(testCredentials19);
        if (testResult19.getCode().equals("info")) {
            testsPassed++;
            test19 = true;
        }
        console.info("chat.info[2]: " + (test19 ? "PASSED" : "FAILED") + ", state: " + testResult19.getCode() + ", message: " + testResult19.getMessage());


        /* CHAT REMOVE [1] */
        totalTests++;
        boolean test20 = false;
        String[] testCredentials20 = {null, null, chatID2};
        ConsoleResponse testResult20 = new ConsoleAPIChatRemove().proceed(testCredentials20);
        if (testResult20.getCode().equals("info") && testResult20.getMessage().equals("Successfully removed chat #" + chatID2)) {
            testsPassed++;
            test20 = true;
        }
        console.info("chat.remove[1]: " + (test20 ? "PASSED" : "FAILED") + ", state: " + testResult20.getCode() + ", message: " + testResult20.getMessage());


        /* CHAT INFO [3] */
        totalTests++;
        boolean test21 = false;
        String[] testCredentials21 = {null, null, chatID2};
        ConsoleResponse testResult21 = new ConsoleAPIChatInfo().proceed(testCredentials21);
        if (testResult21.getCode().equals("warn") && testResult21.getMessage().equals("Chat #" + chatID2 + " not found")) {
            testsPassed++;
            test21 = true;
        }
        console.info("chat.info[3]: " + (test21 ? "PASSED" : "FAILED") + ", state: " + testResult21.getCode() + ", message: " + testResult21.getMessage());




        console.info("");
        console.info("Test result: " + (testsPassed == totalTests ? "SUCCESS" : "FAILURE") + " (" + testsPassed + " PASSED," + (totalTests - testsPassed) + " FAILED)");

        VDB.users = new ArrayList<>();
        VDB.chats = new ArrayList<>();
        VDB.sessions = new ArrayList<>();

        ConsoleAPI.silentMode = false;

        return new ConsoleResponse("info", "Test ended, lasted " + (new Date().getTime() - timestampStart) + "ms");
    }
}