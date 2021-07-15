package com.oi.hata.common.reminder.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.DialogFragment
import com.google.android.material.datepicker.DateSelector
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import java.util.*

@Composable
fun ScreenOne(sampleViewModel: SampleViewModel, onClickHobby: () -> Unit){
    Column {

    }

}

@Composable
fun ScreenTwo(sampleViewModel: SampleViewModel, onCompleteDetail: ()-> Unit){
   TextField(value = sampleViewModel.userhobby, onValueChange = { sampleViewModel.onUserHiobbyChange(it)})
    Button(onClick = { onCompleteDetail() }) {
        Text(text = "Hobby")
    }

}





class MyCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context) {

    init {
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }
}


