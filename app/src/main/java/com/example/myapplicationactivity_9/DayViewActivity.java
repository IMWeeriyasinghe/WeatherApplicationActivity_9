package com.example.myapplicationactivity_9;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.LayoutInflater;

public class DayViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);


        Intent intent = getIntent();
        if (intent != null) {
            String date = intent.getStringExtra("date");
            String temperature = intent.getStringExtra("temperature");
            String humidity = intent.getStringExtra("humidity");
            int iconResourceId = intent.getIntExtra("icon", R.drawable.pic_01d);

            TextView txtDate = findViewById(R.id.txtDate2);
            TextView txtTemp = findViewById(R.id.txtTemp2);
            TextView txtHumidity = findViewById(R.id.txtHumidity2);
            ImageView imgIcon = findViewById(R.id.imgIcon2);

            if (date != null) {
                txtDate.setText(date);
            }
            if (temperature != null) {
                txtTemp.setText(temperature + " ℃");
            }
            if (humidity != null) {
                txtHumidity.setText("Humidity: " + humidity + "%");
            }
            imgIcon.setImageResource(iconResourceId);
        }
    }
}
