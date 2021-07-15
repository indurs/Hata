package com.oi.hata.common.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oi.hata.R

@Composable
fun DateChip(text: String){
    Chip(text = text)
}

@Composable
fun Chip(text:String){
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colors.surface.copy(alpha = 0.50f),
        modifier = Modifier
            .padding(bottom = 4.dp)
            .border(BorderStroke(1.dp, colorResource(id = R.color.header)), RoundedCornerShape(40.dp))
    ){
        Text(
            text = text,
            color = Color.White,
            fontSize = 8.sp,
            style = MaterialTheme.typography.button,
            modifier = Modifier.padding(8.dp)
        )
    }
}