package com.example.baforecast.observer;

import com.example.baforecast.model.json.City;

public interface Observer {
    void selectCity(City cityParcel);
}
