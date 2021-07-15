package com.oi.hata.common.ui.components

import HataTheme
import android.content.Context
import android.util.AttributeSet
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.oi.hata.R
import java.util.*

@Composable
fun HataDatePicker(onDateSelected: (Boolean) -> Unit,onDateSelect : (year: Int,month:Int,date:Int,) -> Unit,){

    Column() {
        AndroidView(factory = { context ->

            var calendar = GregorianCalendar()

            DatePicker(context).apply {
                init(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(
                Calendar.DAY_OF_MONTH)) { view, year, month, day,   ->
                onDateSelect(year,month+1,day)
                onDateSelected(false)

            } }
        },)

    }

}
