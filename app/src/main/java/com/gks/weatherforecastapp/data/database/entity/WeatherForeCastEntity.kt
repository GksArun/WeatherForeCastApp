package com.gks.weatherforecastapp.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "weatherforecast",
    foreignKeys = [ForeignKey(
        entity = CityEntity::class,
        parentColumns = ["id"],
        childColumns = ["cityId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class WeatherForeCastEntity(
    @PrimaryKey(autoGenerate = true) val weatherId: Long = 0,
    val cityId: Long,
    val date: Int,
    val weather: String,
    val icon: String,
    val minTemp: Double,
    val maxTemp: Double,
)
