package com.example.vlad.earthquaketrackingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String[] urls;
    public EarthquakeLoader(Context context, String... urls) {
        super(context);
        this.urls = urls;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if (urls.length < 1 || urls[0] == null) {
            return null;
        }

        List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
        return result;
    }
}
