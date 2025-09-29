package com.gks.weatherforecastapp.domain.model

data class WeatherForeCastApiResponse(
    val city: City,
    val list: List<WeatherItem>,
)

data class City(
    val id: Long,
    val name: String,
    val country: String,
)

data class WeatherItem(
    val dt: Int,
    val temp: Temp,
    val weather: List<WeatherObject>,
)

data class Temp(
    val max: Double,
    val min: Double,
)

data class WeatherObject(
    val main: String,
    val icon: String,
)
