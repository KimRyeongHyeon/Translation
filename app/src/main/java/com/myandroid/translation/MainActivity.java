package com.myandroid.translation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView resultText;
    Button transBtn;
    String get, message, result, translatedText;
    Spinner spinner;

    String clientID = "API KEY ID";
    String clientSECRET = "API KEY";

    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        resultText = (TextView)findViewById(R.id.resultText);
        transBtn = (Button)findViewById(R.id.transBtn);
        spinner = (Spinner)findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        transBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos == 1) {
                    translationKOENG(clientID, clientSECRET);
                } else if(pos == 2) {
                    translationENGKO(clientID, clientSECRET);
                }
            }
        });
    }

    private void translationENGKO(final String clientId, final String clientSecret) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String msg = editText.getText().toString();
                    String text = URLEncoder.encode(msg, "UTF-8");
                    String apiURL = "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation";
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
                    con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
                    // post request
                    String postParams = "source=en&target=ko&text=" + text;
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(postParams);
                    wr.flush();
                    wr.close();
                    int responseCode = con.getResponseCode();
                    BufferedReader br;
                    if(responseCode==200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // 오류 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    System.out.println(response.toString());

                    get = response.toString();
                    JSONObject object = new JSONObject(get);
                    message = object.getString("message");

                    object = new JSONObject(message);
                    result = object.getString("result");

                    object = new JSONObject(result);
                    translatedText = object.getString("translatedText");
                } catch (Exception e) {
                    System.out.println(e);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultText.setText(translatedText);
                    }
                });
            }
        }).start();
    }

    private void translationKOENG(final String clientId, final String clientSecret) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String msg = editText.getText().toString();
                    String text = URLEncoder.encode(msg, "UTF-8");
                    String apiURL = "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation";
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
                    con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
                    // post request
                    String postParams = "source=ko&target=en&text=" + text;
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(postParams);
                    wr.flush();
                    wr.close();
                    int responseCode = con.getResponseCode();
                    BufferedReader br;
                    if (responseCode == 200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // 오류 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    System.out.println(response.toString());

                    get = response.toString();
                    JSONObject object = new JSONObject(get);
                    message = object.getString("message");

                    object = new JSONObject(message);
                    result = object.getString("result");

                    object = new JSONObject(result);
                    translatedText = object.getString("translatedText");
                } catch (Exception e) {
                    System.out.println(e);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultText.setText(translatedText);
                    }
                });
            }
        }).start();
    }
}