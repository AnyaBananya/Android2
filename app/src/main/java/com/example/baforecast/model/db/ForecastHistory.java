package com.example.baforecast.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"city", "date", "temperature"})})
public class ForecastHistory {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "city")
    public String city;

    @ColumnInfo(name = "date")
    public int date;

    @ColumnInfo(name = "temperature")
    public long temperature;
}
