package com.example.nikolay.connecttotestserver.apiwrappers;

import android.util.Log;

import com.example.nikolay.conecttotestserver.Util;
import com.example.nikolay.conecttotestserver.models.Wallet;
import com.example.nikolay.conecttotestserver.util.OkHttpClientFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class WalletResource {


    public static Wallet add(Wallet w, String auth) throws UnauthorisedException, IOException {
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
            throw new UnauthorisedException();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(result, new TypeReference<Wallet>() {
        });
    }

    public static StringBuilder get(String auth) {
        String result;
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
    public static List<Wallet> getforSpin(String auth) {
        String result;
        String errorMessage;
        List<Wallet> wallets = Collections.emptyList();

        OkHttpClient client = OkHttpClientFactory.get();
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
        return wallets;
    }

    public static String delete(String auth, String idWallet) {
        OkHttpClient client = new OkHttpClient();
        String result;

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
        }
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static StringBuilder update(String auth, String idWallet, String selected, String nameWallet) {
        StringBuilder sb = new StringBuilder();
        List<Wallet> wallets = Collections.emptyList();
        String ht = Util.getFilePathToSave("scheme");
        String host = Util.getFilePathToSave("host");
        String port = Util.getFilePathToSave("port");
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor()).build();
        String result = "unknown";
        try {
            HttpUrl.Builder builder = new HttpUrl.Builder()
                    .scheme(ht)
                    .host(host)
                    .port(Integer.parseInt(port))
                    .addPathSegments("api/")
                    .addPathSegment(idWallet)
                    .addPathSegment("update/");
            final HttpUrl url = builder.build();
            RequestBody reqbody = new FormBody.Builder()
                    .add("name", nameWallet)
                    .add("type", selected)
                    .build();
            Request request = new Request.Builder()
                    .url(url.toString())
                    .header("Authorization", auth)
                    .post(reqbody)
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
                result = e.getMessage();
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Wallet wallet : wallets) {
            sb.append(wallet.toString())
                    .append("\n");
        }
        Log.d("Task", selected);

        return sb;
    }

    public static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.d("OkHttp", String.format("--> Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

            Buffer requestBuffer = new Buffer();
            request.body().writeTo(requestBuffer);
            Log.d("OkHttp", requestBuffer.readUtf8());

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.d("OkHttp", String.format("<-- Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            MediaType contentType = response.body().contentType();
            String content = response.body().string();
            Log.d("OkHttp", content);

            ResponseBody wrappedBody = ResponseBody.create(contentType, content);
            return response.newBuilder().body(wrappedBody).build();
        }


    }
}