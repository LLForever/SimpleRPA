package com.simplerpa.cloudservice.entity.util;

import java.net.URL;

public class RpaTools {
    public static boolean isURLValid(String url){
        try {
            new URL(url);
            return true;
        }catch (Exception e){
//            e.printStackTrace();
            return false;
        }
    }
}
