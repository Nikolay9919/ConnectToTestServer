package com.example.nikolay.conecttotestserver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class WalletActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        final Intent intent = new Intent(WalletActivity.this, MainActivity.class);
        String[] spinMenu = {"Visa","Master","Cash"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinMenu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.choise_types);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Choise types");
        spinner.setSelection(1);

    }
}
