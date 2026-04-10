package com.moekyawaung.skycast.ui.main

import androidx.lifecycle.*
import com.moekyawaung.skycast.data.model.AirPollutionResponse
import com.moekyawaung.skycast.data.model.ForecastResponse
import com.moekyawaung.skycast.data.model.WeatherResponse
import com.moekyawaung.skycast.data.repository.WeatherRepository
import com.moekyawaung.skycast.utils.Resource
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherState = MutableLiveData<Resource<WeatherResponse>>()
    val weatherState: LiveData<Resource<WeatherResponse>> = _weatherState

    private val _forecastState = MutableLiveData<Resource<ForecastResponse>>()
    val forecastState: LiveData<Resource<ForecastResponse>> = _forecastState

    private val _airState = MutableLiveData<Resource<AirPollutionResponse>>()
    val airState: LiveData<Resource<AirPollutionResponse>> = _airState

    fun fetchWeather(city: String, apiKey: String) {
        _weatherState.value = Resource.Loading

        viewModelScope.launch {
            try {
                val response = repository.getCurrentWeather(city, apiKey)
                if (response.isSuccessful && response.body() != null) {
                    _weatherState.value = Resource.Success(response.body()!!)
                } else {
                    _weatherState.value = Resource.Error("Failed to load weather")
                }
            } catch (e: Exception) {
                _weatherState.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchForecast(city: String, apiKey: String) {
        _forecastState.value = Resource.Loading

        viewModelScope.launch {
            try {
                val response = repository.getForecast(city, apiKey)
                if (response.isSuccessful && response.body() != null) {
                    _forecastState.value = Resource.Success(response.body()!!)
                } else {
                    _forecastState.value = Resource.Error("Failed to load forecast")
                }
            } catch (e: Exception) {
                _forecastState.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchAirPollution(lat: Double, lon: Double, apiKey: String) {
        _airState.value = Resource.Loading

        viewModelScope.launch {
            try {
                val response = repository.getAirPollution(lat, lon, apiKey)
                if (response.isSuccessful && response.body() != null) {
                    _airState.value = Resource.Success(response.body()!!)
                } else {
                    _airState.value = Resource.Error("Failed to load air quality")
                }
            } catch (e: Exception) {
                _airState.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }
}
