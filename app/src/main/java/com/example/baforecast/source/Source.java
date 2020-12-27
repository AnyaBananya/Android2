package com.example.baforecast.source;

import android.util.Log;
import com.example.baforecast.App;
import com.example.baforecast.BuildConfig;
import com.example.baforecast.model.City;
import com.example.baforecast.model.WeatherRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Source {
    private static final String TAG = Source.class.getSimpleName();

    public void requestRetrofit(City city, WeatherDisplayable displayable) {
        App.getOpenWeatherApi().loadWeather(city.getName(), BuildConfig.WEATHER_API_KEY)
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
