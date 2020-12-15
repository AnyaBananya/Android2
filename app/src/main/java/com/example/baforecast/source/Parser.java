package com.example.baforecast.source;

import com.example.baforecast.model.WeatherRequest;
import com.google.gson.Gson;

public class Parser {
    public void parse(String result, WeatherDisplayable displayable){
        new Thread(() -> {
            Gson gson = new Gson();
            final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
            displayable.displayWeather(weatherRequest);
        }).start();
    }
}
