package com.example.baforecast.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baforecast.R;
import com.example.baforecast.constant.Constants;
import com.example.baforecast.model.db.ForecastHistory;
import com.example.baforecast.source.ForecastHistorySource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {

    private Activity activity;
    private ForecastHistorySource dataSource;
    private long menuPosition;

    public HistoryRecyclerAdapter(ForecastHistorySource dataSource, Activity activity) {
        this.dataSource = dataSource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_history, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<ForecastHistory> historyList = dataSource.getHistory();
        ForecastHistory forecastHistory = historyList.get(position);

        holder.city.setText(forecastHistory.city);
        holder.temperature.setText(String.format("%.0f", forecastHistory.temperature - Constants.ABSOLUTE_ZERO));
        Date date = new Date(forecastHistory.date * 1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+3"));
        holder.date.setText(sdf.format(date));

        holder.cardView.setOnLongClickListener(view -> {
            menuPosition = position;
            return false;
        });

        if (activity != null) {
            activity.registerForContextMenu(holder.cardView);
        }
    }

    @Override
    public int getItemCount() {
        return (int) dataSource.getCountHistory();
    }

    public long getMenuPosition() {
        return menuPosition;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView city;
        TextView date;
        TextView temperature;
        View cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            city = cardView.findViewById(R.id.text_city_name);
            date = cardView.findViewById(R.id.text_date);
            temperature = cardView.findViewById(R.id.text_temperature);
        }
    }
}
