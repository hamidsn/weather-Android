package com.example.open.weather.ui.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.open.weather.R
import com.example.open.weather.data.model.City
import com.example.open.weather.ui.theme.OpenWeatherTheme
import com.example.open.weather.ui.viewmodel.HomeViewModel
import com.example.open.weather.ui.viewmodel.UiStates
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherApp() {
    val viewModel: HomeViewModel = hiltViewModel()
    val eventFlow = viewModel.eventFlow
    val scaffoldState = rememberScaffoldState()

    OpenWeatherTheme {
        val searchText by viewModel.searchText.collectAsState()
        val cities by viewModel.cities.collectAsState(initial = emptyList())
        val isSearching by viewModel.isSearching.collectAsState()
        val uiViewStates by remember { mutableStateOf(viewModel.uiViewStates) }

        val isVisible by remember {
            derivedStateOf {
                searchText.isNotBlank()
            }
        }
        val context = LocalContext.current
        LaunchedEffect(context) {
            viewModel.setupGeo(context)
        }

        LaunchedEffect(key1 = true) {
            eventFlow.collectLatest { event ->
                when (event) {
                    is HomeViewModel.UIEvent.ShowSnackBar -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val locationPermissions = rememberMultiplePermissionsState(
            permissions = listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        )

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {

            },
            bottomBar = {
                if (uiViewStates.value is UiStates.SelectionScreen) {
                    HomeBottomBar(
                        onResetPressed = {
                            if (locationPermissions.allPermissionsGranted) {
                                viewModel.getCurrentLocation()
                            } else {
                                locationPermissions.launchMultiplePermissionRequest()
                            }
                        },
                    )
                }

            },
        ) { innerPadding ->
            when (uiViewStates.value) {

                is UiStates.SelectionScreen -> {
                    CityList(
                        modifier = Modifier.padding(innerPadding),
                        searchText,
                        viewModel,
                        isVisible,
                        isSearching,
                        cities
                    )
                }

                is UiStates.WeatherScreen -> {
                    CurrentWeather(
                        modifier = Modifier.padding(innerPadding),
                        (uiViewStates.value as UiStates.WeatherScreen).data,
                        backAction = { viewModel.selectCity() })
                }

                is UiStates.ErrorState -> {
                    ErrorScreen(
                        (uiViewStates.value as UiStates.ErrorState).message,
                        Modifier.fillMaxSize()
                    ) { viewModel.selectCity() }
                }

                is UiStates.Loading -> {
                    LoadingScreen(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Composable
private fun CityList(
    modifier: Modifier = Modifier,
    searchText: String,
    viewModel: HomeViewModel,
    isVisible: Boolean,
    isSearching: Boolean,
    cities: List<City>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TextField(value = searchText,
            onValueChange = viewModel::onSearchTextChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Search") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { viewModel.setSelectedCity(searchText) }
            ),
            trailingIcon = {
                if (isVisible) {
                    IconButton(
                        onClick = { viewModel.setSelectedCity(searchText) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Clear"
                        )
                    }
                }
            }

        )
        Spacer(modifier = Modifier.height(16.dp))
        if (isSearching) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(cities) { city ->
                    Row(//horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(all = 3.dp)
                            .fillMaxWidth()
                            .clickable { viewModel.getCurrentWeather(city) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.History, contentDescription = "history")

                        Text(
                            text = city.name.toString().replaceFirstChar { it.uppercase() },
                            modifier = Modifier
                                .padding(16.dp)
                        )
                        Spacer(Modifier.weight(1f))

                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "delete",
                            modifier = Modifier.clickable { viewModel.deleteCity(city.name) })
                    }
                }
            }
        }

    }
}

@Composable
private fun HomeBottomBar(
    onResetPressed: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                enabled = true,
                onClick = onResetPressed
            ) {
                Text(text = stringResource(id = R.string.user_location))
            }
        }
    }
}
