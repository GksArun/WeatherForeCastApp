package com.gks.weatherforecastapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gks.weatherforecastapp.data.database.dao.WeatherForecastDao
import com.gks.weatherforecastapp.data.database.entity.CityEntity
import com.gks.weatherforecastapp.data.database.entity.WeatherForeCastEntity

@Database(
    exportSchema = false,
    entities = [
        CityEntity::class,
        WeatherForeCastEntity::class,
    ],
    version = 1
)
abstract class WeatherForeCastDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherForecastDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherForeCastDatabase? = null


        fun getDatabase(context: Context): WeatherForeCastDatabase {
            return INSTANCE ?: synchronized(this) {

                val databaseBuilder = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherForeCastDatabase::class.java,
                    "weather_forecast_db"
                ).fallbackToDestructiveMigration()
                val instance = databaseBuilder.build()
                INSTANCE = instance
                instance
            }
        }
    }
}