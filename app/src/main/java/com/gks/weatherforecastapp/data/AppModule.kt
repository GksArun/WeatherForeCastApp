package com.gks.weatherforecastapp.data

import android.content.Context
import com.gks.weatherforecastapp.BuildConfig
import com.gks.weatherforecastapp.data.database.WeatherForeCastDatabase
import com.gks.weatherforecastapp.data.database.dao.WeatherForecastDao
import com.gks.weatherforecastapp.data.network.WeatherForeCastApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherForeCastDatabase {
        return WeatherForeCastDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideWeatherDao(weatherDatabase: WeatherForeCastDatabase): WeatherForecastDao {
        return weatherDatabase.weatherDao()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            // Choose log level: BASIC, HEADERS, BODY
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)  // attach logger
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenWeather(client: OkHttpClient): WeatherForeCastApi =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherForeCastApi::class.java)
}