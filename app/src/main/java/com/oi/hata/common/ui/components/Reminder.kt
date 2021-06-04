package com.oi.hata.common.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ReminderItem(text: String){
    Text(text = text,color = Color.Blue)
}