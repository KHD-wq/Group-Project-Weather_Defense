package com.example.project7

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextCity = findViewById<EditText>(R.id.editTextCity)
        val buttonLoad = findViewById<Button>(R.id.buttonLoad)

        buttonLoad.setOnClickListener {
            val city = editTextCity.text.toString()
            val intent = Intent(this, WeatherActivity::class.java).apply {
                putExtra("CITY", city)
            }
            startActivity(intent)
        }
    }
}
