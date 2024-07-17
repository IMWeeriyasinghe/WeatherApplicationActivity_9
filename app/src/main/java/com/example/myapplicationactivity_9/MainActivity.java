package com.example.myapplicationactivity_9;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.LayoutInflater;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String[] date_list = new String[20];
    private String[] temp_list = new String[20];
    private String[] humidity_list = new String[20];
    private Integer[] icon_list = new Integer[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inflate the header layout and add it to the ListView
        LayoutInflater inflater = getLayoutInflater();
        View headerView = inflater.inflate(R.layout.header_layout, null);
        ListView listView = findViewById(R.id.list_view);
        listView.addHeaderView(headerView);

        FetchOnlineData fetchOnlineData = new FetchOnlineData();
        fetchOnlineData.execute();
    }

    private class FetchOnlineData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr;

            try {
                final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?q=colombo,lk&cnt=20&units=metric&appid=751699e1d8b015830e4de960809775cd";
                URL url = new URL(BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();

                JSONObject weather_object = new JSONObject(forecastJsonStr);
                JSONArray data_list = weather_object.getJSONArray("list");
                for (int i = 0; i < data_list.length(); i++) {
                    JSONObject value_object = data_list.getJSONObject(i);
                    date_list[i] = value_object.getString("dt_txt");
                    JSONObject main_object = value_object.getJSONObject("main");
                    temp_list[i] = main_object.getString("temp");
                    humidity_list[i] = main_object.getString("humidity");
                    JSONArray weather_array = value_object.getJSONArray("weather");
                    JSONObject weather_array_object = weather_array.getJSONObject(0);
                    icon_list[i] = getApplicationContext().getResources().getIdentifier("pic_" +
                            weather_array_object.getString("icon"), "drawable", getApplicationContext().getPackageName());
                }
            } catch (IOException | JSONException e) {
                Log.e("MainActivity", "Error", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("MainActivity", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            CustomListAdapter adapter = new CustomListAdapter(MainActivity.this, date_list, temp_list, icon_list);
            ListView list = findViewById(R.id.list_view);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent detailActivity = new Intent(MainActivity.this, DayViewActivity.class);
                    detailActivity.putExtra("date", date_list[i]);
                    detailActivity.putExtra("temperature", temp_list[i]);
                    detailActivity.putExtra("humidity", humidity_list[i]);
                    detailActivity.putExtra("icon", icon_list[i]);
                    startActivity(detailActivity);
                }
            });
        }
    }
}
