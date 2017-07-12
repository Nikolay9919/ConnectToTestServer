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

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AddWalletActivity extends AppCompatActivity {
    String username, password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity_wallet);
        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        Log.d("ret", username);
        final Button button_add_wallet = (Button) findViewById(R.id.button_wallet);
        button_add_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpTask().execute();
            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Choise_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private class HttpTask extends AsyncTask<Void, Void, Void> {
        private OkHttpClient client = new OkHttpClient();
        private String result = "unknown";
        String selected = "Visa";

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String nameWallet = ((EditText) findViewById(R.id.name_wallet_input)).getText().toString();
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                selected = spinner.getSelectedItem().toString();
                Log.d("selected", selected);
                HttpUrl.Builder builder = new HttpUrl.Builder()
                        .scheme("http")
                        .host("10.10.8.22")
                        .port(8000)
                        .addPathSegments("api/wallet/");
                final HttpUrl url = builder.build();
                RequestBody reqbody = new FormBody.Builder()
                        .add("name", nameWallet)
                        .add("type", selected)
                        .build();
                String basic = Credentials.basic(username, password);
                Log.d("auth", basic);
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
                    Log.d("ree", result);
                } catch (Exception e) {
                    result = e.getMessage();
                    e.printStackTrace();
                    Log.e("ewr4", e.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
//                Log.e("ewr", e.getMessage());
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
