package com.moekyawaung.skycast.data.repository

import com.moekyawaung.skycast.data.model.AirPollutionResponse
import com.moekyawaung.skycast.data.model.ForecastResponse
import com.moekyawaung.skycast.data.model.WeatherResponse
import com.moekyawaung.skycast.data.remote.WeatherApiService
import retrofit2.Response

class WeatherRepository(private val apiService: WeatherApiService) {

    suspend fun getCurrentWeather(city: String, apiKey: String): Response<WeatherResponse> {
        return apiService.getCurrentWeather(city, apiKey)
    }

    suspend fun getForecast(city: String, apiKey: String): Response<ForecastResponse> {
        return apiService.getForecast(city, apiKey)
    }

    suspend fun getAirPollution(lat: Double, lon: Double, apiKey: String): Response<AirPollutionResponse> {
        return apiService.getAirPollution(lat, lon, apiKey)
    }
}
