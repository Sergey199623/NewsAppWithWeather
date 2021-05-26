package com.sv.newsapp.activities

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sv.newsapp.R
import com.sv.newsapp.fragments.WeatherFragment
import com.sv.newsapp.models.CityPreference

class WeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, WeatherFragment())
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.change_city) {
            showInputDialog()
        }
        return false
    }

    private fun showInputDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Change city")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("Go") { dialog: DialogInterface?, which: Int -> changeCity(input.text.toString()) }
        builder.show()
    }

    fun changeCity(city: String?) {
        val wf = supportFragmentManager
            .findFragmentById(R.id.container) as WeatherFragment?
        wf!!.changeCity(city)
        CityPreference(this).city = city
    }
}