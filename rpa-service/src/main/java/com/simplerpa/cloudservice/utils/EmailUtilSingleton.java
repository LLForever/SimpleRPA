package com.simplerpa.cloudservice.utils;

public class EmailUtilSingleton {
    private static volatile EmailUtilSingleton instance;

    private EmailUtilSingleton(){

    }

    public static EmailUtilSingleton getInstance(){
        if (instance == null) {
            synchronized (EmailUtilSingleton.class) {
                if (instance == null) {
                    instance = new EmailUtilSingleton();
                }
            }
        }
        return instance;
    }
}
