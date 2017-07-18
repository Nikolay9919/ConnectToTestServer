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
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikolay.conecttotestserver.models.Wallet;
import com.example.nikolay.connecttotestserver.apiwrappers.WalletResource;

import java.util.Collections;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;


public class DeleteWalletActivity extends AppCompatActivity {
    String username, password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_activity_wallet);
        final Intent intent = getIntent();

        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        final Intent intent1 = new Intent(DeleteWalletActivity.this, MenuActivity.class);
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
            String auth = Credentials.basic(username, password);
            result = String.valueOf(WalletResource.get(auth));
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
                        }
                    });
        }
    }

    private class HttpTask extends AsyncTask<Void, Void, Void> {

        String result;
        @Override
        protected Void doInBackground(Void... voids) {
            String auth = Credentials.basic(username, password);
            String idWallet = ((EditText) findViewById(R.id.id_wallet_input)).getText().toString();
            result = WalletResource.delete(auth,idWallet);
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
