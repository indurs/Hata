package com.oi.hata.ui

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.google.accompanist.insets.ProvideWindowInsets

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun HataApp(homeViewModel: HomeViewModel){

        NavGraph(homeViewModel = homeViewModel)

}