package com.gks.weatherforecastapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gks.weatherforecastapp.view.home.HomeScreen

enum class NavRoutePage {
    HOME
}

@Composable
fun WeatherForeCastNavHost(
    modifier: Modifier,
) {

    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = NavRoutePage.HOME.name,
        modifier = modifier
    ) {
        composable(NavRoutePage.HOME.name) {
            HomeScreen()
        }
    }
}