package com.example.open.weather.ui.viewmodel

data class WeatherState(
    val main: String? = null,
    val temp: String? = null,
    val feelsLike: String? = null,
    val humidity: String? = null,
    val windSpeed: String? = null,
    val windDeg: String? = null,
    val rain: String? = null,
    val sunRise: String? = null,
    val sunSet: String? = null,
    val name: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null

)
