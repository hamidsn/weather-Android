package com.example.open.weather.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM city_names")
    fun getAllCities(): Flow<List<City>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: City)

    @Delete
    suspend fun deleteCity(city: City)

    @Query("select * from city_names where name = :city ")
    suspend fun getCityByName(city: String): City?

}