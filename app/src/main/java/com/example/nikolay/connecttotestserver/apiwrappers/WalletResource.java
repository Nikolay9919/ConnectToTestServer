package com.example.nikolay.connecttotestserver.apiwrappers;

import android.util.Log;

import com.example.nikolay.conecttotestserver.Util;
import com.example.nikolay.conecttotestserver.models.Wallet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
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
        Log.d("WalletResource.add",result);
        if (result.equals("unknown")) {
            throw new UnautorisedException();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(result, new TypeReference<Wallet>() {
        });
    }
}
