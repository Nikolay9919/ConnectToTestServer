package com.example.nikolay.conecttotestserver.util;

import com.example.nikolay.conecttotestserver.Util;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;


public class OkHttpClientFactory {

    private static OkHttpClient client;
    public static okhttp3.OkHttpClient get(){
        if(client ==null){
            client = new okhttp3.OkHttpClient();
        }



        return client;
    }
}
