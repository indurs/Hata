package com.oi.hata.common.ui

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import java.util.*

@Composable
fun HataDatePicker(onDateSelect : (year: Int,month:Int,date:Int) -> Unit){
    var context = LocalContext.current
    AndroidView(factory = { context ->
        var calendar = GregorianCalendar()
        DatePicker(context).apply {  init(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)) { view, year, month, day ->
           onDateSelect(year,month,day)
        } }
    }) {

    }
}

@Composable
fun HataTimePicker(onTimeSelect: (hour:Int,minute:Int, am: Boolean) -> Unit){
    var context = LocalContext.current
    AndroidView(factory = { context ->
        var calendar = GregorianCalendar()
        TimePicker(context).apply { setOnTimeChangedListener { view, hourOfDay, minute ->
            onTimeSelect(hourOfDay,minute,true)
        } }
    }
    ) {

    }
}