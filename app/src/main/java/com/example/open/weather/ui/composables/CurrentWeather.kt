package com.example.open.weather.ui.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.open.weather.R
import com.example.open.weather.ui.viewmodel.WeatherState

@Composable
fun CurrentWeather(
    modifier: Modifier = Modifier,
    uiState: WeatherState,
    backAction: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.world_map_svgrepo_com),
            contentDescription = "Weather forecast"
        )

        Text(
            text = uiState.name.toString(),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = uiState.main.toString(),
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.sunset_svgrepo),
                contentDescription = "Sunrise"
            )
            Text(
                text = "Sunrise: ${uiState.sunRise}",
                modifier = Modifier.padding(16.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sunset: ${uiState.sunSet}",
                modifier = Modifier.padding(16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.sunrise_svgrepo),
                contentDescription = "Sunset"
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.rain_svgrepo_com),
                contentDescription = "Rain"
            )
            Text(
                text = "Rain: ${uiState.rain ?: "Not available"}",
                modifier = Modifier.padding(16.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Wind speed: ${uiState.windSpeed.toString()} (meter/sec)",
                modifier = Modifier.padding(16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.wind_svgrepo_com),
                contentDescription = "Wind speed"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.curve_arrow_right_circle_svgrepo_com),
                contentDescription = "Wind degree"
            )
            Text(
                text = "Wind degree: ${uiState.windDeg.toString()} (meteorological)",
                modifier = Modifier.padding(16.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Humidity: ${uiState.humidity.toString()}",
                modifier = Modifier.padding(16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.humidity_svgrepo),
                contentDescription = "Humidity"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.temperature_svgrepo_com),
                contentDescription = "Temperature"
            )
            Text(
                text = "Temperature: ${uiState.temp}",
                modifier = Modifier.padding(16.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Feels like: ${uiState.feelsLike}",
                modifier = Modifier.padding(16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.man_walking_svgrepo_com),
                contentDescription = "Feels like"
            )
        }
    }
    BackHandler(true) {
        backAction()
    }
}

@Composable
fun ErrorScreen(error: String, modifier: Modifier = Modifier, retryAction: () -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.connection_svgrepo_com),
            contentDescription = stringResource(R.string.loading_failed)
        )
        Text(
            text = stringResource(R.string.loading_failed) + ":\n $error",
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry), color = Color.White)
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}
