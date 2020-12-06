package com.example.baforecast.observer;

import com.example.baforecast.City;

import java.util.ArrayList;
import java.util.List;


public class Publisher {
    private List<Observer> observers;

    public Publisher() {
        observers = new ArrayList<>();
    }

    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    public void notify(City cityParcel) {
        for (Observer observer : observers) {
            observer.selectCity(cityParcel);
        }
    }
}
