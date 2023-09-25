package com.example.open.weather.domain.use_case

import com.example.open.weather.data.Result
import com.example.open.weather.data.model.weather.CurrentWeatherModel
import com.example.open.weather.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lat: String, lon: String): Flow<Result<CurrentWeatherModel>> {
        return repository.getCurrentWeather(lat, lon)
    }
}