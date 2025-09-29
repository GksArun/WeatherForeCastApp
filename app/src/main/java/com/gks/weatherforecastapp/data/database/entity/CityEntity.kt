package com.gks.weatherforecastapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class CityEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val country: String,
)