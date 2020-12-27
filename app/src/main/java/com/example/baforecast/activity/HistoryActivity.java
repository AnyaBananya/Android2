package com.example.baforecast.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.baforecast.App;
import com.example.baforecast.R;
import com.example.baforecast.adapter.HistoryRecyclerAdapter;
import com.example.baforecast.constant.Constants;
import com.example.baforecast.source.ForecastHistorySource;

public class HistoryActivity extends BaseActivity {

    private HistoryRecyclerAdapter adapter;
    private ForecastHistorySource historySource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_history) {
            historySource.deleteHistory();
            adapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_history);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        historySource = App
            .getInstance()
            .getForecastHistorySource();
        adapter = new HistoryRecyclerAdapter(historySource,this);

        recyclerView.setAdapter(adapter);
    }
}