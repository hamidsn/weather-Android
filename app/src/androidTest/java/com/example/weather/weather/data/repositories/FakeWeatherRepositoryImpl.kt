package com.example.weather.weather.data.repositories

import com.example.open.weather.data.HTTP_ERROR_MESSAGE
import com.example.open.weather.data.IO_ERROR_MESSAGE
import com.example.open.weather.data.Result
import com.example.open.weather.data.model.CityDao
import com.example.open.weather.data.model.weather.CurrentWeatherModel
import com.example.open.weather.data.source.remote.OpenWeatherApi
import com.example.open.weather.domain.repositories.WeatherRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FakeWeatherRepositoryImpl @Inject constructor(
    private val cityDao: CityDao,
    private val api: OpenWeatherApi
) : WeatherRepository {

    var gson = Gson()
    var weatherData: CurrentWeatherModel = gson.fromJson(
        """{"coord":{"lon":-0.1257,"lat":51.5085},"weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03d"}],"base":"stations","main":{"temp":285.68,"feels_like":285.21,"temp_min":284.02,"temp_max":286.63,"pressure":1014,"humidity":85},"visibility":10000,"wind":{"speed":4.12,"deg":230},"clouds":{"all":40},"dt":1691473759,"sys":{"type":2,"id":2075535,"country":"GB","sunrise":1691469261,"sunset":1691523486},"timezone":3600,"id":2643743,"name":"London","cod":200}""".trimMargin(),
        CurrentWeatherModel::class.java
    )

    override suspend fun getCurrentWeather(
        lat: String,
        lon: String
    ): Flow<Result<CurrentWeatherModel>> = flow {
        try {

            val response = weatherData
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