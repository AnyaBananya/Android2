package com.example.baforecast.source;

import com.example.baforecast.dao.ForecastHistoryDao;
import com.example.baforecast.model.db.ForecastHistory;

import java.util.List;

public class ForecastHistorySource {
    private final ForecastHistoryDao forecastHistoryDao;
    private List<ForecastHistory> historyList;

    public ForecastHistorySource(ForecastHistoryDao forecastHistoryDao) {
        this.forecastHistoryDao = forecastHistoryDao;
    }

    public List<ForecastHistory> getHistory() {
        if (historyList == null) {
            loadHistory();
        }
        return this.historyList;
    }

    private void loadHistory() {
        historyList = forecastHistoryDao.getAllHistory();
    }

    public long getCountHistory() {
        return forecastHistoryDao.getCountHistory();
    }

    public void addHistory(ForecastHistory forecastHistory) {
        forecastHistoryDao.insertHistory(forecastHistory);
        loadHistory();
    }

    public void updateHistory(ForecastHistory forecastHistory) {
        forecastHistoryDao.updateHistory(forecastHistory);
        loadHistory();
    }

    public void removeHistory(ForecastHistory forecastHistory) {
        forecastHistoryDao.deleteHistory(forecastHistory);
        loadHistory();
    }

    public void deleteHistory() {
        forecastHistoryDao.deleteAllHistory();
        loadHistory();
    }
}
