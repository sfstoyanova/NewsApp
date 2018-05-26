package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<New>> {

    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;
    /**
     * RecyclerView for the list of earthquakes
     */
    private RecyclerView rvNews;
    /**
     * Holds the list of news
     */
    private List<New> mNewsList;
    /* Checks if there is a network connectivity*/
    private boolean isConnected;
    /* View for no news case*/
    private TextView noNewsView;
    private ProgressBar mLoadingSpinnerBar;
    private ConnectivityManager mConnectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingSpinnerBar = (ProgressBar) findViewById(R.id.loading_spinner);

        // Create a view for case where there are no news
        noNewsView = (TextView) findViewById(R.id.no_news);

        // Create adapter passing in the sample user data
        mAdapter = new NewsAdapter(new ArrayList<New>());

        // Lookup the RecyclerView in activity layout
        rvNews = (RecyclerView) findViewById(R.id.recycler_view);

        // Attach the adapter to the NewsAdapter to populate items
        rvNews.setAdapter(mAdapter);
        // Set layout manager to position the items
        rvNews.setLayoutManager(new LinearLayoutManager(this));

        mConnectivityManager =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();

        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        if (isConnected == true) {
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(0, null, this);
        } else {
            noNewsView.setText(R.string.no_connection);
            mLoadingSpinnerBar.setVisibility(View.GONE);
        }

        rvNews.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvNews, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Find the current new that was clicked on
                New currentNew = mNewsList.get(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri webpage = Uri.parse(currentNew.getWebSite());

                // Create a new intent to view the new's URI
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                // Send the intent to launch a new activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public Loader<List<New>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String orderBy  = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("content.guardianapis.com");
        uriBuilder.appendPath("search");
        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("from-date", "2017-12-01");
        uriBuilder.appendQueryParameter("tag", "lifeandstyle%2Ffood-and-drink");
        uriBuilder.appendQueryParameter("q", "food");
        uriBuilder.appendQueryParameter("api-key", "8a76c4a1-56df-4b75-9147-cd4f41481884");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("orderBy", orderBy);

        URL url = null;

        try {
            url = new URL(URLDecoder.decode(uriBuilder.build().toString(), "UTF-8"));
            return new NewsLoader(MainActivity.this, url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new NewsLoader(MainActivity.this, url.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<New>> loader, List<New> news) {

        noNewsView.setText(R.string.no_news);
        mLoadingSpinnerBar.setVisibility(View.GONE);

        //clear the adapter
        mAdapter.clearAdapter(mNewsList);

        if (news == null) {
            rvNews.setVisibility(View.GONE);
            noNewsView.setVisibility(View.VISIBLE);
        } else {
            rvNews.setVisibility(View.VISIBLE);
            noNewsView.setVisibility(View.GONE);
        }
        // If there is a valid list of {@link New}s, then add them to the adapter's
        // data set. This will trigger the RecyclerView to update.
        if (news != null && !news.isEmpty()) {
            mAdapter.swapItems(news);
            mNewsList = news;
        }
    }

    @Override
    public void onLoaderReset(Loader<List<New>> loader) {
        //clear the adapter
        mAdapter.clearAdapter(mNewsList);
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
