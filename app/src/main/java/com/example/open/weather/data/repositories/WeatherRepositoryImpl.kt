package com.example.open.weather.data.repositories

import com.example.open.weather.BuildConfig
import com.example.open.weather.data.HTTP_ERROR_MESSAGE
import com.example.open.weather.data.IO_ERROR_MESSAGE
import com.example.open.weather.data.Result
import com.example.open.weather.data.model.CityDao
import com.example.open.weather.data.model.weather.CurrentWeatherModel
import com.example.open.weather.data.source.remote.OpenWeatherApi
import com.example.open.weather.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val cityDao: CityDao, //possible to be used in future
    private val api: OpenWeatherApi
) : WeatherRepository {
    override suspend fun getCurrentWeather(
        lat: String,
        lon: String
    ): Flow<Result<CurrentWeatherModel>> = flow {

        try {
            val response = api.getCurrentWeather(lat, lon, BuildConfig.APP_ID)
            emit(Result.CurrentWeatherSuccess(response))
        } catch (e: HttpException) {
            emit(
                Result.Error(
                    message = HTTP_ERROR_MESSAGE,
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Result.Error(
                    message = IO_ERROR_MESSAGE,
                    data = null
                )
            )
        }
    }


}