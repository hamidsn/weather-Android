package com.example.open.weather.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.open.weather.data.model.City
import com.example.open.weather.data.model.CityDao

@Database(entities = [City::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {
        const val DATABASE_NAME = "CITY_NAMES"
    }

}