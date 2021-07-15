package com.oi.hata.common.reminder.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(): ViewModel() {
    var username by mutableStateOf("")
    var userhobby by mutableStateOf("")

    init{
        Log.d("SAmpleviewmodel", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
    }
    fun onNameChange(name: String){
        username = name
    }

    fun onUserHiobbyChange(hobby: String){
        userhobby = hobby
    }

}