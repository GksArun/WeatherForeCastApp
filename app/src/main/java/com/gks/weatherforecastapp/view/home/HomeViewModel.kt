package com.gks.weatherforecastapp.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gks.weatherforecastapp.data.database.entity.CityWithWeather
import com.gks.weatherforecastapp.data.database.entity.WeatherForeCastEntity
import com.gks.weatherforecastapp.domain.model.Resource
import com.gks.weatherforecastapp.domain.usecase.home.HomeViewModelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeViewModelUseCase: HomeViewModelUseCase,
) : ViewModel() {

    val state: StateFlow<HomeState>
        get() = _state
    private val _state = MutableStateFlow<HomeState>(HomeState.NotStarted)

    fun getLastFetchedCity() {
        viewModelScope.launch {
            homeViewModelUseCase.getLastFetchedCity()?.let {
                getWeatherDetailsForTheCity(it)
            }
        }
    }

    fun getWeatherDetailsForTheCity(city: String) {
        viewModelScope.launch {
            homeViewModelUseCase.getWeatherForeCast(city).collect {
                when (it) {
                    is Resource.Error -> {
                        _state.value = HomeState.Error(it.message)
                    }

                    is Resource.Loading -> {
                        _state.value = HomeState.Loading
                    }

                    is Resource.Success -> {
                        _state.value = HomeState.Success(it.value)
                    }
                }
            }
        }
    }
}

sealed class HomeState {

    object NotStarted : HomeState()

    object Loading : HomeState()

    data class Success(
        val cityWithWeather: CityWithWeather,
    ) : HomeState()

    data class Error(
        val message: String,
    ) : HomeState()

    fun getWeatherListOrEmpty(): List<WeatherForeCastEntity> = when (this) {
        is Success -> this.cityWithWeather.weatherList
        else -> emptyList()
    }
}