package com.gks.weatherforecastapp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.gks.weatherforecastapp.data.database.entity.CityEntity
import com.gks.weatherforecastapp.data.database.entity.CityWithWeather
import com.gks.weatherforecastapp.data.database.entity.WeatherForeCastEntity

@Dao
interface WeatherForecastDao {

    @Upsert
    suspend fun insertCity(item: CityEntity)

    @Upsert
    suspend fun insertWeatherForeCastList(itemList: List<WeatherForeCastEntity>)

    @Query("SELECT name from city limit 1")
    suspend fun getLastFetchedCity() : String?

    @Query("SELECT * FROM city WHERE name = :cityName")
    suspend fun getAllWeatherForeCast(cityName: String): CityWithWeather?

}