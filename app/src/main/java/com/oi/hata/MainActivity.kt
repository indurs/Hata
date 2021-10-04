package com.oi.hata

import HataTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.oi.hata.common.reminder.ui.ReminderViewModel
import com.oi.hata.common.ui.LocalBackPressedDispatcher
import com.oi.hata.ui.HataApp
import com.oi.hata.ui.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HataTheme {
                HataApp()
            }
        }
    }
}

