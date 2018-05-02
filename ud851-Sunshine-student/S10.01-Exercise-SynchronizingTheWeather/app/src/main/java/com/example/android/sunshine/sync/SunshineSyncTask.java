package com.example.android.sunshine.sync;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

//  DONE (1) Create a class called SunshineSyncTask
//  DONE (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
//      DONE (3) Within syncWeather, fetch new weather data
//      DONE (4) If we have valid results, delete the old data and insert the new
public class SunshineSyncTask {

    synchronized  public static void syncWeather(Context context) {
        URL weatherRequestUrl = NetworkUtils.getUrl(context);

        try {
            String jsonWeatherResponse = NetworkUtils
                    .getResponseFromHttpUrl(weatherRequestUrl);

            ContentValues[] contentValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, jsonWeatherResponse);

            if(contentValues != null && contentValues.length > 0) {
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI,
                        null, null);
                contentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, contentValues);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}