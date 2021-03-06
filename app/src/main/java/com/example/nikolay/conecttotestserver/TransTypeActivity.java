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
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class TransTypeActivity extends AppCompatActivity {
    String username, password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_type);
        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        final Button button_add_type = (Button) findViewById(R.id.button_add_type);
        button_add_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpTask().execute();
            }
        });
    }

    private class HttpTask extends AsyncTask<Void, Void, Void> {
        private OkHttpClient client = new OkHttpClient();
        private String result = "unknown";

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String transtype = ((EditText) findViewById(R.id.type)).getText().toString();
                String ht = Util.getFilePathToSave("scheme");
                String host = Util.getFilePathToSave("host");
                String port = Util.getFilePathToSave("port");
                String type = Util.getFilePathToSave("pathType");
                HttpUrl.Builder builder = new HttpUrl.Builder()
                        .scheme(ht)
                        .host(host)
                        .port(Integer.parseInt(port))
                        .addPathSegments(type);
                final HttpUrl url = builder.build();
                RequestBody reqbody = new FormBody.Builder()
                        .add("trans_type", transtype)
                        .build();
                Request request = new Request.Builder()
                        .url(url.toString())
                        .header("Authorization", Credentials.basic(username, password))
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    if (result.equals("unknown")) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    } else {
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void op) {
            TextView textView = (TextView) findViewById(R.id.infoOutput);
            textView.setText(result);
        }
    }
}
