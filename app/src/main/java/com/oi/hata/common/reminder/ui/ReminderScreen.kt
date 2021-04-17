package com.oi.hata.common.ui.reminder

import HataCalendarTheme
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import apr
import aug
import com.oi.hata.common.reminder.ui.ReminderViewModel
import dec
import feb
import jan
import jul
import jun
import mar
import may
import nov
import oct
import sep


@Composable
fun CustomReminderPicker(reminderViewModel: ReminderViewModel){
    //val reminderViewModel: ReminderViewModel = viewModel()
    //val viewState by reminderViewModel.reminderState.collectAsState()
    Log.d("CustomReminderPicker >>", "CustomReminderPicker>>>>.")
    HataCalendarTheme {
        Surface(color = MaterialTheme.colors.primaryVariant, modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                Reminder(reminderViewModel.reminder)
                Spacer(modifier = Modifier.height(32.dp))
                Header(name="Months",modifier = Modifier.padding(8.dp))

                Months(selectedMonths = reminderViewModel.months, onMonthSelect = reminderViewModel::onMonthSelected)

                Header("Dates",modifier = Modifier.padding(8.dp))
                Dates(selectedDates = reminderViewModel.dates, onDateSelect = reminderViewModel::onDateSelected)

                Header("Weeks")
                Weeks(selectedWeeks = reminderViewModel.weeks, onWeekSelect = reminderViewModel::onWeekSelected)
                Spacer(modifier = Modifier.height(8.dp))
                Header("WeekNum")
                WeekNums(selectedWeekNums = reminderViewModel.weeknums, onWeekNumSelect = reminderViewModel::onWeekNumSelected)
            }
        }
    }

}

@Composable
private fun Reminder(reminder: String){
    Log.d("Reminder ", "Reminder>>>>>>>>>>")
    ReminderSurface(modifier = Modifier,color=Color.White) {
        Log.d("ReminderSurface ", "Reminder>>>>>>>>>>")
        Text(text = reminder)
    }
}

@Composable
private fun Months(selectedMonths: List<String>, onMonthSelect: (String) -> Unit){
    Log.d("Months","Months>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        Row(  modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

            LayoutContainer(modifier = Modifier
                .background(
                    color = MaterialTheme.colors.onSurface,
                    shape = MaterialTheme.shapes.large
                )
                .padding(8.dp))
            {
                for (month in CalMonths.values()) {
                    if(month.name in selectedMonths){
                        Month(month.name, Color.Green,onMonthSelect)
                    }else
                        Month(month.name, Color.White,onMonthSelect)
                }
            }
        }
}

