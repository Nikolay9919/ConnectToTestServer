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

import com.example.nikolay.conecttotestserver.models.Wallet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UpdateWalletActivity extends AppCompatActivity {
    String username, password;//, selected1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity_wallet);
        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        Log.d("ret", username);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Choise_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        new getWalletsTask().execute();
        final Button update_wallet = (Button) findViewById(R.id.button_wallet);
        update_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateWalletTask().execute();

            }
        });

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
//                Document doc = Jsoup.parse(result);
//                Elements nameElement;
//                nameElement = doc.select("name");
//                    Log.d("name", String.valueOf(nameElement));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void op) {
            Spinner spinId = (Spinner) findViewById(R.id.spinner_idwall);
            ArrayAdapter<Wallet> adapter1 = new ArrayAdapter<>(UpdateWalletActivity.this, android.R.layout.simple_spinner_item, wallets);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinId.setAdapter(adapter1);
            spinId.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                            Wallet selectedW = wallets.get(position);
//                            Log.d("yyy" ,"---"+((EditText) findViewById(R.id.id_wallet_input)).getText().toString());
//                            Log.d("yyy" ,"---"+((EditText) findViewById(R.id.name_wallet_input)).getText().toString());

                            ((EditText) findViewById(R.id.id_wallet_input1)).setText(String.valueOf(selectedW.getId()));
                            ((EditText) findViewById(R.id.name_wallet_input1)).setText(selectedW.getName());
                            Spinner spinner = (Spinner) findViewById(R.id.spinner);
                            Map<String, Integer> types = new HashMap<>();
                            types.put("visa", 0);
                            types.put("master", 1);
                            types.put("cash", 2);


                            spinner.setSelection(types.get(selectedW.getType()));
                            spinner.setSelection(types.get(selectedW.getType()));
//                            Log.d("qqq", position + "");
//                            Log.d("qqq", selectedW.toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }


                    });


        }
    }

    private class UpdateWalletTask extends AsyncTask<Void, Void, Void> {
        private OkHttpClient client = new OkHttpClient();
        private String result = "unknown";
        String selected = "Visa";
        List<Wallet> wallets = Collections.emptyList();
        StringBuilder sb = new StringBuilder();

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String idWallet = ((EditText) findViewById(R.id.id_wallet_input1)).getText().toString();
                String nameWallet = ((EditText) findViewById(R.id.name_wallet_input1)).getText().toString();
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                selected = spinner.getSelectedItem().toString();
                HttpUrl.Builder builder = new HttpUrl.Builder()
                        .scheme("http")
                        .host("10.10.8.22")
                        .port(8000)
                        .addPathSegments("api/")
                        .addPathSegment(idWallet)
                        .addPathSegment("update/");
                final HttpUrl url = builder.build();
                RequestBody reqbody = new FormBody.Builder()
                        .add("name", nameWallet)
                        .add("type", selected)
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
                    ObjectMapper objectMapper = new ObjectMapper();
                    wallets = objectMapper.readValue(result, new TypeReference<List<Wallet>>() {
                    });
                } catch (Exception e) {
                    result = e.getMessage();
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Wallet wallet : wallets) {
                sb.append(wallet.toString())
                        .append("\n");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void op) {

            TextView textView = (TextView) findViewById(R.id.infoOutput);
            if (result.contains("<!DOCTYPE html>")) {
                textView.setText(result);
                textView.setText("Check wallet id");
            } else {
                textView.setText(sb.toString());
            }
        }
    }
}