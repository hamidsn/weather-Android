package com.example.open.weather.data.source.remote

import com.example.open.weather.data.model.weather.CurrentWeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String
    ): CurrentWeatherModel

}