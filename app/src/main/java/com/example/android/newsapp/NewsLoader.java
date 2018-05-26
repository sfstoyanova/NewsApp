package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viper on 14-May-18.
 */

public class NewsLoader extends AsyncTaskLoader<List<New>> {
    /**
     * Query URL
     */
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<New> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Create a list of news
        List<New> news = QueryUtils.fetchEarthquakeData(mUrl);
        return news;
    }
}
