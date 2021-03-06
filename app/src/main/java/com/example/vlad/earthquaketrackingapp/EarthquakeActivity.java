package com.example.vlad.earthquaketrackingapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vlad.earthquaketrackingapp.databinding.EarthquakeItemBinding;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Earthquake>>{

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";
    public static final int LOADER_ID = 11;
    public static final String LOG_TAG = "EA";
    private EarthquakeAdapter adapter;
    private ProgressBar progressBar;
    private TextView twNoInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        progressBar = (ProgressBar) findViewById(R.id.pb);
        twNoInternet = (TextView) findViewById(R.id.tvInternetConnection);
        twNoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "twNoInternet clicked");
               loadData();
            }
        });

        RecyclerView rv = (RecyclerView) findViewById(R.id.rvQuakeList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EarthquakeAdapter();
        rv.setAdapter(adapter);

//        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
//        task.execute(USGS_REQUEST_URL);
//        Bundle bundle = new Bundle();
//        bundle.putString(LOG_TAG, USGS_REQUEST_URL);

        loadData();


    }

    private void loadData() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if(info != null && info.isConnected()) {
            getLoaderManager().initLoader(LOADER_ID, null,this);
            twNoInternet.setVisibility(View.GONE);
        } else {
            twNoInternet.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        Loader<List<Earthquake>> loader = null;
        if(id == LOADER_ID) {
            loader = new EarthquakeLoader(this, USGS_REQUEST_URL);
            Log.d(LOG_TAG, "onCreateLoader: " + loader.hashCode());
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        progressBar.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()) {
            adapter.setData(data);
        } else {
            twNoInternet.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {

    }

    class EarthquakeHolder extends RecyclerView.ViewHolder {

        private static final String LOCATION_SEPARATOR = " of ";
        EarthquakeItemBinding binding;

        public EarthquakeHolder(EarthquakeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Earthquake earthquake) {

            binding.setEarthquake(earthquake);
            binding.magnitude.setText(formatMagnitude(earthquake.getMagnitude()));

            Date date = new Date(earthquake.getTimeInMilliseconds());
            binding.date.setText(formatDate(date));
            binding.time.setText(formatTime(date));

            String primaryLocation, locationOffset;
            if (earthquake.getLocation().contains(LOCATION_SEPARATOR)) {
                String[] parts = earthquake.getLocation().split(LOCATION_SEPARATOR);
                locationOffset = parts[0] + LOCATION_SEPARATOR;
                primaryLocation = parts[1];
            } else {
                locationOffset = binding.getRoot().getContext().getString(R.string.near_the);
                primaryLocation = earthquake.getLocation();
            }
            binding.locationOffset.setText(locationOffset);
            binding.primaryLocation.setText(primaryLocation);


            GradientDrawable magnitudeCircle = (GradientDrawable) binding.magnitude.getBackground();
            int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());
            magnitudeCircle.setColor(magnitudeColor);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(earthquake.getUrl()));
                    startActivity(i);
                }
            });

            binding.executePendingBindings();
        }

        private int getMagnitudeColor(double magnitude) {
            int magnitudeColorResourceId;
            int magnitudeFloor = (int) Math.floor(magnitude);
            switch (magnitudeFloor) {
                case 0:
                case 1:
                    magnitudeColorResourceId = R.color.magnitude1;
                    break;
                case 2:
                    magnitudeColorResourceId = R.color.magnitude2;
                    break;
                case 3:
                    magnitudeColorResourceId = R.color.magnitude3;
                    break;
                case 4:
                    magnitudeColorResourceId = R.color.magnitude4;
                    break;
                case 5:
                    magnitudeColorResourceId = R.color.magnitude5;
                    break;
                case 6:
                    magnitudeColorResourceId = R.color.magnitude6;
                    break;
                case 7:
                    magnitudeColorResourceId = R.color.magnitude7;
                    break;
                case 8:
                    magnitudeColorResourceId = R.color.magnitude8;
                    break;
                case 9:
                    magnitudeColorResourceId = R.color.magnitude9;
                    break;
                default:
                    magnitudeColorResourceId = R.color.magnitude10plus;
                    break;
            }
            return ContextCompat.getColor(binding.getRoot().getContext(), magnitudeColorResourceId);
        }

        private String formatMagnitude(double magnitude) {
            DecimalFormat format = new DecimalFormat("0.0");
            return format.format(magnitude);
        }

        private String formatDate(Date date) {
            DateFormat format = new SimpleDateFormat("LLL dd, yyyy");
            return format.format(date);
        }

        private String formatTime(Date date) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            return timeFormat.format(date);
        }

    }

    class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeHolder> {

        private List<Earthquake> items = new LinkedList<>();

        public void setData(List<Earthquake> data) {
            items.clear();
            items.addAll(data);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public EarthquakeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            EarthquakeItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.earthquake_item, parent, false);
            return new EarthquakeHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull EarthquakeHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Earthquake> data) {
            if (data != null && !data.isEmpty()) {
                adapter.setData(data);
            }
        }
    }

   }