@Composable
private fun Dates(selectedDates: List<Int>, onDateSelect: (Int) -> Unit){
    Log.d("Dates ", ">>>>Dates >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

    Row( modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
        Log.d("Dates ", ">>>>Dates >>>>>>>>>>>>>>>>7777>>>>>>>>>>>>>>>>>>")
        LayoutContainer(modifier = Modifier
            .background(
                color = MaterialTheme.colors.onSurface,
                shape = MaterialTheme.shapes.large
            )
            .padding(8.dp))
        {
            for (date in 1..31) {
                if(date in selectedDates){
                    Date(date, Color.Green,onDateSelect)
                }else{
                    Date(date, Color.White,onDateSelect)
                }

            }
        }
    }
}

@Composable
private fun Weeks(selectedWeeks: List<String>, onWeekSelect: (String) -> Unit){
    Log.d("Weeks", ">>Weeks >>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
    Row( modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {

        LayoutContainer(modifier = Modifier
            .background(
                color = MaterialTheme.colors.onSurface,
                shape = MaterialTheme.shapes.large
            )
            .padding(8.dp))
        {
            for (week in CalWeeks.values()) {
                if(week.name in selectedWeeks)
                    Week(week.name, Color.Green,onWeekSelect)
                else
                    Week(week.name, Color.White,onWeekSelect)
            }
        }
    }
}

@Composable
private fun WeekNums(selectedWeekNums: List<Int>, onWeekNumSelect: (Int) -> Unit){
    Row( modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {

        LayoutContainer(modifier = Modifier
            .background(
                color = MaterialTheme.colors.onSurface,
                shape = MaterialTheme.shapes.large
            )
            .padding(8.dp))
        {
            for (weeknum in 1..5) {
                if(weeknum in selectedWeekNums)
                    WeekNum(weeknum.toString(), Color.Green,onWeekNumSelect)
                else
                    WeekNum(weeknum.toString(), Color.White,onWeekNumSelect)
            }
        }
    }
}

@Composable
private fun Month(name: String, color: Color, onMonthSelect: (String) -> Unit){

    var mthSurfaceModifier: Modifier = Modifier.size(width = CELL_SIZE, height = CELL_SIZE)

    val brush = Brush.horizontalGradient(
        listOf(Color.Gray, Color.Gray, Color.Gray),

    )
    MonthSurface( color = color,brush,modifier = mthSurfaceModifier) {
        Text(text = name, modifier = Modifier
            .clickable { onMonthSelect(name) }
            .padding(4.dp) , style = MaterialTheme.typography.overline)
    }

}

@Composable
private fun Date(name: Int, color: Color, onDateSelect: (Int) -> Unit){

    var dteSurfaceModifier: Modifier = Modifier.size(width = CELL_SIZE, height = CELL_SIZE)

    val brush = Brush.horizontalGradient(
        listOf(Color.Gray, Color.Gray, Color.Gray),

        )
    Log.d("DATE","DAte *********************************************")
    DateSurface( color = color,brush,modifier = dteSurfaceModifier) {
        Text(text = name.toString(), modifier = Modifier
            .clickable { onDateSelect(name) }
            .padding(4.dp), style = MaterialTheme.typography.overline)
    }

}

@Composable
private fun Week(name: String, color: Color, onWeekSelect: (String) -> Unit){

    var weekSurfaceModifier: Modifier = Modifier.size(width = WEEK_CELL_SIZE, height = WEEK_CELL_SIZE)

    val brush = Brush.horizontalGradient(
        listOf(Color.Gray, Color.Gray, Color.Gray),

        )

    WeekSurface( color = color,brush,modifier = weekSurfaceModifier) {
        Text(text = name, modifier = Modifier
            .clickable {onWeekSelect(name)  }
            .padding(4.dp), style = MaterialTheme.typography.overline)
    }

}

@Composable
private fun WeekNum(name: String, color: Color, onWeekNumSelect: (Int) -> Unit){

    var weekSurfaceModifier: Modifier = Modifier.size(width = WEEK_NUM_CELL_SIZE, height = WEEK_NUM_CELL_SIZE)

    val brush = Brush.horizontalGradient(
        listOf(Color.Gray, Color.Gray, Color.Gray),

        )

    WeekNumSurface( color = color,brush,modifier = weekSurfaceModifier) {
        Text(text = name, modifier = Modifier
            .clickable { onWeekNumSelect(name.toInt()) }
            .padding(4.dp), style = MaterialTheme.typography.overline)
    }

}

@Composable
private fun Header(name: String,modifier: Modifier = Modifier){
    Log.d("Header","Heeader >>>>>>>>>>>>>>>>>")
    Row(modifier = modifier) {
        Text(text = name,color = Color.Black,style = MaterialTheme.typography.caption)
    }
}

@Composable
private fun ReminderSurface(color: Color,modifier: Modifier, content: @Composable () -> Unit){
    Surface(modifier = modifier.padding(4.dp),color = color,shape = RoundedCornerShape(8.dp)) {
        content()
    }
}

@Composable
private fun MonthSurface(color: Color,
                         brush: Brush,
                         modifier: Modifier,
                         content: @Composable () -> Unit
){
    
    Surface(modifier = modifier.padding(4.dp), color = color, shape= CircleShape, elevation = 1.dp) {
        Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
    }
    
}



@Composable
private fun DateSurface(color: Color,
                         brush: Brush,
                         modifier: Modifier,
                         content: @Composable () -> Unit
){

    Surface(modifier = modifier.padding(4.dp), color = color, shape= CutCornerShape(2.dp), elevation = 1.dp) {
        Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
    }

}

@Composable
private fun WeekSurface(color: Color,
                        brush: Brush,
                        modifier: Modifier,
                        content: @Composable () -> Unit
){

    Surface(modifier = modifier.padding(4.dp), color = color, shape= RoundedCornerShape(topStart = 8.dp,topEnd = 8.dp), elevation = 1.dp) {
        Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
    }

}

@Composable
private fun WeekNumSurface(color: Color,
                        brush: Brush,
                        modifier: Modifier,
                        content: @Composable () -> Unit
){

    Surface(modifier = modifier.padding(4.dp), color = color, shape= RoundedCornerShape(topStart = 8.dp,topEnd = 8.dp), elevation = 1.dp) {
        Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
    }

}

@Composable
fun LayoutContainer(modifier: Modifier=Modifier, content: @Composable () -> Unit){

    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        var rowWidths = mutableListOf<Int>()
        var rowWidth = 0
        var maxRowElements = 0
        var layoutHeight = 0
        var layoutWidth = 0
        var index = 0

        val placeables = measurables.map { measurable ->

            val placeable = measurable.measure(constraints)

            if((rowWidth + placeable.width) < constraints.maxWidth){
                rowWidth += placeable.width
            }else{
                rowWidths.add(rowWidth)
                ++index
                rowWidth = 0
                rowWidth += placeable.width
            }

            placeable
        }

        rowWidths.add(rowWidth)
        ++index

        layoutHeight = placeables[0].height * index

        layoutWidth = rowWidths.maxOf { it }

        maxRowElements =  layoutWidth/placeables[0].width

        var xPosition = 0
        var yPosition = 0
        var rowElements = 0

        layout(layoutWidth,layoutHeight){
            placeables.forEach {
                if(rowElements >= maxRowElements){
                    xPosition = 0
                    yPosition += it.height
                    rowElements = 0
                }
                it.placeRelative(xPosition,yPosition)
                xPosition = xPosition+it.width
                ++rowElements
            }
        }
    }
}


private val CELL_SIZE = 48.dp
private val WEEK_CELL_SIZE = 48.dp
private val WEEK_NUM_CELL_SIZE = 48.dp

enum class CalMonths {
    Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
}

private val monthColorMap = mapOf(CalMonths.Jan to jan,
    CalMonths.Feb to feb,
    CalMonths.Mar to mar,
    CalMonths.Apr to apr,
    CalMonths.May to may,
    CalMonths.Jun to jun,
    CalMonths.Jul to jul,
    CalMonths.Aug to aug,
    CalMonths.Sep to sep,
    CalMonths.Oct to oct,
    CalMonths.Nov to nov,
    CalMonths.Dec to dec
)

enum class CalWeeks { Mon,Tue,Wed,Thu,Fri,Sat,Sun }


/*@Preview
@Composable
fun LayoutSampleTest(){
   Surface(){
       Column(modifier = Modifier.padding(bottom = 8.dp)) {
           LayoutContainer(modifier = Modifier.padding(16.dp))
           {
               for (month in CalMonths.values().take(6)) {
                   Month(month.name, Color.White)
               }
           }
       }
   }
}*/
