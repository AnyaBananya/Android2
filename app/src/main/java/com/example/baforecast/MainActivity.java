package com.example.baforecast;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.baforecast.model.WeatherRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final boolean DEBUG = false;
    private static final int DAY_COUNT = 5;
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s,RU&appid=%s";
    private City city;
    private TextView txtViewCity;
    private TextView txtViewTemperature;
    private TextView txtViewPressure;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);

        txtViewCity = findViewById(R.id.text_city);
        txtViewTemperature = findViewById(R.id.text_curr_temp);
        txtViewPressure = findViewById(R.id.text_pressure);

        Intent intent = getIntent();
        if (intent.hasExtra("parcel")) {
            city = (City) intent.getSerializableExtra("parcel");
        } else {
            city = new City(getResources().getStringArray(R.array.cities)[0]);
        }

        connect();

        txtViewCity.setText(city.getName());
        txtViewTemperature.setText(city.getTemperature());
        txtViewPressure.setText(city.getPressure());

        initRecyclerView(generateDays(), generateTemperatures());

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
        String[] days = new String[DAY_COUNT];
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM");
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < DAY_COUNT; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            days[i] = dateFormatter.format(calendar.getTime());
        }

        return days;
    }

    private String[] generateTemperatures() {
        String[] temps = new String[DAY_COUNT];
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < DAY_COUNT; i++) {
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

    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

    @SuppressLint("DefaultLocale")
    private void displayWeather(WeatherRequest weatherRequest) {
        txtViewCity.setText(weatherRequest.getName());
        txtViewTemperature.setText(String.format("%.0f", weatherRequest.getMain().getTemp() - 273.15));
        txtViewPressure.setText(String.format("%.0f", weatherRequest.getMain().getPressure() / 1.333));

        if (city.isNeedPressure()) {
            TextView txtViewLabelPressure = (TextView) findViewById(R.id.label_pressure);
            txtViewLabelPressure.setVisibility(View.VISIBLE);
            txtViewPressure.setVisibility(View.VISIBLE);
        }
    }

    private void connect(){
        final URL uri;
        try {
            uri = new URL(String.format(WEATHER_URL, URLEncoder.encode(city.getName(), StandardCharsets.UTF_8.toString()), BuildConfig.WEATHER_API_KEY));

            final Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpsURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpsURLConnection) uri.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setReadTimeout(10000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String result = getLines(in);
                        Gson gson = new Gson();
                        final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                displayWeather(weatherRequest);
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Fail connection", e);
                        showAllert("Fail connection");
                        e.printStackTrace();
                    } finally {
                        if (null != urlConnection) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail URI", e);
            showAllert("Fail URI");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Fail encode URL", e);
            showAllert("Fail encode URL");
            e.printStackTrace();
        }
    }

    private void showAllert(String allertMsg){
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