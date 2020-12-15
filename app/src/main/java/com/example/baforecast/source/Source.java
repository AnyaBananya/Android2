package com.example.baforecast.source;

import android.os.Handler;
import android.util.Log;

import com.example.baforecast.BuildConfig;
import com.example.baforecast.activity.MainActivity;
import com.example.baforecast.constant.Constants;
import com.example.baforecast.model.City;
import com.example.baforecast.model.WeatherRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class Source {
    private static final String TAG = Source.class.getSimpleName();

    public void connectAndFetch(City city, WeatherDisplayable displayable) throws UnsupportedEncodingException, MalformedURLException {
        final URL uri;
        uri = new URL(
            String.format(Constants.WEATHER_URL, URLEncoder.encode(city.getName(), StandardCharsets.UTF_8.toString()), BuildConfig.WEATHER_API_KEY));

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
                            displayable.displayWeather(weatherRequest);
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Fail connection", e);
                    e.printStackTrace();
                } finally {
                    if (null != urlConnection) {
                        urlConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }
}
