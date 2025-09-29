package com.gks.weatherforecastapp.domain.usecase.home

import com.gks.weatherforecastapp.data.database.entity.CityWithWeather
import com.gks.weatherforecastapp.domain.model.Resource
import com.gks.weatherforecastapp.domain.repository.WeatherForeCastRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeViewModelUseCase @Inject constructor(
    private val weatherForeCastRepository: WeatherForeCastRepository,
) {

    suspend fun getLastFetchedCity() = weatherForeCastRepository.getLastFetchedCity()

    fun getWeatherForeCast(cityName: String): Flow<Resource<CityWithWeather>> =
        weatherForeCastRepository.getWeatherForeCast(cityName)
}