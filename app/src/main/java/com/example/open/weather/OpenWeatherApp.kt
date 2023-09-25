package com.example.open.weather

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.open.weather.ui.navigation.OpenWeatherActions
import com.example.open.weather.ui.navigation.OpenWeatherNavGraph
import com.example.open.weather.ui.theme.OpenWeatherTheme

@Composable
fun OpenWeatherApp() {

    OpenWeatherTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            OpenWeatherActions(navController)
        }

        OpenWeatherNavGraph(
            navController = navController,
            navigateToHome = navigationActions.navigateToHome,
            navigateToDetail = navigationActions.navigateToDetail
        )
    }
}