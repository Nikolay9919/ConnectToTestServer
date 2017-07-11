package com.example.nikolay.conecttotestserver;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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


public class DeleteWalletActivity extends AppCompatActivity {
    String username, password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_activity_wallet);
        final Intent intent = getIntent();

        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        final Intent intent1 = new Intent(DeleteWalletActivity.this, MenuActivity.class);
        Log.d("ret", username);
        final Button delete_wallet = (Button) findViewById(R.id.button_delete_wallet);
        delete_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpTask().execute();
                intent1.putExtra("username", username);
                intent1.putExtra("password", password);
                startActivity(intent1);

            }
        });

    }

    private class HttpTask extends AsyncTask<Void, Void, Void> {
        private OkHttpClient client = new OkHttpClient();
        private String result = "unknown";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String idWallet = ((EditText) findViewById(R.id.id_wallet_input)).getText().toString();
                HttpUrl.Builder builder = new HttpUrl.Builder()
                        .scheme("http")
                        .host("10.10.8.22")
                        .port(8000)
                        .addPathSegments("api/")
                        .addPathSegment(idWallet)
                        .addPathSegment("update/");
                final HttpUrl url = builder.build();
                RequestBody reqbody = new FormBody.Builder()
                        .add("id", idWallet)
                        .build();
                Log.i("id", idWallet);
                String basic = Credentials.basic(username, password);
                Log.d("auth", basic);
                Request request = new Request.Builder()
                        .url(url.toString())
                        .header("Authorization", Credentials.basic(username, password))
                        .delete(reqbody)
                        .build();
                Call newCall = client.newCall(request);
                Response response;
                try {
                    response = newCall.execute();
                    result = response.body().string();
                    Log.d("ree", result);
                } catch (Exception e) {
                    result = e.getMessage();
                    e.printStackTrace();
                    Log.e("ewr4", e.getMessage());
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
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();

            } else {
                if (result.contains("<!DOCTYPE html>")) {
                    Toast.makeText(getApplicationContext(), "Error,please check id", Toast.LENGTH_LONG).show();

                }
            }
        }
    }
}
//       TextView textView = (TextView) findViewById(R.id.infoOutput);
//            if (result.contains("<!DOCTYPE html>")) {
//                    textView.setText(result);
//                    textView.setText("Check wallet id");
//                    } else {
//                    textView.setText(result);
//                    }