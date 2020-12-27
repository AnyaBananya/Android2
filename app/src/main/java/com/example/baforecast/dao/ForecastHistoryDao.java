package com.example.baforecast.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.baforecast.model.db.ForecastHistory;

import java.util.List;

@Dao
public interface ForecastHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(ForecastHistory forecastHistory);

    @Update
    void updateHistory(ForecastHistory forecastHistory);

    @Delete
    void deleteHistory(ForecastHistory forecastHistory);

    @Query("DELETE FROM forecastHistory")
    void deleteAllHistory();

    @Query("DELETE FROM forecastHistory WHERE id = :id")
    void deleteHistoryById(long id);

    @Query("SELECT * FROM forecastHistory")
    List<ForecastHistory> getAllHistory();

    @Query("SELECT * FROM forecastHistory WHERE id = :id")
    ForecastHistory getHistoryById(long id);

    @Query("SELECT COUNT() FROM forecastHistory")
    long getCountHistory();

}
