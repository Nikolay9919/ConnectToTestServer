package com.example.nikolay.connecttotestserver.apiwrappers;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nikolay.conecttotestserver.R;
import com.example.nikolay.conecttotestserver.Util;
import com.example.nikolay.conecttotestserver.models.Wallet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WalletResource {


    public static Wallet add(Wallet w, String auth) throws UnautorisedException, IOException {
        String result;
        OkHttpClient client = new OkHttpClient();
        Log.d("selected", w.getType());
        String host = Util.getFilePathToSave("host");
        String port = Util.getFilePathToSave("port");
        String wallet = Util.getFilePathToSave("pathWallet");
        String ht = Util.getFilePathToSave("scheme");

        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(ht)
                .host(host)
                .port(Integer.parseInt(port))
                .addPathSegments(wallet);

        final HttpUrl url = builder.build();
        RequestBody rebody = new FormBody.Builder()
                .add("name", w.getName())
                .add("type", w.getType())
                .build();
        Request request = new Request.Builder()
                .url(url.toString())
                .header("Authorization", auth)
                .post(rebody)
                .build();
        Call newCall = client.newCall(request);
        Response response;
        try {
            response = newCall.execute();
            result = response.body().string();
        } catch (Exception e) {
            result = e.getMessage();
            e.printStackTrace();
        }
        Log.d("WalletResource.add", result);
        if (result.equals("unknown")) {
            throw new UnautorisedException();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(result, new TypeReference<Wallet>() {
        });
    }

    public static StringBuilder get(String auth) {
        String result = "";
        String errorMessage;
        List<Wallet> wallets = Collections.emptyList();
        OkHttpClient client = new OkHttpClient();
        String ht = Util.getFilePathToSave("scheme");
        String host = Util.getFilePathToSave("host");
        String port = Util.getFilePathToSave("port");
        String wallet = Util.getFilePathToSave("pathWallet");
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(ht)
                .host(host)
                .port(Integer.parseInt(port))
                .addPathSegments(wallet);
        final HttpUrl url = builder.build();
        Request request = new Request.Builder()
                .url(url.toString())
                .header("Authorization", auth)
                .build();
        Call newCall = client.newCall(request);
        Response response;
        try {
            response = newCall.execute();
            result = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            wallets = objectMapper.readValue(result, new TypeReference<List<Wallet>>() {
            });
        } catch (Exception e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (Wallet walet : wallets) {
            sb.append(walet.toString())
                    .append(" \n");
        }
        return sb;
    }
    public static String delete(String auth,String idWallet)
    {
         OkHttpClient client = new OkHttpClient();
         String result = "unknown";

        String host = Util.getFilePathToSave("host");
        String port = Util.getFilePathToSave("port");
        String ht = Util.getFilePathToSave("scheme");
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(ht)
                .host(host)
                .port(Integer.parseInt(port))
                .addPathSegments("api/")
                .addPathSegment(idWallet)
                .addPathSegment("update/");
        final HttpUrl url = builder.build();
        RequestBody reqbody = new FormBody.Builder()
                .add("id", idWallet)
                .build();
        Request request = new Request.Builder()
                .url(url.toString())
                .header("Authorization", auth)
                .delete(reqbody)
                .build();
        Call newCall = client.newCall(request);
        Response response;
        try {
            response = newCall.execute();
            result = response.body().string();
        } catch (Exception e) {
            result = e.getMessage();
            e.printStackTrace();
        } try {

    } catch (Exception e) {
        e.printStackTrace();
    }
 return result;
    }
}
