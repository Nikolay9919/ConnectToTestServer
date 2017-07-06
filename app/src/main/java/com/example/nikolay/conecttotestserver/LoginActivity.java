package com.example.nikolay.conecttotestserver;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        @Override
        protected Void doInBackground(Void... voids) {
            String username = ((EditText) findViewById(R.id.login)).getText().toString();
            String password = ((EditText) findViewById(R.id.passwordinput)).getText().toString();
            HttpUrl.Builder builder = new HttpUrl.Builder()
                    .scheme("http")
                    .host("10.10.9.217")
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

                textView.setText("Not");
            }else{
                textView.setText(result);
            }
        }
    }
}
