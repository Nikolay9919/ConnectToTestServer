package com.example.nikolay.conecttotestserver;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button_login = (Button) findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("click", "click");
                new LoginActivity.HttpTask().execute();
            }
        });
        final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        final Button button_register = (Button) findViewById(R.id.button_registr);
        ((EditText) findViewById(R.id.login)).setText("nik");
        ((EditText) findViewById(R.id.passwordinput)).setText("nikolay123");
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    private class HttpTask extends AsyncTask<Void, Void, Void> {
        public HttpTask() {
            client = new OkHttpClient.Builder().build();
        }

        private OkHttpClient client;
        private String result = "unknown";
        String username;
        String password;
        Intent intent_addWallet;
        Intent intent_EditProfile;

        @Override
        protected Void doInBackground(Void... voids) {
            username = ((EditText) findViewById(R.id.login)).getText().toString();
            password = ((EditText) findViewById(R.id.passwordinput)).getText().toString();
            intent_addWallet = new Intent(LoginActivity.this, AddWalletActivity.class);
            intent_addWallet.putExtra("username", username);
            intent_addWallet.putExtra("password", password);

            String basic = Credentials.basic(username, password);
            Log.d("auth", basic);

            HttpUrl.Builder builder = new HttpUrl.Builder()
                    .scheme("http")
                    .host("10.10.8.22")
                    .port(8000)
                    .addPathSegments("api/login/");
            final HttpUrl url = builder.build();
            RequestBody reqbody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(url.toString())
                    .post(reqbody)
                    .header("Authorization", Credentials.basic(username, password))
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
            return null;
        }

        @Override
        protected void onPostExecute(Void op) {
            TextView textView = (TextView) findViewById(R.id.infoOutput);
            if (result.isEmpty()) {
            } else {


                final Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                textView.setText(result);

                startActivity(intent);
            }


        }
    }
}

