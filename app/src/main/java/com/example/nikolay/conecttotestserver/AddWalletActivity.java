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

import com.example.nikolay.conecttotestserver.models.Wallet;
import com.example.nikolay.connecttotestserver.apiwrappers.UnautorisedException;
import com.example.nikolay.connecttotestserver.apiwrappers.WalletResource;

import java.io.IOException;

import okhttp3.Credentials;

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
        Wallet w = new Wallet();
        Wallet result;

        @Override
        protected Void doInBackground(Void... voids) {
            w.setName(((EditText) findViewById(R.id.name_wallet_input)).getText().toString());
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            w.setType(spinner.getSelectedItem().toString());
            String auth = Credentials.basic(username, password);
            try {
                result = WalletResource.add(w, auth);
            } catch (UnautorisedException ex) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("HttpTask", "Error");
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (final IOException ex) {
                Log.d("HttpTask", ex.getMessage());
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void op) {
            if (result != null) {
                TextView textView = (TextView) findViewById(R.id.infoOutput);

                textView.setText(result.toString());
            }
        }
    }
}
