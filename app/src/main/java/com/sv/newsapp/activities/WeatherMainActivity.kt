package com.sv.newsapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sv.newsapp.R
import com.sv.newsapp.utils.Utils
import org.json.JSONObject


class WeatherMainActivity : AppCompatActivity() {

    // weather url to get JSON
    private var weather_url1 = ""

    // api id for url
    private var api_id1 = "a51cd2648ecc4b2cbea36c7ef8c9376b"

    private lateinit var tvTemp: TextView
    private lateinit var tvCity: TextView
    private lateinit var tvSunset: TextView
    private lateinit var tvSunrise: TextView
    private lateinit var ivWeather: ImageView
    private lateinit var tv1: TextView
    private lateinit var tv2: TextView
    private lateinit var tv3: TextView
    private lateinit var tv4: TextView
    private lateinit var btVar1: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_main)

        // link the textView in which the
        // temperature will be displayed
        tv1 = findViewById(R.id.tv1)
        tv2 = findViewById(R.id.tv2)
        tv3 = findViewById(R.id.tv3)
        tv4 = findViewById(R.id.tv4)
        ivWeather = findViewById(R.id.ivWeather)
        tvTemp = findViewById(R.id.tvTemp)
        tvCity = findViewById(R.id.tvCity)
        tvSunset = findViewById(R.id.tvSunset)
        tvSunrise = findViewById(R.id.tvSunrise)
        btVar1 = findViewById(R.id.btVar1)

        // create an instance of the Fused
        // Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.e("lat", weather_url1)

        // on clicking this button function to
        // get the coordinates will be called
        btVar1.setOnClickListener {
            Log.e("lat", "onClick")
            // function to find the coordinates
            // of the last location
            checkPermission()
            setTextViewVisible()
            obtainLocation()
        }
    }

    private fun setTextViewVisible() {
        tvTemp.visibility = View.VISIBLE
        tvCity.visibility = View.VISIBLE
        tvSunset.visibility = View.VISIBLE
        tvSunrise.visibility = View.VISIBLE
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) { //Can add more as per requirement
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                    123)
        }
    }

    @SuppressLint("MissingPermission")
    private fun obtainLocation() {
        Log.e("lat", "function")
        // get the last location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // get the latitude and longitude
                // and create the http URL
                weather_url1 = "https://api.weatherbit.io/v2.0/current?" + "lat="+
                        location?.latitude + "&lon=" + location?.longitude + "&key=" + api_id1
                Log.e("lat", weather_url1)
                // this function will
                // fetch data from URL
                getWeather()
            }
    }

    fun getWeather() {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = weather_url1
        Log.e("lat", url)
        // Request a string response from the provided URL.
        val stringReq = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    Log.e("lat", response.toString())
                    //get the JSON object
                    val obj = JSONObject(response)
                    //get the Array from obj of name - "data"
                    val arr = obj.getJSONArray("data")
                    //get the JSON object from the array at index position 0
                    val obj2 = arr.getJSONObject(0)
                    Log.e("lat obj2", obj2.toString())

                    val weatherArray = obj2.getJSONObject("weather")
                    Log.e("weather", weatherArray.toString())
                    val weatherArrayIcon = weatherArray.getString("icon")
                    tvTemp.text = obj2.getString("temp")
                    tvCity.text = obj2.getString("city_name")
                    tvSunset.text = obj2.getString("sunset")
                    tvSunrise.text = obj2.getString("sunrise")
                    val requestOptions = RequestOptions()
                    requestOptions.error(Utils.randomDrawbleColor)
                    Glide.with(this)
                            .load(weatherArrayIcon)
//                            .apply(requestOptions)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(ivWeather)
                },
                //In case of any error
                Response.ErrorListener { tvTemp!!.text = "That didn't work!" })
        queue.add(stringReq)
    }
}
