package com.oi.hata.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oi.hata.common.ui.reminder.CustomReminderPicker
import com.oi.hata.common.reminder.ui.ReminderViewModel

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun NavGraph(startDestination: String = Destinations.REMINDER_ROUTE,
             homeViewModel: HomeViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Destinations.HOME_ROUTE) {
            HomeScreen(homeViewModel = homeViewModel)
        }
        composable(Destinations.REMINDER_ROUTE){
            val reminderViewModel = hiltNavGraphViewModel<ReminderViewModel>()
            CustomReminderPicker(reminderViewModel)
        }
    }

    
}

object Destinations {
    const val HOME_ROUTE = "home"
    const val REMINDER_ROUTE = "reminder"
}


