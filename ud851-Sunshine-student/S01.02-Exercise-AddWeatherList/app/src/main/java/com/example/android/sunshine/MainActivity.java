/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // DONE (1) Create a field to store the weather display TextView
    TextView weatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // DONE (2) Use findViewById to get a reference to the weather display TextView
        weatherData = (TextView) findViewById(R.id.tv_weather_data);

        // TODO (3) Create an array of Strings that contain fake weather data
        ArrayList<String[]> data = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        data.add(new String[] {dateFormat.format(calendar.getTime()), "Sunny"});
        calendar.add(Calendar.DATE, 1);
        data.add(new String[] {dateFormat.format(calendar.getTime()), "Mostly Sunny"});
        calendar.add(Calendar.DATE, 1);
        data.add(new String[] {dateFormat.format(calendar.getTime()), "Cloudy"});
        calendar.add(Calendar.DATE, 1);
        data.add(new String[] {dateFormat.format(calendar.getTime()), "Cloudy"});
        calendar.add(Calendar.DATE, 1);
        data.add(new String[] {dateFormat.format(calendar.getTime()), "Rain"});
        calendar.add(Calendar.DATE, 1);
        data.add(new String[] {dateFormat.format(calendar.getTime()), "Showers"});
        calendar.add(Calendar.DATE, 1);
        data.add(new String[] {dateFormat.format(calendar.getTime()), "Rain"});
        calendar.add(Calendar.DATE, 1);
        data.add(new String[] {dateFormat.format(calendar.getTime()), "Thunderstorm"});
        calendar.add(Calendar.DATE, 1);
        data.add(new String[] {dateFormat.format(calendar.getTime()), "Partly Cloudy"});
        calendar.add(Calendar.DATE, 1);
        data.add(new String[] {dateFormat.format(calendar.getTime()), "Sunny"});


        // DONE (4) Append each String from the fake weather data array to the TextView
        for(String[] s: data) {
            weatherData.append(s[0] + " : " + s[1] + "\n \n \n");
        }
    }
}