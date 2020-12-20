package com.example.baforecast.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baforecast.R;

public class SocnetAdapter extends RecyclerView.Adapter<SocnetAdapter.ViewHolder> {
    private final String[] days;
    private final String[] temperatures;

    public SocnetAdapter(String[] days, String[] temperatures) {
        this.days = days;
        this.temperatures = temperatures;
    }

    @NonNull
    @Override
    public SocnetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SocnetAdapter.ViewHolder holder, int position) {
        holder.getTextViewDay().setText(days[position]);
        holder.getTextViewTemp().setText(temperatures[position]);
    }

    @Override
    public int getItemCount() {

        Log.d("getItemCount", String.valueOf(days.length));
        return days.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewDay;
        private final TextView textViewTemp;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewDay = itemView.findViewById(R.id.dayTextView);
            textViewTemp = itemView.findViewById(R.id.tempTextView);
        }

        public TextView getTextViewDay() {
            return textViewDay;
        }
        public TextView getTextViewTemp() {
            return textViewTemp;
        }
    }
}
