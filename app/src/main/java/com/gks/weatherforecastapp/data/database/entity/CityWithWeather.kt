package com.gks.weatherforecastapp.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CityWithWeather(
    @Embedded val city: CityEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "cityId"
    )
    val weatherList: List<WeatherForeCastEntity>,
)
