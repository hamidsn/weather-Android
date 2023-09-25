package com.example.open.weather.data.repositories

import com.example.open.weather.data.model.City
import com.example.open.weather.data.model.CityDao
import com.example.open.weather.domain.repositories.LocalDatabaseRepository
import kotlinx.coroutines.flow.Flow

class LocalDatabaseRepositoryImpl(
    private val dao: CityDao
) : LocalDatabaseRepository {

    override fun getAllCities(): Flow<List<City>> {
        return dao.getAllCities()
    }

    override suspend fun insertCity(city: City) {
        dao.insertCity(city)
    }

    override suspend fun deleteCity(city: City) {
        dao.deleteCity(city)
    }

    override suspend fun getCityByName(city: String): City? {
        return dao.getCityByName(city)
    }
}