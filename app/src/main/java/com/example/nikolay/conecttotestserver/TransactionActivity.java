package com.example.nikolay.conecttotestserver;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nikolay.conecttotestserver.models.Wallet;
import com.example.nikolay.connecttotestserver.apiwrappers.WalletResource;

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

public class TransactionActivity extends AppCompatActivity {
    String username, password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        final Button trans = (Button) findViewById(R.id.button_id);
        trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TransactionHttpTask().execute();
            }
        });
        new HttpTask2().execute();
        new getWalletsTask().execute();
    }

    private class getWalletsTask extends AsyncTask<Void, Void, Void> {
        private String result;
        List<Wallet> wallets = Collections.emptyList();

        @Override
        protected Void doInBackground(Void... voids) {
            String auth = Credentials.basic(username, password);
            result = String.valueOf(WalletResource.get(auth));
            return null;
        }

        @Override
        protected void onPostExecute(Void op) {
            Spinner spinId = (Spinner) findViewById(R.id.spinner_idwall);
            ArrayAdapter<Wallet> adapter1 = new ArrayAdapter<>(TransactionActivity.this, android.R.layout.simple_spinner_item, wallets);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinId.setAdapter(adapter1);
            spinId.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            Wallet selectedW = wallets.get(position);
                            ((EditText) findViewById(R.id.wallet_id)).setText(String.valueOf(selectedW.getId()));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });
        }
    }

    String ht = Util.getFilePathToSave("scheme");
    String host = Util.getFilePathToSave("host");
    String port = Util.getFilePathToSave("port");
    String transaction = Util.getFilePathToSave("pathTrans");

    private class TransactionHttpTask extends AsyncTask<Void, Void, Void> {
        private OkHttpClient client = new OkHttpClient();
        private String result1 = "unknown";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String idWallet = ((EditText) findViewById(R.id.wallet_id)).getText().toString();
                String transValue = ((EditText) findViewById(R.id.trans_value)).getText().toString();
                String transType = ((EditText) findViewById(R.id.trans_type)).getText().toString();

                HttpUrl.Builder builder = new HttpUrl.Builder()
                        .scheme(ht)
                        .host(host)
                        .port(Integer.parseInt(port))
                        .addPathSegments(transaction);
                final HttpUrl url = builder.build();
                RequestBody reqbody = new FormBody.Builder()
                        .add("wallet_id", idWallet)
                        .add("transaction_value", transValue)
                        .add("transaction_type", transType)
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
                    result1 = response.body().string();
                } catch (Exception e) {
                    result1 = e.getMessage();
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            runOnUiThread(new Runnable() {
                public void run() {
                    if (result1.equals("unknown")) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    } else {
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void op) {
            new HttpTask2().execute();
        }
    }

    private class HttpTask2 extends AsyncTask<Void, Void, Void> {
        private OkHttpClient client = new OkHttpClient();
        private String result = "unknown";

        @Override
        protected Void doInBackground(Void... voids) {
            HttpUrl.Builder builder = new HttpUrl.Builder()
                    .scheme(ht)
                    .host(host)
                    .port(Integer.parseInt(port))
                    .addPathSegments(transaction);
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
        }
    }
}

