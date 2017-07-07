package com.example.nikolay.conecttotestserver;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button_id);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("click", "click");
                new HttpTask().execute();
            }
        });
        final Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        final Button button_login = (Button) findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    private class HttpTask extends AsyncTask<Void, Void, Void> {
        private OkHttpClient client = new OkHttpClient();
        private String result = "unknown";

        @Override
        protected Void doInBackground(Void... voids) {

            String username = ((EditText) findViewById(R.id.UserName)).getText().toString();
            String password1 = ((EditText) findViewById(R.id.password1)).getText().toString();
            String password2 = ((EditText) findViewById(R.id.password2)).getText().toString();
            String email = ((EditText) findViewById(R.id.email)).getText().toString();
            Log.d("username", username);
            HttpUrl.Builder builder = new HttpUrl.Builder()
                    .scheme("http")
                    .host("127.0.0.1")
                    .port(8000)
                    .addPathSegments("api/register/");
            final HttpUrl url = builder.build();
            RequestBody reqbody = new FormBody.Builder()
                    .add("username", username)
                    .add("password1", password1)
                    .add("password2", password2)
                    .add("email", email)
                    .build();
            Request request = new Request.Builder()
                    .url(url.toString())
                    .post(reqbody)
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
            TextView textView = (TextView) findViewById(R.id.text);

            textView.setText(result);
        }
    }
}
