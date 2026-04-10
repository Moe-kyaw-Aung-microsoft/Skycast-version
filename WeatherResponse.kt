package com.moekyawaung.skycast.data.model

data class WeatherResponse(
    val name: String,
    val coord: Coord,
    val main: MainData,
    val weather: List<WeatherData>,
    val wind: WindData
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class MainData(
    val temp: Double,
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int
)

data class WeatherData(
    val main: String,
    val description: String,
    val icon: String
)

data class WindData(
    val speed: Double
)
