package com.example.open.weather.domain.repositories

import com.example.open.weather.data.model.City
import kotlinx.coroutines.flow.Flow

interface LocalDatabaseRepository {

    fun getAllCities(): Flow<List<City>>

    suspend fun insertCity(city: City)

    suspend fun deleteCity(city: City)

    suspend fun getCityByName(city: String): City?

}