package com.example.open.weather.data

sealed class Result<T>(val data: T? = null, val message: String = "") {
    class Loading<T>(data: T? = null) : Result<T>(data)
    class CitySelection<T>(data: T? = null) : Result<T>(data)
    class CurrentWeatherSuccess<T>(data: T?) : Result<T>(data)
    class Error<T>(message: String, data: T? = null) : Result<T>(data, message)
}
