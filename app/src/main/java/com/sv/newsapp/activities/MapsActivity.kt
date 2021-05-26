package com.sv.newsapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sv.newsapp.R
import com.sv.newsapp.data.WeatherData
import com.sv.newsapp.models.api.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MapsActivity : FragmentActivity(), OnMapReadyCallback, View.OnClickListener {
    private var mMap: GoogleMap? = null
    private val mApi = Api.Instance.getApi()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        findViewById<View>(R.id.moscow_button).setOnClickListener(this)
        findViewById<View>(R.id.spb_button).setOnClickListener(this)
        findViewById<View>(R.id.news_button).setOnClickListener(this)
    }

    @SuppressLint("CheckResult")
    private fun receiveWeather(city: String) {
        mApi.getWeatherDataByCity(
            city, "d3e76d9f4759e01c064d1b34ba9ff19e",
            "metric"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { weatherData: WeatherData -> showWeatherMarker(weatherData) }
    }

    private fun showWeatherMarker(weatherData: WeatherData) {
        val latLng = LatLng(weatherData.coord!!.lat.toDouble(), weatherData.coord!!.lon.toDouble())
        mMap!!.addMarker(
            MarkerOptions().position(latLng).title(
                "Temp: "
                        + weatherData.main!!.temp
            )
        ).showInfoWindow()
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_weather, menu)
        return true
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {}
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_weather) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.action_settings) {
//
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onClick(v: View) {
        receiveWeather((v as Button).text.toString())
    }
}