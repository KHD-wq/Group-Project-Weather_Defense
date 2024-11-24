package com.example.project7

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.openweathermap.org/data/2.5/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService: WeatherApiService = retrofit.create(WeatherApiService::class.java)



class WeatherActivity : AppCompatActivity() {
    private val apiKey = "ff96fd24c77162ff71e919d358a32224"
    private lateinit var textViewWeather: TextView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val webViewWeather = findViewById<WebView>(R.id.webViewWeather)
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        textViewWeather = findViewById(R.id.textViewWeather)
        val city = intent.getStringExtra("CITY") ?: "Baltimore,MD"

        webViewWeather.webViewClient = WebViewClient()
        val webSettings: WebSettings = webViewWeather.settings
        webSettings.javaScriptEnabled = true

        getWeatherForCity(city)

        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun getWeatherForCity(city: String) {
        val call = apiService.getCurrentWeather(city, apiKey)
        call.enqueue(object : Callback<WeatherResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    if (weatherResponse != null) {
                        // Update UI with the weather data
                        textViewWeather.text = "City: ${weatherResponse.name}\n" +
                                "Temperature: ${weatherResponse.main.temp}°C\n" +
                                "Description: ${weatherResponse.weather[0].description}\n" +
                                "Humidity: ${weatherResponse.main.humidity}%\n" +
                                "Pressure: ${weatherResponse.main.pressure} hPa"
                    } else {
                        Log.d("WeatherActivity", "Response body is null")
                    }
                } else {
                    Log.d("WeatherActivity", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("WeatherActivity", "Failed to get weather data: ${t.message}")
            }
        })
    }
}
