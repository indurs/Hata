package com.oi.hata.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.navigation
import com.oi.hata.R
import com.oi.hata.common.ui.reminder.CustomReminderPicker
import com.oi.hata.common.reminder.ui.ReminderViewModel

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun NavGraph(startDestination: String = HataAppDestinations.HOME_ROUTE,
             homeViewModel: HomeViewModel) {

    val navController = rememberNavController()
    val actions = remember(navController) { HataNavActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(HataAppDestinations.HOME_ROUTE) {
            val reminderViewModel =
                hiltViewModel<ReminderViewModel>(navController.getBackStackEntry(route = HataAppDestinations.HOME_ROUTE))
            //val reminderViewModel = hiltNavGraphViewModel<ReminderViewModel>()
            HomeScreen(homeViewModel = homeViewModel,reminderViewModel = reminderViewModel,onClickReminder = actions.onClickReminder)

        }
        composable(HataAppDestinations.REMINDER_ROUTE){
            val reminderViewModel =
                hiltViewModel<ReminderViewModel>(navController.getBackStackEntry(route = HataAppDestinations.HOME_ROUTE))
            //val reminderViewModel = hiltNavGraphViewModel<ReminderViewModel>(navController.previousBackStackEntry!!)
            CustomReminderPicker(reminderViewModel = reminderViewModel,onCompleteCustomReminder = actions.onCompleteCustomReminder)
        }
    }

    
}

object HataAppDestinations {
    const val HOME_ROUTE = "home"
    const val REMINDER_ROUTE = "reminder"
}



class HataNavActions(navController: NavHostController) {
    val onClickReminder: () -> Unit = {
        navController.navigate(HataAppDestinations.REMINDER_ROUTE)
    }

    val onCompleteCustomReminder: () -> Unit = {
        navController.popBackStack()
    }
}