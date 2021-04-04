package com.oi.hata.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oi.hata.common.ui.reminder.Months

object MainDestinations {
    const val HOME_ROUTE = "home"
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun NavGraph(startDestination: String = MainDestinations.HOME_ROUTE,
             homeViewModel: HomeViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainDestinations.HOME_ROUTE) {
            //HomeScreen(homeViewModel = homeViewModel)
            Months()
        }
    }
}


