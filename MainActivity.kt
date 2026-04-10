package com.moekyawaung.skycast.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.moekyawaung.skycast.R
import com.moekyawaung.skycast.data.remote.ApiClient
import com.moekyawaung.skycast.data.repository.WeatherRepository
import com.moekyawaung.skycast.utils.Resource
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var etCity: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnLang: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var tvCity: TextView
    private lateinit var tvTemp: TextView
    private lateinit var tvCondition: TextView
    private lateinit var tvFeelsLike: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvWind: TextView
    private lateinit var tvPressure: TextView
    private lateinit var tvAirQuality: TextView
    private lateinit var tvPm25: TextView
    private lateinit var tvPm10: TextView
    private lateinit var tvForecast: TextView
    private lateinit var tvError: TextView

    private lateinit var viewModel: WeatherViewModel

    private val apiKey = "YOUR_OPENWEATHER_API_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        setupViewModel()
        observeViewModel()

        btnSearch.setOnClickListener {
            val city = etCity.text.toString().trim()
            if (city.isNotEmpty()) {
                loadAllWeather(city)
            } else {
                tvError.visibility = View.VISIBLE
                tvError.text = getString(R.string.enter_city)
            }
        }

        btnLang.setOnClickListener {
            toggleLanguage()
        }

        loadAllWeather("Yangon")
    }

    private fun bindViews() {
        etCity = findViewById(R.id.etCity)
        btnSearch = findViewById(R.id.btnSearch)
        btnLang = findViewById(R.id.btnLang)
        progressBar = findViewById(R.id.progressBar)

        tvCity = findViewById(R.id.tvCity)
        tvTemp = findViewById(R.id.tvTemp)
        tvCondition = findViewById(R.id.tvCondition)
        tvFeelsLike = findViewById(R.id.tvFeelsLike)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvWind = findViewById(R.id.tvWind)
        tvPressure = findViewById(R.id.tvPressure)
        tvAirQuality = findViewById(R.id.tvAirQuality)
        tvPm25 = findViewById(R.id.tvPm25)
        tvPm10 = findViewById(R.id.tvPm10)
        tvForecast = findViewById(R.id.tvForecast)
        tvError = findViewById(R.id.tvError)
    }

    private fun setupViewModel() {
        val repository = WeatherRepository(ApiClient.apiService)
        val factory = WeatherViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]
    }

    private fun loadAllWeather(city: String) {
        tvError.visibility = View.GONE
        viewModel.fetchWeather(city, apiKey)
        viewModel.fetchForecast(city, apiKey)
    }

    private fun observeViewModel() {
        viewModel.weatherState.observe(this) { state ->
            when (state) {
                is Resource.Loading -> progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    val data = state.data
                    tvCity.text = data.name
                    tvTemp.text = "${data.main.temp.toInt()}°C"
                    tvCondition.text = data.weather.firstOrNull()?.description ?: "-"
                    tvFeelsLike.text = getString(R.string.feels_like_value, data.main.feels_like.toInt().toString())
                    tvHumidity.text = getString(R.string.humidity_value, data.main.humidity.toString())
                    tvWind.text = getString(R.string.wind_value, data.wind.speed.toString())
                    tvPressure.text = getString(R.string.pressure_value, data.main.pressure.toString())

                    viewModel.fetchAirPollution(data.coord.lat, data.coord.lon, apiKey)
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    tvError.visibility = View.VISIBLE
                    tvError.text = state.message
                }
            }
        }

        viewModel.forecastState.observe(this) { state ->
            when (state) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val builder = StringBuilder()
                    val items = state.data.list.take(5)
                    items.forEach {
                        val weatherMain = it.weather.firstOrNull()?.main ?: "-"
                        builder.append("${it.dt_txt} • ${it.main.temp.toInt()}°C • $weatherMain\n")
                    }
                    tvForecast.text = builder.toString()
                }
                is Resource.Error -> {
                    tvForecast.text = state.message
                }
            }
        }

        viewModel.airState.observe(this) { state ->
            when (state) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val item = state.data.list.firstOrNull()
                    if (item != null) {
                        val aqiLabel = when (item.main.aqi) {
                            1 -> getString(R.string.aqi_good)
                            2 -> getString(R.string.aqi_fair)
                            3 -> getString(R.string.aqi_moderate)
                            4 -> getString(R.string.aqi_poor)
                            5 -> getString(R.string.aqi_very_poor)
                            else -> getString(R.string.unknown)
                        }
                        tvAirQuality.text = getString(R.string.air_quality_value, aqiLabel)
                        tvPm25.text = getString(R.string.pm25_value, item.components.pm2_5.toString())
                        tvPm10.text = getString(R.string.pm10_value, item.components.pm10.toString())
                    }
                }
                is Resource.Error -> {
                    tvAirQuality.text = state.message
                }
            }
        }
    }

    private fun toggleLanguage() {
        val current = resources.configuration.locales[0].language
        val newLang = if (current == "my") "en" else "my"
        setLocale(newLang)
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate()
    }
}
