package com.example.baforecast;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.baforecast.dao.ForecastHistoryDao;
import com.example.baforecast.database.ForecastHistoryDatabase;
import com.example.baforecast.source.ForecastHistorySource;
import com.example.baforecast.source.OpenWeather;

public class App extends Application {
    private static App INSTANCE;
    private static ApiHolder apiHolder;
    private ForecastHistoryDatabase db;
    private ForecastHistorySource historySource;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        apiHolder = new ApiHolder();

        connectToDb();
        initHistory();
    }

    public static OpenWeather getOpenWeatherApi(){
        return apiHolder.getOpenWeather();
    }

    public static Context getAppContext(){
        return INSTANCE;
    }

    public static App getInstance(){
        return INSTANCE;
    }

    private void connectToDb(){
        db = Room.databaseBuilder(getApplicationContext(), ForecastHistoryDatabase.class,"forecast_history_database")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build();
    }

    private void initHistory() {
        ForecastHistoryDao forecastHistoryDao = db.getForecastHistoryDao();

        historySource = new ForecastHistorySource(forecastHistoryDao);
    }

    public ForecastHistorySource getForecastHistorySource() {
        return historySource;
    }
}
