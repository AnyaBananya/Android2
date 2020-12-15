package com.example.baforecast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.baforecast.observer.Observer;
import com.example.baforecast.observer.Publisher;
import com.example.baforecast.observer.PublisherGetter;
import com.google.android.material.snackbar.Snackbar;


public class SelectCityActivity extends BaseActivity implements View.OnClickListener, PublisherGetter, Observer {
    private static final String TAG = SelectCityActivity.class.getSimpleName();
    private static final boolean DEBUG = false;
    private final Publisher publisher = new Publisher();

    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (DEBUG) {
            Log.d(TAG, "onCreate()");
            Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
        }

        publisher.subscribe(this);
        setContentView(R.layout.activity_select_city);
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);

        Intent intent = getIntent();

        if (savedInstanceState != null ) {
            city = (City) intent.getSerializableExtra(Constants.EXTRA_PARCEL);
        } else {
            city = new City(getResources().getStringArray(R.array.cities)[0], getResources().getStringArray(R.array.temperatures)[0],
                getResources().getStringArray(R.array.pressures)[0], getResources().getStringArray(R.array.windSpeeds)[0]);
        }

        CheckBox cbNeedPressure = (CheckBox) findViewById(R.id.checkBoxPressure);
        cbNeedPressure.setOnCheckedChangeListener((compoundButton, b) -> {
            city.setNeedPressure(cbNeedPressure.isChecked());
            if(cbNeedPressure.isChecked()){
                Snackbar.make(compoundButton, getResources().getString(R.string.msg_pressure), Snackbar.LENGTH_LONG).show();
            }
        });

        Button selectCity = (Button) findViewById(R.id.buttonSelectCity);
        selectCity.setOnClickListener(this);
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
    public void onClick(View v) {
        if (DEBUG) {
            Log.d(TAG, "onClick()" + city.getName());
        }

        if (v.getId() == R.id.buttonSelectCity) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(Constants.EXTRA_PARCEL, city);
            startActivity(intent);
        }
    }

    @Override
    public Publisher getPublisher() {
        return publisher;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (DEBUG) {
            Log.d(TAG, "onStart() 1" + city.getName());
        }

        Fragment citiesFragment  = getSupportFragmentManager().findFragmentById(R.id.cities);
        if (citiesFragment.getArguments()!=null){
            city = (City) citiesFragment.getArguments().getSerializable(Constants.EXTRA_PARCEL);
        }
        if (DEBUG) {
            Log.d(TAG, "onStart() 2" + city.getName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DEBUG) {
            Log.d(TAG, "onResume()" + city.getName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (DEBUG) {
            Log.d(TAG, "onPause()" + city.getName());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (DEBUG) {
            Log.d(TAG, "onStop()" + city.getName());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (DEBUG) {
            Log.d(TAG, "onRestart()" + city.getName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (DEBUG) {
            Log.d(TAG, "onDestroy()" + city.getName());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (DEBUG) {
            Log.d(TAG, "onRestoreInsSt()" + city.getName());
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("parcel", city);
        super.onSaveInstanceState(outState);
        if (DEBUG) {
            Log.d(TAG, "onSaveInsSt()" + city.getName());
        }
    }

    @Override
    public void selectCity(City cityParcel) {
        if (DEBUG) {
            Log.d(TAG, "selectCity(" + cityParcel.getName() + ")");
        }
        city = cityParcel;
    }
}