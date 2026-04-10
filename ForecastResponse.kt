package com.moekyawaung.skycast.data.model

data class ForecastResponse(
    val list: List<ForecastItem>
)

data class ForecastItem(
    val dt_txt: String,
    val main: ForecastMain,
    val weather: List<WeatherData>
)

data class ForecastMain(
    val temp: Double
)
