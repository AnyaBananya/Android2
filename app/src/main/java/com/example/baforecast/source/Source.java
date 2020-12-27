package com.example.baforecast.source;

import android.util.Log;

import com.example.baforecast.BuildConfig;
import com.example.baforecast.constant.Constants;
import com.example.baforecast.model.City;
import com.example.baforecast.model.WeatherRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Source {
    private static final String TAG = Source.class.getSimpleName();
    private OpenWeather openWeather;

    public void initRetorfit() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

    public void requestRetrofit(City city, WeatherDisplayable displayable) {
        openWeather.loadWeather(city.getName(), BuildConfig.WEATHER_API_KEY)
            .enqueue(new Callback<WeatherRequest>() {
                @Override
                public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                    if (response.body() != null) {
                        displayable.displayWeather(response.body());
                    }
                }

                @Override
                public void onFailure(Call<WeatherRequest> call, Throwable t) {
                    Log.e(TAG, "Fail connection", t);
                    t.printStackTrace();
                }
            });
    }
}
