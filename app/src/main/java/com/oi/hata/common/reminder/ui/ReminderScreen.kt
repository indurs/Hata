package com.oi.hata.common.ui.reminder

import HataCalendarTheme
import android.util.Log
import androidx.annotation.FloatRange
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import apr
import aug
import com.google.accompanist.insets.statusBarsHeight
import com.oi.hata.R
import com.oi.hata.common.reminder.data.local.model.HataReminder
import com.oi.hata.common.reminder.data.local.model.ReminderMaster
import com.oi.hata.common.reminder.ui.ReminderViewModel
import com.oi.hata.common.reminder.ui.dateTransition
import com.oi.hata.common.reminder.ui.monthTransition
import com.oi.hata.common.reminder.ui.weekTransition
import com.oi.hata.common.ui.*
import com.oi.hata.common.ui.components.HataDatePicker
import com.oi.hata.common.ui.components.HataTimePicker
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.ui.CustomReminderContentUpdates
import com.oi.hata.task.ui.ReminderContentUpdates
import com.oi.hata.ui.HomeViewModel
import com.oi.hata.ui.TaskViewModel
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
import kotlin.math.pow


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun CustomReminderPicker(reminderViewModel: ReminderViewModel,
                         homeViewModel: HomeViewModel,
                         taskViewModel: TaskViewModel,
                         onCompleteCustomReminder: () -> Unit,
                         onCloseCustomReminder: () -> Unit,
                         color: Color = MaterialTheme.colors.surface,
                         shape: Shape
){
    //val reminderViewModel: ReminderViewModel = viewModel()
    //val viewState by reminderViewModel.reminderState.collectAsState()

    HataCalendarTheme {

        /*Scaffold(
            topBar = { TopBar(reminderViewModel.reminder,onCompleteCustomReminder = onCompleteCustomReminder)},
            content = { ReminderWhenOptions(reminderViewModel = reminderViewModel, color = color, shape = shape) },
            bottomBar = {},
            drawerContent = {}
        )*/
        ReminderContent(
            reminderViewModel = reminderViewModel,
            reminder = reminderViewModel.reminder,
            onCompleteCustomReminder = onCompleteCustomReminder,
            clearCustomReminderValues = { reminderViewModel.clearCustomReminderValues(taskViewModel.getReminder()) },
            setHataReminder = { taskViewModel.setReminder(reminderViewModel.getReminderValues()) },
            onCloseCustomReminder = onCloseCustomReminder,
            shape = shape)

    }

    //Log.d("CustomReminderPicker >>","******************" + reminderViewModel.reminderTxt)

}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
private fun ReminderContent(reminderViewModel: ReminderViewModel,
                            reminder:String,
                            onCompleteCustomReminder: () -> Unit,
                            onCloseCustomReminder: () -> Unit,
                            clearCustomReminderValues: () -> Unit,
                            setHataReminder: () -> Unit,
                            color: Color = MaterialTheme.colors.surface,
                            shape: Shape
){
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            val surfaceColor = MaterialTheme.colors.surface
            val appBarColor = MaterialTheme.colors.surface.copy(alpha = 0.87f)
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ){
                Spacer(
                    Modifier
                        .background(appBarColor)
                        .fillMaxWidth()
                        .statusBarsHeight()
                )
                TopBar(reminder,
                    onCompleteCustomReminder = onCompleteCustomReminder,
                    onCloseCustomReminder = onCloseCustomReminder,
                    clearCustomReminderValues = clearCustomReminderValues,
                    setHataReminder = setHataReminder
                )
            }
                ReminderWhenOptions(reminderViewModel = reminderViewModel, color = color, shape = shape)
            }
        }
}

