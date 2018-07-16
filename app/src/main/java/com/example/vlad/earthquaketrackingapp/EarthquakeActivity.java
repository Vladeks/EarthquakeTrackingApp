package com.example.vlad.earthquaketrackingapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vlad.earthquaketrackingapp.databinding.EarthquakeItemBinding;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);


        RecyclerView rv = (RecyclerView) findViewById(R.id.rvQuakeList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        EarthquakeAdapter adapter = new EarthquakeAdapter();
        adapter.setData(QueryUtils.extractEarthquakes());
        rv.setAdapter(adapter);

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

}
