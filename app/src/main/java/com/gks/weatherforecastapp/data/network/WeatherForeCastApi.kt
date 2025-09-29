package com.gks.weatherforecastapp.data.network

import com.gks.weatherforecastapp.BuildConfig
import com.gks.weatherforecastapp.domain.model.WeatherForeCastApiResponse
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WeatherForeCastApi {

    @GET(value = "data/2.5/forecast/daily")
    suspend fun getWeatherForeCast(
        @Query("q") query: String,
        @Query("cnt") count: Int = 3,
        @Query("units") unit: String = "metric",
        @Query("appid") appid: String = BuildConfig.API_KEY
    ): WeatherForeCastApiResponse

}