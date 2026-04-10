package com.moekyawaung.skycast.data.model

data class AirPollutionResponse(
    val list: List<AirItem>
)

data class AirItem(
    val main: AirMain,
    val components: AirComponents
)

data class AirMain(
    val aqi: Int
)

data class AirComponents(
    val pm2_5: Double,
    val pm10: Double,
    val co: Double,
    val no2: Double,
    val o3: Double,
    val so2: Double
)
