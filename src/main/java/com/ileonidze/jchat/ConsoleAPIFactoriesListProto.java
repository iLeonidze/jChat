package com.ileonidze.jchat;
public class ConsoleAPIFactoriesListProto {
    public String section, key;
    public ConsoleAPIFactory value;
    public ConsoleAPIFactoriesListProto(String section, String key, ConsoleAPIFactory value){
        this.section = section;
        this.key = key;
        this.value = value;
    }
}