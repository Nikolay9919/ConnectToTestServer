package com.example.nikolay.conecttotestserver;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
Button button = (Button) findViewById(R.id.button_id);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("click","click");
                new HttpTask().execute();

            }
        });
//

//ObjectMapper mapper = new ObjectMapper();
//        User user = mapper.readValue(new File("user.json"),User.class);
    }

    private class HttpTask extends AsyncTask<Void, Void, Void> {
        private OkHttpClient client = new OkHttpClient();
//

private String result="unknown";
//        private char[] test;
//
//        public void onClick(View arg0){
//            EditText test = (EditText) findViewById(R.id.email);
//            String test1 = test.getText().toString();
//
//
//        }
        @Override
        protected Void doInBackground(Void... voids) {

            String username =((EditText) findViewById(R.id.UserName)).getText().toString();
            Log.d("username",username);


            HttpUrl.Builder builder = new HttpUrl.Builder()
                    .scheme("http")
                    .addQueryParameter("username",username)
                    .host("10.10.9.217")
                    .port(8000)
                    .addPathSegments("/api/register/");
            final HttpUrl url =builder.build();
            RequestBody reqbody = RequestBody.create(null, new byte[0]);
            Request request = new Request.Builder()
                    .url(url.toString())
                    .post(reqbody)

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
//            Document doc = Jsoup.parse(url1);
//            Elements linkedElements = doc.select("a[href]");

//            for (Element el : linkedElements) {
//                links.add(el.attr("href"));
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void op) {
            TextView textView = (TextView) findViewById(R.id.text);
           textView.setText(result);

        }


    }
}
//10.10.9.217:8000