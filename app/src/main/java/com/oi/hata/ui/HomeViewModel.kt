package com.oi.hata.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel(){

    private val _homeUiState = MutableLiveData<HomeUiState>()
    val homeUiState: LiveData<HomeUiState> = _homeUiState

    var currentTab by mutableStateOf(HataHomeScreens.Today)


    fun onSelectTab(hataScreens: HataHomeScreens){
        Log.d("HomeViewModel>>>>>>",hataScreens.title)
        currentTab = hataScreens
    }


}

@Immutable
data class HomeUiState(
    val currentTab: HataHomeScreens? = null
)