package com.example.baforecast.source;

import com.example.baforecast.model.json.WeatherRequest;

public interface WeatherDisplayable {
    void displayWeather(WeatherRequest request);
}
