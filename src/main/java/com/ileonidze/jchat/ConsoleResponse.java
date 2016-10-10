package com.ileonidze.jchat;

class ConsoleResponse {
    private String message;
    private String code;

    ConsoleResponse(String code, String message) {
        init(code, message);
    }

    ConsoleResponse init(String code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    String getCode() {
        return code;
    }

    void setCode(String code) {
        this.code = code;
    }
}