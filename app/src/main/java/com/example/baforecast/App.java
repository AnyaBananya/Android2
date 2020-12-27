package com.example.baforecast;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.baforecast.dao.ForecastHistoryDao;
import com.example.baforecast.database.ForecastHistoryDatabase;
import com.example.baforecast.source.OpenWeather;

public class App extends Application {
    private static App INSTANCE;
    private static ApiHolder apiHolder;
    private ForecastHistoryDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        apiHolder = new ApiHolder();

        db = Room.databaseBuilder(getApplicationContext(), ForecastHistoryDatabase.class,"forecast_history_database")
            .allowMainThreadQueries()
            .build();

    }

    public static OpenWeather getOpenWeatherApi(){
        return apiHolder.getOpenWeather();
    }

    public Context getAppContext(){
        return INSTANCE;
    }

    public ForecastHistoryDao getForecastHistoryDao(){
        return db.getForecastHistoryDao();
    }
}
