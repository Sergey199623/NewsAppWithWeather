package com.sv.newsapp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.sv.newsapp.R;
import com.sv.newsapp.data.WeatherData;
import com.sv.newsapp.models.api.Api;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;

    private final Api mApi = Api.Instance.getApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.moscow_button).setOnClickListener(this);
        findViewById(R.id.spb_button).setOnClickListener(this);
    }

    @SuppressLint("CheckResult")
    private void receiveWeather(final String city) {
        mApi.getWeatherDataByCity(city, "d3e76d9f4759e01c064d1b34ba9ff19e",
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherData -> showWeatherMarker(weatherData));
    }

    private void showWeatherMarker(WeatherData weatherData) {
        LatLng latLng = new LatLng(weatherData.getCoord().getLat(), weatherData.getCoord().getLon());
        mMap.addMarker(new MarkerOptions().position(latLng).title("Temp: "
                + weatherData.getMain().getTemp())).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onClick(View v) {
        receiveWeather(((Button)v).getText().toString());
    }
}