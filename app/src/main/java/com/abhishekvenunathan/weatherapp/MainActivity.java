package com.abhishekvenunathan.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView outputText;
    TextView outputText2;
    EditText cityName;
    Button getDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputText=findViewById(R.id.outputText);
        outputText2=findViewById(R.id.outputText2);
        cityName=findViewById(R.id.cityName);
        getDetails=findViewById(R.id.getDetails);

        }

    public void checkWeather(View view) {

        InputMethodManager mgr=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(getDetails.getWindowToken(),0);

        DownloadTask task = new DownloadTask();
        task.execute("https://openweathermap.org/data/2.5/weather?q="+cityName.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22");


    }

    public class DownloadTask extends AsyncTask<String,Void,String>{

            @Override
            protected String doInBackground(String... urls) {
                String result="";
                URL url;
                HttpURLConnection urlConnection=null;

                try {
                    url = new URL(urls[0]);
                    urlConnection=(HttpURLConnection)url.openConnection();
                    InputStream in=urlConnection.getInputStream();
                    InputStreamReader reader=new InputStreamReader(in);
                    int data = reader.read();

                    while (data!=-1){
                        char current=(char)data;
                        result += current;
                        data=reader.read();

                    }
                    return result;

                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try{
                    JSONObject jsonObject=new JSONObject(s);

                    String weatherInfo=jsonObject.getString("weather");

                    JSONObject tempInfo=jsonObject.getJSONObject("main");


                    Log.i("WEATHER",weatherInfo);

                    Log.i("TEMPERATURE",tempInfo.getString("temp"));

                    JSONArray array=new JSONArray(weatherInfo);

                    for (int i=0;i<array.length();i++){
                        JSONObject weather=array.getJSONObject(i);

                        outputText.setText("Weather: "+weather.getString("main")+"\n"+"Description: "+weather.getString("description"));

                    }

                    outputText2.setText("Temperature: "+tempInfo.getString("temp")+(char) 0x00B0+"C");


                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
}

