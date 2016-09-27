package com.ileonidze.jchat;
class ConsoleAPIFactoriesListProto {
    public String section, key;
    public ConsoleAPIFactory value;
    ConsoleAPIFactoriesListProto(String section, String key, ConsoleAPIFactory value){
        this.section = section;
        this.key = key;
        this.value = value;
    }
}