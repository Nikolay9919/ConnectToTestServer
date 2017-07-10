package com.example.nikolay.conecttotestserver;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class EditProfileActivity extends AppCompatActivity {
    String username, password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);
        Intent intent = getIntent();
//
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        Log.d("ret", username);
        final Button edit = (Button) findViewById(R.id.button_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpTask().execute();
            }
        });
    }

    private class HttpTask extends AsyncTask<Void, Void, Void> {
        private OkHttpClient client = new OkHttpClient();
        private String result = "unknown";


        @Override
        protected Void doInBackground(Void... voids) {

            String username_edit = ((EditText) findViewById(R.id.username_input)).getText().toString();
            String password_edit = ((EditText) findViewById(R.id.password_input)).getText().toString();
            String firstName = ((EditText) findViewById(R.id.first_name_input)).getText().toString();
            String lastName = ((EditText) findViewById(R.id.last_name_input)).getText().toString();
            String email = ((EditText) findViewById(R.id.email_input)).getText().toString();
            try {


                HttpUrl.Builder builder = new HttpUrl.Builder()
                        .scheme("http")
                        .host("10.10.8.22")
                        .port(8000)
                        .addPathSegments("api/edit/");
                final HttpUrl url = builder.build();
                Log.d("usr",username_edit);
                Log.d("psw",password_edit);
                Log.d("fn",firstName);
                Log.d("ls",lastName);
                Log.d("em",email);

                RequestBody reqbody = new FormBody.Builder()
                        .add("username", username_edit)
                        .add("password", password_edit)
                        .add("first_name", firstName)
                        .add("last_name", lastName)
                        .add("email", email)
                        .build();
                String basic = Credentials.basic(username, password);
                Log.d("auth", basic);
                Request request = new Request.Builder()
                        .url(url.toString())
                        .header("Authorization", Credentials.basic(username, password))
                        .put(reqbody)
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
                Log.e("ewr", e.getMessage());
            }
            password = password_edit;
            username = username_edit;
            return null;
        }

        @Override
        protected void onPostExecute(Void op) {
            TextView textView = (TextView) findViewById(R.id.infoOutput);

            textView.setText(result);
        }
    }
}
