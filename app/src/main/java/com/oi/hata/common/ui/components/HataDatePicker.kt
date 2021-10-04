package com.oi.hata.common.ui.components

import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.viewinterop.AndroidView
import com.oi.hata.R
import java.util.*

@Composable
fun HataDatePicker(onDateSelected: (Boolean) -> Unit,onDateSelect : (year: Int,month:Int,date:Int,) -> Unit,){

    Column() {
        AndroidView(
            modifier = Modifier.background(color = colorResource(id = R.color.bottombar)),
            factory = { context ->

                var calendar = GregorianCalendar()

                DatePicker(context).apply {
                    init(
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(
                            Calendar.DAY_OF_MONTH
                        )
                    ) { view, year, month, day, ->
                        onDateSelect(year, month + 1, day)
                        onDateSelected(false)

                    }
                }
            },
        )

    }

}