@Composable
private fun TopBar(
                    reminder: String,
                    onCompleteCustomReminder: () -> Unit,
                    clearCustomReminderValues: () -> Unit,
                    setHataReminder: () -> Unit,
                    onCloseCustomReminder: () -> Unit){

    Column() {
        Surface(modifier = Modifier.fillMaxWidth()) {

            Column(horizontalAlignment = Alignment.Start) {
                IconButton(
                    onClick = { onCompleteCustomReminder()
                                setHataReminder()
                    }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_done_24),
                        tint = Color.White.copy(alpha = 0.90f).compositeOver(Color.Black),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                IconButton(
                    onClick = { onCloseCustomReminder()
                                clearCustomReminderValues()
                    }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_undo_24),
                        tint = Color.White.copy(alpha = 0.90f).compositeOver(Color.Black),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                }
            }
        }
        When(reminder)
    }

}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun ReminderWhenOptions(reminderViewModel: ReminderViewModel,
                                color: Color,
                                shape: Shape
                                ){


    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()){

        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()) {

            Months(selectedMonths = reminderViewModel.months,
                onMonthSelect = reminderViewModel::onMonthSelected,
                color = color,
                shape = shape
            )

            Dates(selectedDates = reminderViewModel.dates,
                onDateSelect = reminderViewModel::onDateSelected,
                color = color,
                shape = shape
            )

            Card(
                modifier = Modifier.padding(12.dp),
                shape = shape
            ){
                Column() {
                    Header("Weeks",color= colorResource(id = R.color.header2))
                    Weeks(selectedWeeks = reminderViewModel.weeks,
                        onWeekSelect = reminderViewModel::onWeekSelected,
                        color = color,
                    )

                    Header("WeekNum",color = colorResource(id = R.color.header2))
                    WeekNums(selectedWeekNums = reminderViewModel.weeknums,
                        onWeekNumSelect = reminderViewModel::onWeekNumSelected,
                        color = color,
                    )
                }
            }
        }

    }
}


@Composable
private fun Reminders(reminders: List<ReminderMaster>){
    Card(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            for(item in reminders){
                Text(text = item.alarmScreenVal)
            }
        }
    }
}

@Composable
private fun When(reminder: String){

    ReminderSurface(color=Color.White) {
        Text(
            text = if(reminder.isNotEmpty()) reminder else stringResource(id = R.string.emptyreminder),
            color = colorResource(id = R.color.customselect),
            style = MaterialTheme.typography.overline,
            fontSize = 12.sp)
    }
}

/*@ExperimentalMaterialApi
@Composable
private fun Months(selectedMonths: List<String>,
                   onMonthSelect: (String) -> Unit,
                   color: Color,
                   shape: Shape,
                   modifier: Modifier = Modifier
) {

        Column() {
            Header(
                name = stringResource(id = R.string.months),
                color = colorResource(id = R.color.header1)
            )
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                LayoutContainer(
                    modifier = modifier
                        .padding(8.dp)
                    .background(
                        color = colorResource(id = R.color.grey800)
                    )
                )
                {
                    for (month in CalMonths.values()) {
                        Month(month.name, color, onMonthSelect, selectedMonths = selectedMonths)
                    }
                }
            }
        }

}*/

@ExperimentalMaterialApi
@Composable
private fun Months(selectedMonths: List<String>,
                   onMonthSelect: (String) -> Unit,
                   color: Color,
                   shape: Shape,
                   modifier: Modifier = Modifier
                    ) {
    Card(
        modifier = Modifier.padding(12.dp),
        shape = shape
    ){
        Column() {
            Header(
                name = stringResource(id = R.string.months),
                color = colorResource(id = R.color.header1)
            )
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                LayoutContainer(
                    modifier = modifier
                        .padding(8.dp)
                        /*.background(
                            color = colorResource(id = R.color.grey800)
                        )*/
                )
                {
                    for (month in CalMonths.values()) {
                        Month(month.name, color, onMonthSelect, selectedMonths = selectedMonths)
                    }
                }
            }
        }
    }
}



