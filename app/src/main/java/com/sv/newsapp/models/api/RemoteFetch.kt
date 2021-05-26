package com.sv.newsapp.models.api

import android.content.Context
import com.sv.newsapp.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object RemoteFetch {
    private const val OPEN_WEATHER_MAP_API =
        "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric"

    @JvmStatic
    fun getJSON(context: Context, city: String?): JSONObject? {
        return try {
            val url = URL(String.format(OPEN_WEATHER_MAP_API, city))
            val connection = url.openConnection() as HttpURLConnection
            connection.addRequestProperty(
                "x-api-key",
                context.getString(R.string.open_weather_maps_app_id)
            )
            val reader = BufferedReader(
                InputStreamReader(connection.inputStream)
            )
            val json = StringBuffer(1024)
            var tmp: String? = ""
            while (reader.readLine().also { tmp = it } != null) json.append(tmp).append("\n")
            reader.close()
            val data = JSONObject(json.toString())

            // This value will be 404 if the request was not
            // successful
            if (data.getInt("cod") != 200) {
                null
            } else data
        } catch (e: Exception) {
            null
        }
    }
}