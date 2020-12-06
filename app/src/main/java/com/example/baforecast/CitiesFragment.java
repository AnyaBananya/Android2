package com.example.baforecast;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.baforecast.observer.Publisher;
import com.example.baforecast.observer.PublisherGetter;

public class CitiesFragment extends Fragment {
    private static final String TAG = CitiesFragment.class.getSimpleName();
    private static final boolean DEBUG = false;
    private Publisher publisher;
    private City currentCity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) {
            Log.d(TAG, "onCreate()");
        }
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentCity = (City) savedInstanceState.getSerializable(Constants.EXTRA_PARCEL);
            if (DEBUG) {
                Log.d(TAG, currentCity.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        if (DEBUG) {
            Log.d(TAG, "onCreateView()");
        }

        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (DEBUG) {
            Log.d(TAG, "onViewCreated()");
        }
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (DEBUG) {
            Log.d(TAG, "onActivityCreated()");
        }
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            currentCity = (City) savedInstanceState.getSerializable(Constants.EXTRA_PARCEL);
        } else {
            String city = getResources().getStringArray(R.array.cities)[0];
            String temperature = getResources().getStringArray(R.array.temperatures)[0];
            String pressure = getResources().getStringArray(R.array.pressures)[0];
            String windSpeed = getResources().getStringArray(R.array.windSpeeds)[0];
            currentCity = new City(city, temperature, pressure, windSpeed);
        }
        if (DEBUG) {
            Log.d(TAG, currentCity.getName());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (DEBUG) {
            Log.d(TAG, "onSaveInstanceState()");
        }
        outState.putSerializable(Constants.EXTRA_PARCEL, currentCity);
        super.onSaveInstanceState(outState);
    }

    private void initList(View view) {
        LinearLayout linearLayout = (LinearLayout) view;
        String[] cities = getResources().getStringArray(R.array.cities);
        View[] views = new View[cities.length];
        LayoutInflater layoutInflater = getLayoutInflater();

        for (int i = 0; i < cities.length; i++) {
            String city = cities[i];
            View cityItem = layoutInflater.inflate(R.layout.item_city, linearLayout, false);
            TextView textView = cityItem.findViewById(R.id.cityTextView);
            textView.setText(city);
            linearLayout.addView(cityItem);
            views[i] = cityItem;
            int finalI = i;

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (View v : views) {
                        v.setBackgroundColor(Color.WHITE);
                    }
                    cityItem.setBackgroundColor(Color.YELLOW);
                    String temperature = getResources().getStringArray(R.array.temperatures)[finalI];
                    String pressure = getResources().getStringArray(R.array.pressures)[finalI];
                    String windSpeed = getResources().getStringArray(R.array.windSpeeds)[finalI];
                    currentCity = new City(city, temperature, pressure, windSpeed);
                    publisher.notify(currentCity);
                }
            });
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (DEBUG) {
            Log.d(TAG, "onAttach()");
        }
        super.onAttach(context);
        try {
            publisher = ((PublisherGetter) context).getPublisher();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PublisherGetter");
        }
    }
}