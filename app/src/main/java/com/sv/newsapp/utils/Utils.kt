package com.sv.newsapp.utils

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    @JvmStatic
    var vibrantLightColorList = arrayOf(
            ColorDrawable(Color.parseColor("#ffeead")),
            ColorDrawable(Color.parseColor("#93cfb3")),
            ColorDrawable(Color.parseColor("#fd7a7a")),
            ColorDrawable(Color.parseColor("#faca5f")),
            ColorDrawable(Color.parseColor("#1ba798")),
            ColorDrawable(Color.parseColor("#6aa9ae")),
            ColorDrawable(Color.parseColor("#ffbf27")),
            ColorDrawable(Color.parseColor("#d93947"))
    )

    @JvmStatic
    val randomDrawbleColor: ColorDrawable
        get() {
            val idx = Random().nextInt(vibrantLightColorList.size)
            return vibrantLightColorList[idx]
        }

    @JvmStatic
    fun DateToTimeFormat(oldstringDate: String?): String? {
        val p = PrettyTime(Locale(country))
        var isTime: String? = null
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.ENGLISH)
            val date = sdf.parse(oldstringDate)
            isTime = p.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return isTime
    }

    @JvmStatic
    fun DateFormat(oldstringDate: String?): String? {
        val newDate: String?
        val dateFormat = SimpleDateFormat("E, d MMM yyyy", Locale(country))
        newDate = try {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldstringDate)
            dateFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            oldstringDate
        }
        return newDate
    }

    @JvmStatic
    fun DateFormatEurope(oldstringDate: String?): String? {
        val newDate: String?
        val dateFormat = SimpleDateFormat("E, d MMM yyyy", Locale.ENGLISH)
        newDate = try {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldstringDate)
            dateFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            oldstringDate
        }
        return newDate
    }

    @JvmStatic
    val europe: String
        get() {
            val locale = Locale("en")
            Locale.setDefault(locale)
            val configuration = Configuration()
            configuration.locale = locale
            val  country = locale.country.toString()
            return country.toLowerCase()
    }

    @JvmStatic
    val language: String
        get() {
            val locale = Locale.getDefault()
            return locale.language.toString()
        }

    @JvmStatic
    val country: String
        get() {
            val locale = Locale.getDefault()
            val country = locale.country.toString()
            return country.toLowerCase()
        }
}