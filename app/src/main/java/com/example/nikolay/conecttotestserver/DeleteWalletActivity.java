package com.example.nikolay.conecttotestserver;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikolay.conecttotestserver.models.Wallet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

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
        new getWalletsTask().execute();
    }

    private class getWalletsTask extends AsyncTask<Void, Void, Void> {
        private OkHttpClient client = new OkHttpClient();
        private String result = "unknown";
        List<Wallet> wallets = Collections.emptyList();

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
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void op) {
            Spinner spinId = (Spinner) findViewById(R.id.spinner_idwall);
            ArrayAdapter<Wallet> adapter1 = new ArrayAdapter<>(DeleteWalletActivity.this, android.R.layout.simple_spinner_item, wallets);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinId.setAdapter(adapter1);
            spinId.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                            Wallet selectedW = wallets.get(position);
                            ((EditText) findViewById(R.id.id_wallet_input)).setText(String.valueOf(selectedW.getId()));
                            Spinner spinner = (Spinner) findViewById(R.id.spinner);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }
                    });
        }
    }

    private class HttpTask extends AsyncTask<Void, Void, Void> {
        private OkHttpClient client = new OkHttpClient();
        private String result = "unknown";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String idWallet = ((EditText) findViewById(R.id.id_wallet_input)).getText().toString();
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
