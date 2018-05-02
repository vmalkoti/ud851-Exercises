/*
 * Copyright (C) 2014 The Android Open Source Project
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

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
//      DONE (21) Implement LoaderManager.LoaderCallbacks<Cursor>

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    //  DONE (18) Create a String array containing the names of the desired data columns from our ContentProvider
    private static final String[] COLUMN_NAMES = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES
    };
    //  DONE (19) Create constant int values representing each column name's position above
    private static final int POSITION_DATE_COL = 0;
    private static final int POSITION_MAX_TEMP_COL = 1;
    private static final int POSITION_MIN_TEMP_COL = 2;
    private static final int POSITION_WEATHER_COL = 3;
    private static final int POSITION_HUMIDITY_COL = 4;
    private static final int POSITION_PRESSURE_COL = 5;
    private static final int POSITION_WIND_SPEED_COL = 6;
    private static final int POSITION_WIND_DEGREE_COL = 7;

    //  DONE (20) Create a constant int to identify our loader used in DetailActivity
    private static final int LOADER_ID = 900;

    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

    //  DONE (15) Declare a private Uri field called mUri
    private Uri mUri;

//  DONE (10) Remove the mWeatherDisplay TextView declaration

    //  DONE (11) Declare TextViews for the date, description, high, low, humidity, wind, and pressure
    private TextView mDateTv;
    private TextView mDescriptionTv;
    private TextView mHighTempTv;
    private TextView mLowTempTv;
    private TextView mHumidityTv;
    private TextView mPressureTv;
    private TextView mWindSpeedTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//      DONE (12) Remove mWeatherDisplay TextView

//      DONE (13) Find each of the TextViews by ID
        mDateTv = (TextView) findViewById(R.id.tv_date);
        mDescriptionTv = (TextView) findViewById(R.id.tv_description);
        mHighTempTv = (TextView) findViewById(R.id.tv_high_temp);
        mLowTempTv = (TextView) findViewById(R.id.tv_low_temp);
        mHumidityTv = (TextView) findViewById(R.id.tv_humidity);
        mPressureTv = (TextView) findViewById(R.id.tv_pressure);
        mWindSpeedTv = (TextView) findViewById(R.id.tv_wind_speed);

//      DONE (14) Remove the code that checks for extra text
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {

//      DONE (16) Use getData to get a reference to the URI passed with this Activity's Intent
            Uri uri = intentThatStartedThisActivity.getData();
//      DONE (17) Throw a NullPointerException if that URI is null
            if (uri == null) {
                throw new NullPointerException("URI not passed to Details activity");
            } else {
                mUri = uri;
            }
//      DONE (35) Initialize the loader for DetailActivity
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        }
    }


    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }


    //  DONE (22) Override onCreateLoader
//          DONE (23) If the loader requested is our detail loader, return the appropriate CursorLoader
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case LOADER_ID:
                String date = mUri.getPathSegments().get(1);
                return new CursorLoader(this, mUri, COLUMN_NAMES,
                        null, null, null);

            default:
                throw new RuntimeException("Unknown loader requested " + loaderId);
        }

    }


//  DONE (24) Override onLoadFinished
//      DONE (25) Check before doing anything that the Cursor has valid data
//      DONE (26) Display a readable data string
//      DONE (27) Display the weather description (using SunshineWeatherUtils)
//      DONE (28) Display the high temperature
//      DONE (29) Display the low temperature
//      DONE (30) Display the humidity
//      DONE (31) Display the wind speed and direction
//      DONE (32) Display the pressure
//      DONE (33) Store a forecast summary in mForecastSummary
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()) {

            String readableDate = SunshineDateUtils.getFriendlyDateString(this,
                    data.getLong(DetailActivity.POSITION_DATE_COL), true);
            String weatherDesc = SunshineWeatherUtils.getStringForWeatherCondition(this,
                    data.getInt(DetailActivity.POSITION_WEATHER_COL));
            String highTemp = SunshineWeatherUtils.formatTemperature(this, data.getInt(DetailActivity.POSITION_MAX_TEMP_COL));
            String lowTemp = SunshineWeatherUtils.formatTemperature(this, data.getInt(DetailActivity.POSITION_MIN_TEMP_COL));
            String humidity = data.getInt(DetailActivity.POSITION_HUMIDITY_COL) + "%";
            String pressure = String.valueOf(data.getFloat(DetailActivity.POSITION_PRESSURE_COL));
            String wind = SunshineWeatherUtils.getFormattedWind(this,
                    data.getFloat(DetailActivity.POSITION_WIND_SPEED_COL),
                    data.getFloat(DetailActivity.POSITION_WIND_DEGREE_COL));

            mDateTv.setText(readableDate);
            mDescriptionTv.setText(weatherDesc);
            mHighTempTv.setText(highTemp);
            mLowTempTv.setText(lowTemp);
            mHumidityTv.setText(humidity);
            mPressureTv.setText(pressure);
            mWindSpeedTv.setText(wind);
        }
    }



//  DONE (34) Override onLoaderReset, but don't do anything in it yet
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // nothing
    }
}