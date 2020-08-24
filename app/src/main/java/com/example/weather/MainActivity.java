package com.example.weather;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    TextView textViewSituation;
    TextView textViewDescription;
    TextView textViewTemperature;
    public void findWeather(View view){
        EditText editText = findViewById(R.id.cityName);
        String city = editText.getText().toString();
        Log.i("city",city);
        DownloadTask task = new DownloadTask();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=8f8b1e500605c6d1d0a13db26cdf7fa5");
    }

    class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpsURLConnection connection = null;
            try{
                url= new URL(urls[0]);
                connection = (HttpsURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data!= -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }
                return result;
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                DecimalFormat df = new DecimalFormat("0.00");
                String situation = "";
                String description = "";
                JSONObject jsonObject = new JSONObject(s);
                JSONObject main = jsonObject.getJSONObject("main");
                double temp = Double.parseDouble(main.getString("temp"));
                temp -= 273.15;


                String weatherData = jsonObject.getString("weather");

                Log.i("Weather info:",weatherData);
                JSONArray arr = new JSONArray(weatherData);
                for(int i = 0; i < arr.length(); i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    description = jsonPart.getString("description");
                    situation = jsonPart.getString("main");
                    Log.i("main",jsonPart.getString("main"));
                    Log.i("description",jsonPart.getString("description"));
                }
                Log.i("temp",Double.toString(temp));
                textViewTemperature.setText(df.format(temp)+"°C");
                textViewSituation.setText(situation);
                textViewDescription.setText(description);





            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTemperature = findViewById(R.id.temp);
        textViewSituation = findViewById(R.id.situation);
        textViewDescription = findViewById(R.id.description);

    }
}
