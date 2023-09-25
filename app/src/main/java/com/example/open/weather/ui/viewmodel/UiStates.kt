package com.example.open.weather.ui.viewmodel

sealed class UiStates {
    object Loading : UiStates()
    object SelectionScreen : UiStates()
    class WeatherScreen(val data: WeatherState) : UiStates()
    class ErrorState(val message: String) : UiStates()
}
