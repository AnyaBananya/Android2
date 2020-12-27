package com.example.baforecast;

import com.example.baforecast.source.OpenWeather;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHolder {
    private final OpenWeather openWeather;

    public ApiHolder(){
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

    public OpenWeather getOpenWeather(){
        return openWeather;
    }

}
