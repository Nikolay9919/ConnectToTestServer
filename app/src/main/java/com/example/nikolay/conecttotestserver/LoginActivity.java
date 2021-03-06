package com.example.nikolay.conecttotestserver;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button_login = (Button) findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginActivity.HttpTask().execute();
            }
        });
        final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        final Button button_register = (Button) findViewById(R.id.button_registr);
        ((EditText) findViewById(R.id.login)).setText("nik");
        ((EditText) findViewById(R.id.passwordinput)).setText("nikolya123");
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

        @Override
        protected Void doInBackground(Void... voids) {
            username = ((EditText) findViewById(R.id.login)).getText().toString();
            password = ((EditText) findViewById(R.id.passwordinput)).getText().toString();
            intent_addWallet = new Intent(LoginActivity.this, AddWalletActivity.class);
            intent_addWallet.putExtra("username", username);
            intent_addWallet.putExtra("password", password);
            String host = Util.getFilePathToSave("host");
            String port = Util.getFilePathToSave("port");
            String login = Util.getFilePathToSave("pathLogin");
            String ht = Util.getFilePathToSave("scheme");

            HttpUrl.Builder builder = new HttpUrl.Builder()
                    .scheme(ht)
                    .host(host)
                    .port(Integer.parseInt(port))
                    .addPathSegments(login);

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
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Incorrect login or password", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                if (result.contains("Failed to connect")) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Failed to connect", Toast.LENGTH_LONG).show();
                        }
                    });
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

}