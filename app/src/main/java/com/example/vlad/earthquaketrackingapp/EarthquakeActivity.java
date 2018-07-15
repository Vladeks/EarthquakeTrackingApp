package com.example.vlad.earthquaketrackingapp;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.vlad.earthquaketrackingapp.databinding.EarthquakeItemBinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        earthquakes.add(new Earthquake("7.2", "San Francisco", "Feb 2, 2016"));
        earthquakes.add(new Earthquake("6.1", "London", "July 20, 2015"));
        earthquakes.add(new Earthquake("3.9", "Tokyo", "Nov 10, 2014"));
        earthquakes.add(new Earthquake("5.4", "Mexico City", "May 3, 2014"));
        earthquakes.add(new Earthquake("2.8", "Moscow", "Jan 31, 2013"));
        earthquakes.add(new Earthquake("4.9", "Rio de Janeiro", "Aug 19, 2012"));
        earthquakes.add(new Earthquake("1.6", "Paris", "Oct 30, 2011"));
        RecyclerView rv = (RecyclerView) findViewById(R.id.rvQuakeList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        EarthquakeAdapter adapter = new EarthquakeAdapter();
        adapter.setData(earthquakes);
        rv.setAdapter(adapter);

    }

    class EarthquakeHolder extends RecyclerView.ViewHolder {

        EarthquakeItemBinding binding;

        public EarthquakeHolder(EarthquakeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Earthquake employee) {
            binding.setEarthquake(employee);
            binding.executePendingBindings();
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
