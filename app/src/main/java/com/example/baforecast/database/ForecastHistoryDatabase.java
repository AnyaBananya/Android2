package com.example.baforecast.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.baforecast.dao.ForecastHistoryDao;
import com.example.baforecast.model.db.ForecastHistory;

@Database(entities = {ForecastHistory.class}, version = 1)
public abstract class ForecastHistoryDatabase extends RoomDatabase {
    public abstract ForecastHistoryDao getForecastHistoryDao();
}