@ExperimentalMaterialApi
@Composable
private fun Dates(selectedDates: List<Int>,
                  onDateSelect: (Int) -> Unit,
                  color: Color,
                  shape: Shape,
                  modifier: Modifier = Modifier
){
    Card(
        modifier = Modifier.padding(12.dp),
        shape = shape
    ) {
        Column() {
            Header("Dates",color = colorResource(id = R.color.sep))
            Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                LayoutContainer(
                    modifier = modifier
                )
                {
                    for (date in 1..31) {
                        Date(
                            date,
                            color = color,
                            onDateSelect = onDateSelect,
                            selectedDates = selectedDates
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun Weeks(selectedWeeks: List<String>,
                  onWeekSelect: (String) -> Unit,
                  color: Color,
                  modifier: Modifier = Modifier
){

    Row( modifier = modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {

        LayoutContainer(
            modifier = modifier
            )

        {
            for (week in ReminderUtil.WEEKNAMES.values()) {
                Week(week.name, color = color,onWeekSelect,selectedWeeks)
            }
        }
    }
}

@Composable
private fun WeekNums(selectedWeekNums: List<Int>,
                     onWeekNumSelect: (Int) -> Unit,
                     color:Color,
                     modifier: Modifier = Modifier
){
    Row( modifier = modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {

        LayoutContainer(modifier = modifier)
        {
            for (weeknum in 1..5) {
                WeekNum(weeknum.toString(), color,onWeekNumSelect)
            }
        }
    }

}

@ExperimentalMaterialApi
@Composable
private fun Month(name: String, color: Color, onMonthSelect: (String) -> Unit,selectedMonths: List<String>){

    var mthSurfaceModifier: Modifier = Modifier
        .size(width = CELL_SIZE, height = CELL_SIZE)

    MonthSurface(
        color = colorResource(id = R.color.customremsurface),
        modifier = mthSurfaceModifier,
        name = name,
        onMonthSelect = onMonthSelect,
        selectedMonths = selectedMonths
    ) {

            Text(modifier = Modifier.padding(4.dp),
                textAlign = TextAlign.Center,
                text = name,
                color = Color.White,
                style = MaterialTheme.typography.overline)
    }

}

@ExperimentalMaterialApi
@Composable
private fun Date(name: Int, color: Color,
                 onDateSelect: (Int) -> Unit,
                 selectedDates: List<Int>
){

    var dteSurfaceModifier: Modifier = Modifier.size(width = CELL_SIZE, height = CELL_SIZE)

    DateSurface(
        date = name,
        color = colorResource(id = R.color.customremsurface),
        modifier = dteSurfaceModifier,
        onDateSelect = onDateSelect,
        selectedDates = selectedDates
    ) {
        Text(text = name.toString(), modifier = Modifier
            .padding(4.dp),
            color = Color.White,
            style = MaterialTheme.typography.overline)
    }

}

@ExperimentalMaterialApi
@Composable
private fun Week(name: String, color: Color, onWeekSelect: (String) -> Unit, selectedWeeks: List<String>){

    var weekSurfaceModifier: Modifier = Modifier.size(width = WEEK_CELL_SIZE, height = WEEK_CELL_SIZE)

    WeekSurface(
        color = colorResource(id = R.color.customremsurface),
        modifier = weekSurfaceModifier,
        onWeekSelect = onWeekSelect,
        selectedWeeks = selectedWeeks,
        name = name
    ) {
        Text(text = name, modifier = Modifier
            .padding(4.dp),
            color = Color.White,
            style = MaterialTheme.typography.overline)
    }

}

/*if (monthTransitionState.selectedAlpha > 0f) {

            Dot(
                color = color.copy(alpha = 0.20f).compositeOver(Color.White),
                modifier = Modifier
                    .padding(start = 20.dp, top = 32.dp)
                    .size(8.dp)
            )
        }*/


@Composable
private fun WeekNum(name: String, color: Color, onWeekNumSelect: (Int) -> Unit){

    var weekSurfaceModifier: Modifier = Modifier.size(width = WEEK_NUM_CELL_SIZE, height = WEEK_NUM_CELL_SIZE)

    val brush = Brush.horizontalGradient(
        listOf(Color.Gray, Color.Gray, Color.Gray),

        )

    WeekNumSurface( color = MaterialTheme.colors.surface,
        brush,
        modifier = weekSurfaceModifier) {
        Text(text = name, modifier = Modifier
            .clickable { onWeekNumSelect(name.toInt()) }
            .padding(4.dp),
            color = Color.White,
            style = MaterialTheme.typography.overline)
    }

}

@Composable
private fun Header(name: String,modifier: Modifier = Modifier,color: Color){
    Log.d("Header","Heeader >>>>>>>>>>>>>>>>>")
    Row(modifier = modifier.padding(start = 16.dp,top=12.dp,bottom = 8.dp)) {
        Text(text = name,
            color = color,
            style = MaterialTheme.typography.subtitle2)
    }
}

@Composable
private fun ReminderSurface(color: Color,
                            modifier:Modifier = Modifier,
                            content: @Composable () -> Unit){
    
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        modifier=Modifier.padding(16.dp),
        shape = RoundedCornerShape(4.dp),
        ) {
            Column(modifier = modifier

                .fillMaxWidth(),) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {

                    Text(
                        text = "When",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(16.dp)
                    )

                    IconButton(
                        onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_undo_24),
                            tint = Color.White,
                            contentDescription = null,)
                    }
                }
                ReminderScreenDivider(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                )

                Spacer(modifier = Modifier.padding(4.dp))
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp)) {
                    content()
                }
            }
        }
}

@ExperimentalMaterialApi
@Composable
private fun MonthSurface(color: Color,
                         modifier: Modifier,
                         name: String,
                         onMonthSelect: (String) -> Unit,
                         selectedMonths: List<String>,
                         content: @Composable () -> Unit,
){
    val monthTransitionState = monthTransition(month = name,selectedMonths = selectedMonths)


    Box(contentAlignment = Alignment.Center){
        Surface( modifier = modifier.padding(4.dp),
            color  = colorResource(id = R.color.header1),
            shape= RoundedCornerShape(8.dp),
            elevation = 1.dp) {

        }
        Surface(modifier = modifier.padding(4.dp),
            onClick = { onMonthSelect(name) },
            color = color,
            shape = RoundedCornerShape(4.dp, monthTransitionState.cornerRadius,4.dp,4.dp),
            elevation = 1.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }

    }
}

/*Surface(
      modifier = modifier,
      shape = RoundedCornerShape(MONTH_CORNER),
      onClick = { onMonthSelect(name) },
      color = monthTransitionState.colorAlpha
  ) {

      Column(
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally) {
          content()
      }
  }*/

/* Surface(
        modifier = modifier.padding(4.dp),
        shape = RoundedCornerShape(50.dp),
        elevation = 1.dp,
        color  = colorResource(id = R.color.header1),
    ) {
        Surface(
            elevation = 1.dp,
            shape = RoundedCornerShape(60.dp,60.dp,monthTransitionState.cornerRadius,60.dp),
            onClick = { onMonthSelect(name) },
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                content()
            }
        }
    }*/

@ExperimentalMaterialApi

@Composable
private fun DateSurface( color: Color,
                         date: Int,
                         modifier: Modifier,
                         onDateSelect: (Int) -> Unit,
                         selectedDates: List<Int>,
                         content: @Composable () -> Unit
){
    val dateTransitionState = dateTransition(date = date,selectedDates = selectedDates)

    /*Surface( modifier = modifier.padding(4.dp),
        color  = colorResource(id = R.color.may),
        shape= RoundedCornerShape(8.dp),
        elevation = 1.dp) {

        Surface(
            onClick = { onDateSelect(date) },
            color = color,
            shape = RoundedCornerShape(4.dp, dateTransitionState.cornerRadius,4.dp,4.dp),
            elevation = 1.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    }*/

    Box(contentAlignment = Alignment.Center){
        Surface( modifier = modifier.padding(4.dp),
            color  = colorResource(id = R.color.sep),
            shape= RoundedCornerShape(8.dp),
            elevation = 1.dp) {

        }
        Surface(modifier = modifier.padding(4.dp),
            onClick = { onDateSelect(date) },
            color = color,
            shape = RoundedCornerShape(4.dp, dateTransitionState.cornerRadius,4.dp,4.dp),
            elevation = 1.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }

    }

}

/*
Surface(
        modifier = modifier.padding(4.dp),
        shape = RoundedCornerShape(50.dp),
        elevation = 1.dp,
        color  = colorResource(id = R.color.header1),
    ) {
        Surface(
            elevation = 1.dp,
            shape = RoundedCornerShape(60.dp,60.dp,monthTransitionState.cornerRadius,60.dp),
            onClick = { onMonthSelect(name) },
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                content()
            }
        }
    }
 */

@ExperimentalMaterialApi
@Composable
private fun WeekSurface(color: Color,
                        onWeekSelect: (String) -> Unit,
                        selectedWeeks: List<String>,
                        name: String,
                        modifier: Modifier,
                        content: @Composable () -> Unit
){
    val weekTransitionState = weekTransition(week = name,selectedWeeks = selectedWeeks)

    Box(contentAlignment = Alignment.Center){
        Surface( modifier = modifier.padding(4.dp),
            color  = colorResource(id = R.color.header2),
            shape= RoundedCornerShape(8.dp),
            elevation = 1.dp) {

        }
        Surface(modifier = modifier.padding(4.dp),
            onClick = { onWeekSelect(name) },
            color = color,
            shape = RoundedCornerShape(4.dp, weekTransitionState.cornerRadius,4.dp,4.dp),
            elevation = 1.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }

    }

}

@Composable
private fun WeekNumSurface(color: Color,
                        brush: Brush,
                        modifier: Modifier,
                        content: @Composable () -> Unit
){

    Surface(modifier = modifier.padding(4.dp), color = color, shape= RoundedCornerShape(8.dp), elevation = 1.dp) {
        Column(verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
    }

}

@Composable
fun LayoutContainer(modifier: Modifier=Modifier, content: @Composable () -> Unit){

    Layout(
        modifier = modifier.padding(bottom=12.dp),
        content = content
    ) { measurables, constraints ->

        var rowWidths = mutableListOf<Int>()
        var rowWidth = 0
        var maxRowElements = 0
        var layoutHeight = 0
        var layoutWidth = 0
        var rows = 0

        val placeables = measurables.map { measurable ->

            val placeable = measurable.measure(constraints)

            if((rowWidth + placeable.width) >= constraints.maxWidth)
            {
                rowWidths.add(rowWidth)
                ++rows
                rowWidth = 0

            }
            rowWidth += placeable.width
            placeable
        }

        rowWidths.add(rowWidth)
        ++rows

        layoutHeight = placeables[0].height * rows
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
                it.place(xPosition,yPosition)
                xPosition = xPosition + it.width
                ++rowElements

            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ReminderOptions(
    customReminderContentUpdates: CustomReminderContentUpdates,
    reminderContentUpdates: ReminderContentUpdates,
    ){

    Column() {
        Row(modifier = Modifier
            .padding(8.dp)
            .align(Alignment.End),
            ) {

            TaskSheetBar(
                onReminderSelected = reminderContentUpdates.onReminderSelected,
                onCompleteReminder = customReminderContentUpdates.onCompleteReminder,
                onCloseReminder = customReminderContentUpdates.onCloseReminder,
                onClearReminderValues = reminderContentUpdates.onClearReminderValues
            )
        }

        Row() {
                AnimatedVisibility(visible = reminderContentUpdates.pickaDateSelected) {
                    Dialog(onDismissRequest = {
                        reminderContentUpdates.onPickaDateSelected(false)
                    }) {
                        HataDatePicker(reminderContentUpdates.onPickaDateSelected,reminderContentUpdates.onPickaDate)
                    }
                }
            AnimatedVisibility(visible = reminderContentUpdates.timeSelected) {
                Dialog(onDismissRequest = {
                    reminderContentUpdates.onTimeSelected(false)
                }) {
                    HataTimePicker(reminderContentUpdates.onTimeSelected,reminderContentUpdates.onTimeSelect)
                }
            }
            }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center) {
            HataTaskReminderOptionButton(
                color = colorResource(id = R.color.everyday),
                onReminderOptSelected = {   reminderContentUpdates.onReminderOptSelected(it)
                    reminderContentUpdates.resetReminder()
                                        },
                reminderOptSelected = reminderContentUpdates.reminderOptSelected,
                title = stringResource(id = R.string.everyday),

            )
            HataTaskReminderOptionButton(
                color = colorResource(id = R.color.today),
                onReminderOptSelected = { reminderContentUpdates.onReminderOptSelected(it)
                    reminderContentUpdates.resetReminder()
                                        },
                reminderOptSelected = reminderContentUpdates.reminderOptSelected,
                title = stringResource(id = R.string.today),

            )
            HataTaskReminderOptionButton(
                color = colorResource(id = R.color.tomorow),
                onReminderOptSelected = { reminderContentUpdates.onReminderOptSelected(it)
                    reminderContentUpdates.resetReminder()
                                        },
                reminderOptSelected = reminderContentUpdates.reminderOptSelected,
                title = stringResource(id = R.string.tomorrow),

            )
        }
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {

            Column() {

                HataTaskReminderCustomButton(
                    color = colorResource(id = R.color.custom),
                    onCustomReminderSelect = customReminderContentUpdates.onCustomReminderSelect,
                    reminderOptSelected = reminderContentUpdates.reminderOptSelected,
                    title = stringResource(id = R.string.custom),
                    onCustomReminderInit = customReminderContentUpdates.onCustomReminderInit

                    )
                AnimatedVisibility(visible = customReminderContentUpdates.customreminder.isNotEmpty()) {
                    Text(
                        text = customReminderContentUpdates.customreminder,
                        style = MaterialTheme.typography.overline,
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.custom).copy(alpha = 0.20f).compositeOver(Color.White),
                        modifier = Modifier.padding(start = 28.dp)
                    )
                }
            }

            Column(

            ) {
                HataTaskReminderOptionButton(
                    color = colorResource(id = R.color.pickdate),
                    onReminderOptSelected = { reminderContentUpdates.onReminderOptSelected(it)
                                                reminderContentUpdates.resetReminder()
                                            },
                    reminderOptSelected = reminderContentUpdates.reminderOptSelected,
                    title = stringResource(id = R.string.pickadate),

                    )
                AnimatedVisibility(visible = reminderContentUpdates.pickRemDate.isNotEmpty()) {
                    Text(
                        text = reminderContentUpdates.pickRemDate,
                        style = MaterialTheme.typography.overline,
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.pickdate).copy(alpha = 0.20f).compositeOver(Color.White),
                        modifier = Modifier.padding(start = 28.dp)
                    )
                }
            }
        }

        Row(modifier = Modifier
            .padding(start = 40.dp)){
           HataTimeButton(timeSelected = reminderContentUpdates.timeSelected,
               onTimeSelected = reminderContentUpdates.onTimeSelected,
               reminderTime = reminderContentUpdates.reminderTime)
        }
    }
}

@Composable
fun TaskSheetBar(
    onReminderSelected: (Boolean) -> Unit,
    onCompleteReminder: () -> Unit,
    onCloseReminder: () -> Unit,
    onClearReminderValues: () -> Unit
){

        HataTaskSheetIconButton(
            onClick = { onClearReminderValues() },
            painter = painterResource(id = R.drawable.ic_baseline_undo_24),
            contentDescription = stringResource(id = R.string.clear))
        HataTaskSheetIconButton(
            onClick = {
                        onReminderSelected(false)
                        onCompleteReminder()
                      },
            painter = painterResource(id = R.drawable.ic_baseline_done_24),
            contentDescription = stringResource(id = R.string.done))
        HataTaskSheetIconButton(
            onClick = {
                        onReminderSelected(false)
                        onCloseReminder()
                      },
            painter = painterResource(id = R.drawable.ic_baseline_close_24),
            contentDescription = stringResource(id = R.string.close))

}

@Composable
fun ReminderScreenDivider(modifier: Modifier = Modifier) {
    Divider(color = MaterialTheme.colors.background, thickness = 1.dp, modifier = modifier)
}

private val CELL_SIZE = 48.dp
private val WEEK_CELL_SIZE = 48.dp
private val WEEK_NUM_CELL_SIZE = 48.dp
private val MONTH_CORNER = 60.dp
public val CALENDAR_DAY_COL_SIZE = 56.dp

enum class CalMonths {
    Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
}

private val monthColorMap = mapOf(
    CalMonths.Jan to jan,
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


fun Modifier.verticalGradientScrim(
    color: Color,
    @FloatRange(from = 0.0, to = 1.0) startYPercentage: Float = 0f,
    @FloatRange(from = 0.0, to = 1.0) endYPercentage: Float = 1f,
    decay: Float = 1.0f,
    numStops: Int = 16
): Modifier = composed {
    val colors = remember(color, numStops) {
        if (decay != 1f) {
            // If we have a non-linear decay, we need to create the color gradient steps
            // manually
            val baseAlpha = color.alpha
            List(numStops) { i ->
                val x = i * 1f / (numStops - 1)
                val opacity = x.pow(decay)
                color.copy(alpha = baseAlpha * opacity)
            }
        } else {
            // If we have a linear decay, we just create a simple list of start + end colors
            listOf(color.copy(alpha = 0f), color)
        }
    }

    var height by remember { mutableStateOf(0f) }
    val brush = remember(color, numStops, startYPercentage, endYPercentage, height) {
        Brush.verticalGradient(
            colors = colors,
            startY = height * startYPercentage,
            endY = height * endYPercentage
        )
    }

    drawBehind {
        height = size.height
        drawRect(brush = brush)
    }
}

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
