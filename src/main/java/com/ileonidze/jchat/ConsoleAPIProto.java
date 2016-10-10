package com.ileonidze.jchat;

class ConsoleAPIProto {
    public String section, key;
    public ConsoleAPIEvalProto value;

    ConsoleAPIProto(String section, String key, ConsoleAPIEvalProto value) {
        this.section = section;
        this.key = key;
        this.value = value;
    }
}