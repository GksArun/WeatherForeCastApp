package com.gks.weatherforecastapp.domain.repository

import androidx.room.withTransaction
import com.gks.weatherforecastapp.data.database.WeatherForeCastDatabase
import com.gks.weatherforecastapp.data.database.dao.WeatherForecastDao
import com.gks.weatherforecastapp.data.database.entity.CityEntity
import com.gks.weatherforecastapp.data.database.entity.CityWithWeather
import com.gks.weatherforecastapp.data.database.entity.WeatherForeCastEntity
import com.gks.weatherforecastapp.data.network.WeatherForeCastApi
import com.gks.weatherforecastapp.domain.model.Resource
import com.gks.weatherforecastapp.domain.model.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherForeCastRepository @Inject constructor(
    private val api: WeatherForeCastApi,
    private val db: WeatherForeCastDatabase,
    private val dao: WeatherForecastDao,
) {

    suspend fun getLastFetchedCity(): String? {
        return dao.getLastFetchedCity()
    }

    fun getWeatherForeCast(cityName: String): Flow<Resource<CityWithWeather>> {
        return flow {
            toResource { api.getWeatherForeCast(cityName) }.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val response = resource.value
                        val cityEntity = CityEntity(
                            id = response.city.id,
                            name = response.city.name,
                            country = response.city.country
                        )
                        val weatherForeCastEntityList = response.list.map {
                            WeatherForeCastEntity(
                                cityId = response.city.id,
                                date = it.dt,
                                weather = it.weather[0].main,
                                icon = it.weather[0].icon,
                                minTemp = it.temp.min,
                                maxTemp = it.temp.max
                            )
                        }

                        clearDatabase()
                        dao.insertCity(cityEntity)
                        dao.insertWeatherForeCastList(weatherForeCastEntityList)

                        val forecast = dao.getAllWeatherForeCast(response.city.name)
                        if (forecast != null) {
                            emit(Resource.Success(forecast))
                        } else {
                            emit(Resource.Error("No forecast data found"))
                        }
                    }

                    is Resource.Error -> {
                        emit(Resource.Error(resource.message))
                    }

                    is Resource.Loading -> emit(Resource.Loading)
                }
            }
        }
    }

    private suspend fun clearDatabase() {
        db.withTransaction {
            db.clearAllTables()
        }
    }
}