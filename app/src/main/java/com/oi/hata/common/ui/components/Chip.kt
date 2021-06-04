package com.oi.hata.common.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DateChip(text: String){
    Chip(text = text)
}

@Composable
fun Chip(text:String){
    Surface(
        color = Color.White,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(bottom = 4.dp)
    ){
        Text(
            text = text,
            style = MaterialTheme.typography.overline,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        )
    }
}