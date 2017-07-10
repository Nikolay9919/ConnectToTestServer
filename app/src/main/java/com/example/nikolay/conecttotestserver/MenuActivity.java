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


public class MenuActivity extends AppCompatActivity {
    String username, password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        final Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        final Intent intent1 = new Intent(MenuActivity.this, EditProfileActivity.class);


        final Button editProfile = (Button) findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent1.putExtra("username", username);
                intent1.putExtra("password", password);
                startActivity(intent1);
            }
        });
        final Intent intent2 = new Intent(MenuActivity.this, WalletActivity.class);
        final Button walletList = (Button) findViewById(R.id.see_wallet_list);
        walletList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent2.putExtra("username", username);
                intent2.putExtra("password", password);
                startActivity(intent2);

            }
        });
//        final Button logout = (Button) findViewById(R.id.logout);
//        walletList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                startActivity(intent3);
//                username = "null";
//                password = "null";
//            }
//        });
        final Intent intent3 = new Intent(MenuActivity.this, LoginActivity.class);
        final Button logout = (Button) findViewById(R.id.button_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent3);
                username = "null";
                password = "null";

            }
        });
        final Intent intent4 = new Intent(MenuActivity.this, UpdateWalletActivity.class);
        final Button updateWallet = (Button) findViewById(R.id.update_wallet);
        updateWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent4.putExtra("username", username);
                intent4.putExtra("password", password);
                startActivity(intent4);
            }
        });
    }
}

