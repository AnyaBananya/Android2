package com.example.baforecast.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.baforecast.model.City;
import com.example.baforecast.R;
import com.example.baforecast.adapter.SocnetAdapter;
import com.example.baforecast.constant.Constants;
import com.example.baforecast.model.WeatherRequest;
import com.example.baforecast.source.Source;
import com.example.baforecast.source.WeatherDisplayable;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, WeatherDisplayable {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final boolean DEBUG = false;
    private SharedPreferences sharedPreferences;

    private City city;
    private TextView txtViewCity;
    private TextView txtViewTemperature;
    private TextView txtViewPressure;
    private AppBarConfiguration mAppBarConfiguration;
    private Source retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
        initBackgound();

        txtViewCity = findViewById(R.id.text_city);
        txtViewTemperature = findViewById(R.id.text_curr_temp);
        txtViewPressure = findViewById(R.id.text_pressure);

        Intent intent = getIntent();
        if (intent.hasExtra("parcel")) {
            city = (City) intent.getSerializableExtra("parcel");
        } else {
            city = new City(getResources().getStringArray(R.array.cities)[0]);
        }

        System.out.println("city.getName() = " + city.getName());

        retrofit = new Source();
        retrofit.initRetorfit();
        connectAndFetch();

        txtViewCity.setText(city.getName());
        txtViewTemperature.setText(city.getTemperature());
        txtViewPressure.setText(city.getPressure());

        initRecyclerView(generateDays(), generateTemperatures());
    }

    private void initBackgound() {
        Picasso.get().load(getString(R.string.background_url)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                findViewById(R.id.main_layout).setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                showAllert("Background is not loaded");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private void initRecyclerView(String[] days, String[] temperatures) {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SocnetAdapter adapter = new SocnetAdapter(days, temperatures);
        recyclerView.setAdapter(adapter);
    }

    private String[] generateDays() {
        String[] days = new String[Constants.DAY_COUNT];
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM");
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < Constants.DAY_COUNT; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            days[i] = dateFormatter.format(calendar.getTime());
        }

        return days;
    }

    private String[] generateTemperatures() {
        String[] temps = new String[Constants.DAY_COUNT];
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < Constants.DAY_COUNT; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            temps[i] = "+ " + calendar.get(Calendar.DAY_OF_MONTH);
        }

        return temps;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SETTING_CODE) {
            recreate();
        }
    }

    @SuppressLint("DefaultLocale")
    public void displayWeather(WeatherRequest weatherRequest) {
        runOnUiThread(() -> {
            txtViewCity.setText(weatherRequest.getName());
            txtViewTemperature.setText(String.format("%.0f", weatherRequest.getMain().getTemp() - 273.15));
            txtViewPressure.setText(String.format("%.0f", weatherRequest.getMain().getPressure() / 1.333));

            if (city.isNeedPressure()) {
                TextView txtViewLabelPressure = (TextView) findViewById(R.id.label_pressure);
                txtViewLabelPressure.setVisibility(View.VISIBLE);
                txtViewPressure.setVisibility(View.VISIBLE);
            }
        });
    }

    private void connectAndFetch() {
        try {
            //source.connectAndFetch(city, this);
            retrofit.requestRetrofit(city, this);
        } catch (Exception e) {
            Log.e(TAG, "Fail connection", e);
            showAllert("Fail connection");
            e.printStackTrace();
        }
    }

    private void showAllert(String allertMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.alert_title)
            .setMessage(allertMsg)
            .setIcon(R.mipmap.ic_launcher_round)
            .setCancelable(false)
            .setPositiveButton(R.string.alert_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}