package com.example.nikolay.conecttotestserver;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nikolay.connecttotestserver.apiwrappers.WalletResource;

import okhttp3.Credentials;

public class WalletActivity extends AppCompatActivity {
    String username, password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Intent intent2 = getIntent();

        username = intent2.getStringExtra("username");
        password = intent2.getStringExtra("password");
        final Button button_add_wallet = (Button) findViewById(R.id.button_wallet);

        final Intent intent1 = new Intent(WalletActivity.this, AddWalletActivity.class);
        button_add_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent1.putExtra("username", username);
                intent1.putExtra("password", password);
                startActivity(intent1);
            }
        });
        new GetWalletsTask().execute();
    }

    private class GetWalletsTask extends AsyncTask<Void, Void, Void> {


        StringBuilder result;

        @Override
        protected Void doInBackground(Void... voids) {

            String auth = Credentials.basic(username, password);
            result = WalletResource.get(auth);
            return null;
        }

        @Override
        protected void onPostExecute(Void op) {
            TextView textView = (TextView) findViewById(R.id.infoOutput);

            textView.setText(result);
        }
    }
}
