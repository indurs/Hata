package com.oi.hata.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.oi.hata.common.reminder.ui.ReminderViewModel
import com.oi.hata.common.ui.reminder.CustomReminderPicker
import com.oi.hata.task.ui.TaskGroups
import java.util.*

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun HataNavGraph(startDestination: String = HataAppDestinations.HOME_ROUTE,
             ) {

    val navController = rememberAnimatedNavController()
    val actions = remember(navController) { HataNavActions(navController) }

    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(
            HataAppDestinations.HOME_ROUTE,
            enterTransition = { initial, _ ->
                fadeIn(animationSpec = tween(200))
            },
            exitTransition = { _, target ->
                fadeOut(animationSpec = tween(200))
            },
            popEnterTransition = { initial, _ ->
                fadeIn(animationSpec = tween(200))
            },
            popExitTransition = { _, target ->
                fadeOut(animationSpec = tween(200))
            }

        ) {
            //val reminderViewModel =
                //hiltViewModel<ReminderViewModel>(navController.getBackStackEntry(route = HataAppDestinations.HOME_ROUTE))

            //val reminderViewModel =
                //hiltViewModel<ReminderViewModel>(navController.getBackStackEntry(HataAppDestinations.HOME_ROUTE))

            var calendar = GregorianCalendar()

            val reminderViewModel = hiltViewModel<ReminderViewModel>()
            val homeViewModel = hiltViewModel<HomeViewModel>()
            val taskViewModel = hiltViewModel<TaskViewModel>()


            HomeScreen(
                taskViewModel = taskViewModel,
                homeViewModel = homeViewModel,
                reminderViewModel = reminderViewModel,
                onTaskTabSelected = actions.onTaskTabSelected,
                onCustomReminderSelect = actions.onClickReminder,
            )

        }
        composable(
            HataAppDestinations.REMINDER_ROUTE,
        ){

            //val reminderViewModel =
                //hiltViewModel<ReminderViewModel>(navController.getBackStackEntry(route = HataAppDestinations.HOME_ROUTE))


            val reminderViewModel =
                hiltViewModel<ReminderViewModel>(navController.previousBackStackEntry!!)
            val homeViewModel =
                hiltViewModel<HomeViewModel>(navController.previousBackStackEntry!!)
            val taskViewModel =
                hiltViewModel<TaskViewModel>(navController.previousBackStackEntry!!)
            CustomReminderPicker(
                reminderViewModel = reminderViewModel,
                homeViewModel = homeViewModel,
                taskViewModel = taskViewModel,
                onCompleteCustomReminder = actions.onCompleteCustomReminder,
                onCloseCustomReminder = actions.onCloseCustomReminder,
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(6.dp)
            )
        }

        composable(
            HataAppDestinations.GROUP_ROUTE,
            enterTransition = { initial, _ ->
                fadeIn(animationSpec = tween(200))
            },
            exitTransition = { _, target ->
                fadeOut(animationSpec = tween(200))
            },
            popEnterTransition = { initial, _ ->
                fadeIn(animationSpec = tween(200))
            },
            popExitTransition = { _, target ->
                fadeOut(animationSpec = tween(200))
            }
        ){
            val reminderViewModel = hiltViewModel<ReminderViewModel>()

            val taskViewModel = hiltViewModel<TaskViewModel>()


            TaskGroups(
                taskViewModel = taskViewModel,
                reminderViewModel = reminderViewModel,
                onCustomReminderSelect = actions.onClickReminder,
                onBackTaskScreen = actions.onBackTaskScreen
            )
        }
    }
}

object HataAppDestinations {
    const val HOME_ROUTE = "home"
    const val REMINDER_ROUTE = "reminder"
    const val GROUP_ROUTE = "group"
}

class HataNavActions(navController: NavHostController) {
    val onClickReminder: () -> Unit = {
        navController.navigate(HataAppDestinations.REMINDER_ROUTE)
    }

    val onCompleteCustomReminder: () -> Unit = {
        navController.popBackStack()
    }

    val onBackTaskScreen: () -> Unit = {
        navController.popBackStack()
    }

    val onCloseCustomReminder: () -> Unit = {
        navController.popBackStack()
    }

    val onTaskTabSelected: () -> Unit = {
        navController.navigate(HataAppDestinations.GROUP_ROUTE)
    }
}