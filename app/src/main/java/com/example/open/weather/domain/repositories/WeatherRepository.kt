package com.example.open.weather.domain.repositories

import com.example.open.weather.data.Result
import com.example.open.weather.data.model.weather.CurrentWeatherModel
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getCurrentWeather(lat: String, lon: String): Flow<Result<CurrentWeatherModel>>

}