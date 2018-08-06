package com.example.vlad.earthquaketrackingapp;

import android.app.usage.UsageEvents;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EarthquakeInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_info);
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, UsageEvents.Event> {

        @Override
        protected UsageEvents.Event doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

//            UsageEvents.Event result = Utils.fetchEarthquakeData(urls[0]);
//            return result;
            return null;
        }

        @Override
        protected void onPostExecute(UsageEvents.Event result) {
            if (result == null) {
                return;
            }

//            updateUi(result);
        }
    }
}
