package com.example.baforecast.activity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.baforecast.receiver.BatteryReceiver;
import com.example.baforecast.model.json.City;
import com.example.baforecast.R;
import com.example.baforecast.constant.Constants;
import com.example.baforecast.receiver.NetworkReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String NameSharedPreference = "LOGIN";
    private static final String IsDarkTheme = "IS_DARK_THEME";
    private BroadcastReceiver batteryReceiver = new BatteryReceiver();
    private BroadcastReceiver networkReceiver = new NetworkReceiver();
    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isDarkTheme()){
            setTheme(R.style.AppDarkTheme);
        }else{
            setTheme(R.style.Theme_BaForecast_NoActionBar);
        }

        Intent intent = getIntent();
        if (intent.hasExtra("parcel")) {
            city = (City) intent.getSerializableExtra("parcel");
        } else {
            city = new City(getResources().getStringArray(R.array.cities)[0]);
        }
        setContentView(R.layout.activity_base);

        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
        registerReceiver(networkReceiver, new IntentFilter(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION));

        initNotificationChannel();
        initGetToken();
    }

    private void initGetToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (!task.isSuccessful()) {
                        Log.w("PushMessage", "getInstanceId failed", task.getException());
                        return;
                    }
                }
            });

    }

    protected boolean isDarkTheme() {
        SharedPreferences sharedPref = getSharedPreferences(NameSharedPreference, MODE_PRIVATE);
        return sharedPref.getBoolean(IsDarkTheme, true);
    }

    protected void setDarkTheme(boolean isDarkTheme) {
        SharedPreferences sharedPref = getSharedPreferences(NameSharedPreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(IsDarkTheme, isDarkTheme);
        editor.apply();
    }

    protected Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }


    protected void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                intentMain.putExtra(Constants.EXTRA_PARCEL, city);
                startActivity(intentMain);
                break;
            case R.id.nav_select_city:
                Intent intentSelectAct = new Intent(getApplicationContext(), SelectCityActivity.class);
                intentSelectAct.putExtra(Constants.EXTRA_PARCEL, city);
                startActivity(intentSelectAct);
                break;
            case R.id.nav_history:
                Intent intentHistory = new Intent(getApplicationContext(), HistoryActivity.class);
                intentHistory.putExtra(Constants.EXTRA_PARCEL, city);
                startActivity(intentHistory);
                break;
            case R.id.nav_about:
                Toast toast = new Toast(getApplicationContext());
                toast.setText(R.string.aboutText);
                toast.show();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivityForResult(intent, Constants.SETTING_CODE);
            return true;
        }

        if (id == R.id.action_select_city) {
            Intent intent = new Intent(getApplicationContext(), SelectCityActivity.class);
            intent.putExtra(Constants.EXTRA_PARCEL, city);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryReceiver);
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("2", "name", importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

}