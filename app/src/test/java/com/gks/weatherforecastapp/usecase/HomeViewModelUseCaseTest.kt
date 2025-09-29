package com.gks.weatherforecastapp.usecase

import com.gks.weatherforecastapp.data.database.entity.CityEntity
import com.gks.weatherforecastapp.data.database.entity.CityWithWeather
import com.gks.weatherforecastapp.data.database.entity.WeatherForeCastEntity
import com.gks.weatherforecastapp.domain.model.Resource
import com.gks.weatherforecastapp.domain.repository.WeatherForeCastRepository
import com.gks.weatherforecastapp.domain.usecase.home.HomeViewModelUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelUseCaseTest {

    private val cityName = "Bangalore"
    private lateinit var repository: WeatherForeCastRepository
    private lateinit var homeViewModelUseCase: HomeViewModelUseCase

    @Before
    fun setup() {
        repository = mockk()
        homeViewModelUseCase = HomeViewModelUseCase(repository)
    }

    @Test
    fun getWeatherForeCastSuccess() = runTest {
        val expectedData = CityWithWeather(
            city = CityEntity(1, cityName, "IN"),
            weatherList = listOf(
                WeatherForeCastEntity(
                    1,
                    12L,
                    1662030000,
                    "Rain",
                    "https://openweathermap.org/img/wn/10d.png",
                    28.06,
                    29.38
                )
            )
        )

        coEvery { repository.getWeatherForeCast(cityName) } returns flow {
            emit(Resource.Success(expectedData))
        }

        val result = homeViewModelUseCase.getWeatherForeCast(cityName).toList()
        assertEquals(
            listOf(Resource.Success(expectedData)),
            result
        )

    }

    @Test
    fun getWeatherForeCastError() = runTest {
        val errorMessage = "City not found"

        coEvery { repository.getWeatherForeCast(cityName) } returns flow {
            emit(Resource.Error(errorMessage))
        }

        val result = homeViewModelUseCase.getWeatherForeCast(cityName).toList()
        assertEquals(
            listOf(Resource.Error(errorMessage)),
            result
        )
    }
}
