package com.example.baforecast;

import android.app.Application;
import android.content.Context;
import com.example.baforecast.source.OpenWeather;

public class App extends Application {
    private static App INSTANCE;
    private static ApiHolder apiHolder;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        apiHolder = new ApiHolder();
    }

    public static OpenWeather getOpenWeatherApi(){
        return apiHolder.getOpenWeather();
    }

    public Context getAppContext(){
        return INSTANCE;
    }
}
