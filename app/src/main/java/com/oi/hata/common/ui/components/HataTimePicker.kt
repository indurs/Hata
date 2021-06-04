package com.oi.hata.common.ui.components

import android.widget.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import java.util.*

@Composable
fun HataTimePicker(onTimeSelected: () -> Unit,onTimeSelect: (hour:Int,minute:Int, am: Boolean) -> Unit){
    var context = LocalContext.current
    AndroidView(factory = { context ->
        var calendar = GregorianCalendar()
        TimePicker(context).apply { setOnTimeChangedListener { view, hourOfDay, minute ->
            onTimeSelect(hourOfDay,minute,true)
            onTimeSelected()
        } }
    }
    ) {

    }
}