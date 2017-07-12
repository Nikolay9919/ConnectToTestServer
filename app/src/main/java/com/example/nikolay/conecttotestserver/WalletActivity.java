package com.example.nikolay.conecttotestserver;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nikolay.conecttotestserver.models.Wallet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WalletActivity extends AppCompatActivity {
    String username, password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Intent intent2 = getIntent();

        username = intent2.getStringExtra("username");
        password = intent2.getStringExtra("password");
        final Button button_add_wallet = (Button) findViewById(R.id.button_wallet);

        final Intent intent1 = new Intent(WalletActivity.this, AddWalletActivity.class);
        button_add_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent1.putExtra("username", username);
                intent1.putExtra("password", password);
                startActivity(intent1);
            }
        });
        new GetWalletsTask().execute();

    }

    private class GetWalletsTask extends AsyncTask<Void, Void, Void> {
        private OkHttpClient client = new OkHttpClient();
        List<Wallet> wallets = Collections.emptyList();
        private String result = "";
        private String errorMessage;

        @Override
        protected Void doInBackground(Void... voids) {
            HttpUrl.Builder builder = new HttpUrl.Builder()
                    .scheme("http")
                    .host("10.10.8.22")
                    .port(8000)
                    .addPathSegments("api/wallet/");
            final HttpUrl url = builder.build();
            Request request = new Request.Builder()
                    .url(url.toString())
                    .header("Authorization", Credentials.basic(username, password))
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

                return null;
        }

        @Override
        protected void onPostExecute(Void op) {
            TextView textView = (TextView) findViewById(R.id.infoOutput);
            if (errorMessage != null) {
                textView.setText(errorMessage + " :\n" + result);
            }
            StringBuilder sb = new StringBuilder();
            for (Wallet wallet : wallets) {
                sb.append(wallet.toString())
                        .append(" \n");
            }
            textView.setText(sb.toString());

        }
    }
}
