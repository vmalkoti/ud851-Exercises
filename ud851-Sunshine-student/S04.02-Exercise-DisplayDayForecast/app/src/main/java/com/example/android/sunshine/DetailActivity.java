package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private TextView mDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // DONE (2) Display the weather forecast that was passed from MainActivity
        Intent receivedIntent = getIntent();
        if(receivedIntent.hasExtra(Intent.EXTRA_TEXT)) {
            mDetailsTextView = (TextView) findViewById(R.id.tv_day_data);
            mDetailsTextView.setText(receivedIntent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }
}