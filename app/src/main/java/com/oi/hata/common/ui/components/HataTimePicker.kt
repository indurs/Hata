package com.oi.hata.common.ui.components

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.viewinterop.AndroidView
import com.oi.hata.R
import java.util.*

@Composable
fun HataTimePicker(onTimeSelected: (Boolean) -> Unit,onTimeSelect: (hour:Int,minute:Int, am: Boolean) -> Unit){
    Column() {
        AndroidView(
            modifier = Modifier.background(color = colorResource(id = R.color.timepicker)),
            factory = { context ->
            var calendar = GregorianCalendar()
            TimePicker(context).apply {
                setOnTimeChangedListener { view, hourOfDay, minute ->
                    onTimeSelect(hourOfDay, minute, true)
                    onTimeSelected(false)
                }
            }
        },
        ) {

        }
    }
}
