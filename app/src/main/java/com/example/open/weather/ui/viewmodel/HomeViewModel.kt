package com.example.open.weather.ui.viewmodel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.open.weather.data.AUSTRALIA
import com.example.open.weather.data.Result
import com.example.open.weather.data.model.City
import com.example.open.weather.data.model.weather.CurrentWeatherModel
import com.example.open.weather.domain.location.LocationTracker
import com.example.open.weather.domain.repositories.LocalDatabaseRepository
import com.example.open.weather.domain.use_case.GetCurrentWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationTracker: LocationTracker?,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val localDbRepository: LocalDatabaseRepository
) : ViewModel() {

    private var _result =
        MutableStateFlow<Result<CurrentWeatherModel>>(value = Result.CitySelection())

    private val uiStates: MutableState<UiStates> = mutableStateOf(UiStates.SelectionScreen)
    val uiViewStates: MutableState<UiStates> = uiStates

    private lateinit var geoCoder: Geocoder
    private var _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private var _isSearching = MutableStateFlow(false)
    var isSearching = _isSearching.asStateFlow()

    private var _cities = localDbRepository.getAllCities()
    var cities: Flow<List<City>> = _cities

    @VisibleForTesting
    private val _uiState = MutableStateFlow(WeatherState(isLoading = true))
    val state = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentLocation by mutableStateOf<Location?>(null)

    fun getCurrentLocation() {
        viewModelScope.launch {
            currentLocation = locationTracker?.getCurrentLocation()
            currentLocation?.apply {
                val currentCity =
                    geoCoder.getFromLocation(this.latitude, this.longitude, 1)?.get(0)?.locality
                setSelectedCity(currentCity)
            }
        }
    }

    fun onSearchTextChanged(text: String) {
        _searchText.value = text
    }

    fun setSelectedCity(city: String?) {
        var suburb = city
        var lat = 0.0
        var lon = 0.0
        var zip = 0
        uiStates.value = UiStates.Loading
        if (city?.isNumber() == true) {
            //we have Australian post code -> change it to suburb name
            zip = city.toInt()
            val address = Address(Locale.getDefault())
            address.countryCode = AUSTRALIA
            address.postalCode = city
            suburb = geoCoder.getFromLocationName(address.toString(), 1)?.get(0)?.locality
        }
        suburb?.apply {
            _searchText.value = this
            val addressList: MutableList<Address>? = geoCoder.getFromLocationName(suburb, 1)
            addressList?.apply {
                if (this.size > 0) {
                    val address = this[0]
                    lat = address.latitude
                    lon = address.longitude
                }
            }
            val finalCity = City(
                name = suburb,
                lat = lat,
                lon = lon,
                zipCode = zip,
                id = System.currentTimeMillis().toInt()
            )
            viewModelScope.launch {
                if (lat != 0.0 && lon != 0.0) {

                    if (localDbRepository.getCityByName(suburb) == null) {
                        localDbRepository.insertCity(finalCity)
                        getCurrentWeather(finalCity)
                    } else {
                        getCurrentWeather(finalCity)
                    }

                } else {
                    _eventFlow.emit(
                        UIEvent.ShowSnackBar(
                            "City $city not found."
                        )
                    )
                }
            }


        } ?: run {
            viewModelScope.launch {
                _eventFlow.emit(
                    UIEvent.ShowSnackBar(
                        "Postcode $city not found."
                    )
                )
            }
        }
    }

    fun deleteCity(city: String?) {
        viewModelScope.launch {
            val foundCity: City? = localDbRepository.getCityByName(city ?: "")
            foundCity?.apply {
                localDbRepository.deleteCity(this)
            }
        }
    }

    fun setupGeo(context: Context) {
        try {
            geoCoder = Geocoder(context)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun String.isNumber(): Boolean {
        var dotOccurred = 0
        val integerChars = '0'..'9'
        return this.all { it in integerChars || it == '.' && dotOccurred++ < 1 }
    }

    fun selectCity() {
        uiStates.value = UiStates.SelectionScreen
    }

    fun getCurrentWeather(city: City) {
        uiStates.value = UiStates.Loading

        viewModelScope.launch {
            getCurrentWeatherUseCase(city.lat.toString(), city.lon.toString()).collect {
                _result.value = it
            }
            val weather = _result.value.data
            when (_result.value) {
                is Result.CurrentWeatherSuccess -> {
                    uiStates.value = UiStates.WeatherScreen(
                        WeatherState(
                            main = weather?.weather?.get(0)?.main.toString(),
                            temp = weather?.main?.temp.kelvinToCelsius(),
                            feelsLike = weather?.main?.feelsLike.kelvinToCelsius(),
                            humidity = weather?.main?.humidity.toString(),
                            windSpeed = weather?.wind?.speed.toString(),
                            windDeg = weather?.wind?.deg.toString(),
                            rain = if (weather?.rain?.rain1h == null) {
                                null
                            } else weather.rain?.rain1h.toString(),
                            sunRise = weather?.sys?.sunrise.getDateString(),
                            sunSet = weather?.sys?.sunset.getDateString(),
                            name = weather?.name.toString()
                        )
                    )
                }

                is Result.Error -> {
                    uiStates.value = UiStates.ErrorState(_result.value.message)
                }

                is Result.Loading -> {
                    uiStates.value = UiStates.Loading
                }

                is Result.CitySelection -> {/*not happening*/
                }
            }
        }
    }

    private fun Int?.getDateString(): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        return simpleDateFormat.format((this?.toLong() ?: 0) * 1000L)
    }

    private fun Double?.kelvinToCelsius(): String {
        return ((this ?: 0.0) - 273.15).roundOneDigits().toString()
    }

    private fun Double.roundOneDigits(): Float {
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.DOWN
        return df.format(this).toFloat()
    }

    sealed class UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent()
    }

}
