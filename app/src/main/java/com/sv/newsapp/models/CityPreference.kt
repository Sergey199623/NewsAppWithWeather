package com.sv.newsapp.models

import android.app.Activity
import android.content.SharedPreferences

class CityPreference(activity: Activity) {
    var prefs: SharedPreferences

    // If the user has not chosen a city yet, return
    // Sydney as the default city
    var city: String?
        get() = prefs.getString("city", "Sydney, AU")
        set(city) {
            prefs.edit().putString("city", city).apply()
        }

    init {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE)
    }
}