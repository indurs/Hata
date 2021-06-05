package com.oi.hata.ui

import android.text.Layout
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import com.oi.hata.R
import com.oi.hata.common.reminder.data.local.model.ReminderMaster
import com.oi.hata.common.reminder.ui.ReminderViewModel
import com.oi.hata.common.ui.components.DateChip
import com.oi.hata.common.ui.components.HataDatePicker
import com.oi.hata.common.ui.components.ReminderItem
import com.oi.hata.common.ui.reminder.ReminderOptions
import com.oi.hata.common.util.ReminderUtil
import kotlinx.coroutines.launch

enum class HataHomeScreens(val title: String) {
    Today("Today"),
    Tomorrow("Tomorrow"),
    Calendar("Calendar")
}

enum class HataHomeBottomMenus(val title:String){
    Task("Task"),
    Travel("Travel"),
    Diary("Diary")
}


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun HomeScreen(scaffoldState: ScaffoldState = rememberScaffoldState(),
               homeViewModel: HomeViewModel,
               reminderViewModel: ReminderViewModel,
               onClickReminder: () -> Unit,
                ){

    val todayRemindersState = reminderViewModel.getTodaysReminders().collectAsState(initial = ArrayList())
    //var (taskselected,onTaskSelected) = remember { mutableStateOf(false) }
    var (diaryselected,onDiarySelected) = remember { mutableStateOf(false) }
    var (travelselected,onTravelSelected) = remember { mutableStateOf(false) }
    //var (reminderselected,onReminderSelected) = remember { mutableStateOf(false) }

    Log.d("reminderOptSelected >>>>>>>>*","*****************>>>>>>>>>>"+reminderViewModel.reminderOptSelected)

   HomeScreen(
       currenttab = homeViewModel.currentTab,
       onTabChange = { homeViewModel.onSelectTab(it) },
       scaffoldState = scaffoldState,
       nocontent = true,
       reminderTxt = reminderViewModel.reminderTxt,
       reminder = reminderViewModel.reminder,
       onReminderTxtChange = { reminderViewModel.onReminderTxtChange(it) },
       onDueDateSelect = { year, month, day -> reminderViewModel.onDueDateSelect(year = year,month = month,day = day) },
       onTimeSelect = {hour,minute,am -> reminderViewModel.onTimeSelect(hour,minute,am)},
       reminderTime = reminderViewModel.reminderTime,
       onTimeSelected = reminderViewModel::onTimeSelected,
       onClickReminder = onClickReminder,
       onSaveReminder = { reminderViewModel.saveReminder()},
       dueDateSelected = reminderViewModel.dueDateSelected,
       dueDate = reminderViewModel.reminderDueDate,
       todayReminders = todayRemindersState.value,
       taskselected = reminderViewModel.taskselected,
       onTaskSelected = reminderViewModel::onTaskSelected,
       onDiarySelected = onDiarySelected,
       travelselected = travelselected,
       onTravelSelected = onTravelSelected,
       reminderSelected = reminderViewModel.reminderSelected,
       onReminderSelected = reminderViewModel::onReminderSelected,
       onReminderOptSelected = reminderViewModel::onReminderOptionSelected ,
       reminderOptSelected = reminderViewModel.reminderOptSelected,
       onPickaDate = { year, month, day -> reminderViewModel.onPickaDateSelect(year = year,month = month, day = day) },
       pickaDateSelected = reminderViewModel.pickDateSelected,
       onPickaDateSelected = reminderViewModel::onPickaDateSelected,
       pickRemDate = reminderViewModel.pickAdate,
       onReminderCustomClick = reminderViewModel::onReminderCustomClick,
       timeSelected = reminderViewModel.reminderTimeSelected
    )

}


@ExperimentalAnimationApi

@ExperimentalMaterialApi
@Composable
private fun HomeScreen(
                       currenttab: HataHomeScreens,
                       onTabChange: (HataHomeScreens) -> Unit,
                       scaffoldState: ScaffoldState,
                       nocontent: Boolean,
                       reminderTxt: String,
                       reminder: String,
                       onReminderTxtChange: (String) -> Unit,
                       onClickReminder: () -> Unit,
                       onDueDateSelect: (year: Int,month: Int,day:Int) -> Unit,
                       onTimeSelect: (hour:Int,minute:Int,am:Boolean) -> Unit,
                       onTimeSelected: (Boolean) -> Unit,
                       reminderTime: String,
                       onSaveReminder: () -> Unit,
                       dueDateSelected: Boolean,
                       dueDate: String,
                       todayReminders: List<ReminderMaster>,
                       taskselected: Boolean,
                       onTaskSelected: (Boolean) -> Unit,
                       onDiarySelected: (Boolean) -> Unit,
                       travelselected: Boolean,
                       onTravelSelected: (Boolean) -> Unit,
                       reminderSelected: Boolean,
                       onReminderSelected: (Boolean) -> Unit,
                       onReminderOptSelected: (String) -> Unit,
                       reminderOptSelected: String,
                       onPickaDate: (year: Int,month: Int, day: Int) -> Unit,
                       pickaDateSelected: Boolean,
                       onPickaDateSelected: (Boolean) -> Unit,
                       pickRemDate: String,
                       onReminderCustomClick: () -> Unit,
                       timeSelected: Boolean

) {


    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {},
        topBar = {
        },
        bottomBar = {
            BottomBar(reminderTxt,reminder,onReminderTxtChange,onClickReminder,
                onSaveReminder,onDueDateSelect,onTimeSelect,reminderTime,onTimeSelected,
                timeSelected,dueDateSelected,dueDate,
                taskselected,onTaskSelected,onDiarySelected,travelselected,
                onTravelSelected,reminderSelected,
                onReminderSelected,onReminderOptSelected,reminderOptSelected,onPickaDate,
                pickaDateSelected,onPickaDateSelected,pickRemDate,onReminderCustomClick
                )
                    },
        content= {

            Column() {
                Surface(modifier = Modifier.fillMaxSize()) {

                    HomeTabContent(
                        currentTab = currenttab,
                        onTabChange,
                        tabs = HataHomeScreens.values().map{it.title},
                        scaffoldState = scaffoldState,
                        nocontent = nocontent,
                        todayReminders = todayReminders,
                        reminderSelected = reminderSelected,
                        onReminderSelected = onReminderSelected

                    )

                    /*Column(verticalArrangement = Arrangement.Bottom) {
                        BottomBar(reminderTxt,onReminderTxtChange,onClickReminder,onSaveReminder,onDueDateSelect,onTimeSelect,dueDateSelected,dueDate)
                    }*/

                }
            }
        }
    )

}


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun HomeTabContent(currentTab: HataHomeScreens,
                           onTabChange: (HataHomeScreens) -> Unit,
                           tabs: List<String>,
                           scaffoldState: ScaffoldState,
                           nocontent: Boolean,
                           todayReminders: List<ReminderMaster>,
                           reminderSelected: Boolean,
                           onReminderSelected: (Boolean) -> Unit
                            ){

    val selectedTabIndex = currentTab.ordinal

    val coroutineScope = rememberCoroutineScope()
    Column {


                Row(modifier = Modifier
                    .background(color = MaterialTheme.colors.surface)
                    .fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                    Image(modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp),painter = painterResource(R.drawable.cloudy), contentDescription = null)
                }

            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                indicator = {},
                divider = {},


                ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick =  { onTabChange(HataHomeScreens.values()[index]) },
                        //text = { tabContent.homescreen.title }
                    ){
                        ChoiceChipContent(
                            text = tab,
                            selected = index == selectedTabIndex,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row() {
                when(currentTab){
                    HataHomeScreens.Today -> {
                        TodayScreen(todayReminders)
                    }
                    HataHomeScreens.Tomorrow -> {
                        TomorrowScreen()
                    }
                }
            }

    }

}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
private fun BottomBar(reminderTxt: String,
                      reminder: String,
                      onReminderTxtChange: (String) -> Unit,
                      onClickReminder: () -> Unit,
                      onSaveReminder: () -> Unit,
                      onDueDateSelect: (year: Int,month: Int,day:Int) -> Unit,
                      onTimeSelect: (hour:Int,minute:Int,am:Boolean) -> Unit,
                      reminderTime: String,
                      onTimeSelected: (Boolean) -> Unit,
                      timeSelected: Boolean,
                      dueDateSelected: Boolean,
                      dueDate: String,
                      taskselected: Boolean,
                      onTaskSelected: (Boolean) -> Unit,
                      onDiarySelected: (Boolean) -> Unit,
                      travelselected: Boolean,
                      onTravelSelected: (Boolean) -> Unit,
                      reminderSelected: Boolean,
                      onReminderSelected: (Boolean) -> Unit,
                      onReminderOptSelected: (String) -> Unit,
                      reminderOptSelected: String,
                      onPickaDate: (year: Int,month: Int, day: Int) -> Unit,
                      pickaDateSelected: Boolean,
                      onPickaDateSelected: (Boolean) -> Unit,
                      pickRemDate: String,
                      onReminderCustomClick: () -> Unit
                    ){


    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colors.surface.copy(alpha = 0.90f).compositeOver(Color.White),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Column() {
            AnimatedVisibility(visible = reminderSelected && taskselected,) {

                Row(
                ) {
                        ReminderOptions(
                            onReminderOptSelected = onReminderOptSelected,
                            reminderOptSelected = reminderOptSelected,
                            onReminderSelected = onReminderSelected,
                            onPickaDate = onPickaDate,
                            pickaDateSelected = pickaDateSelected,
                            onPickaDateSelected = onPickaDateSelected,
                            pickRemDate = pickRemDate,
                            onClickReminder = onClickReminder,
                            reminder = reminder,
                            onReminderCustomClick = onReminderCustomClick,
                            reminderTime = reminderTime,
                            onTimeSelected = onTimeSelected,
                            timeSelected = timeSelected,
                            onTimeSelect = onTimeSelect
                        )
                }
            }

            AnimatedVisibility(visible = taskselected,

                ) {
                Row {
                    TaskSheet(reminderTxt,onReminderTxtChange,onClickReminder,
                        onDueDateSelect,onTimeSelect,onSaveReminder,dueDateSelected,dueDate,reminderSelected,onReminderSelected,
                        taskselected,onTaskSelected)
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment =  Alignment.CenterVertically) {

                TaskChip(text = "Task", taskselected,onTaskSelected = {  onTaskSelected(it) },reminderSelected,onReminderSelected = { onReminderSelected(it) } )
                Spacer(modifier = Modifier.width(20.dp))
                ChipContent(text = "Diary", taskselected,onSelected = {  onDiarySelected(it) } )
                Spacer(modifier = Modifier.width(20.dp))
                ChipContent(text = "Travel", travelselected,onSelected = {  onTravelSelected(it) } )
            }

        }

    }
}


@Composable
private fun TodayScreen(todayreminders: List<ReminderMaster>){

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Tasks",
            style = MaterialTheme.typography.subtitle1,
            color = colorResource(id = R.color.taskdivider)
        )
        Surface(color = MaterialTheme.colors.surface.copy(alpha = 0.90f).compositeOver(Color.White),elevation = 2.dp,modifier = Modifier.padding(top = 4.dp)) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()

                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                todayreminders.mapIndexed { index, reminderMaster ->
                    item{
                        Column() {
                            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier
                                .fillMaxWidth()

                            ){

                                Text(text = reminderMaster.reminderText,modifier = Modifier.padding(16.dp),style = MaterialTheme.typography.body2,color = Color.White)

                            }

                        }
                    }
                    if(index < (todayreminders.size - 1)){
                        item{
                            Divider(color = colorResource(id = R.color.taskdivider)
                                , thickness = 0.8.dp, modifier = Modifier.padding(start = 8.dp,end = 8.dp))

                        }
                    }

                }
            }
        }
    }

}

@Composable
private fun TomorrowScreen(){

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Spacer(modifier = Modifier.height(50.dp))
        Text(text = "Tomorrow")

    }

}

@Composable

private fun ChoiceChipContent(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
) {
    Surface(
        color = when {
            selected -> MaterialTheme.colors.primary.copy(alpha = 0.08f)
            else -> MaterialTheme.colors.surface.copy(alpha = 0.30f)
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier,

    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun ChipContent(
    text: String,
    selected: Boolean,
    onSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    Surface(
        color = when {
            selected -> MaterialTheme.colors.surface.copy(alpha = 0.90f).compositeOver(Color.White)
            else -> MaterialTheme.colors.surface.copy(alpha = 0.50f)
        },
        contentColor = when {
            selected -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSurface
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier.clickable {
            if(selected)
                onSelected(false)
            else
                onSelected(true)
                                      },
    ) {

        Text(
            text = text,
            color= when { selected -> colorResource(id = R.color.taskdivider)
                            else -> Color.White
                 },
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun TaskChip(
    text: String,
    taskselected: Boolean,
    onTaskSelected: (Boolean) -> Unit,
    reminderSelected: Boolean,
    onReminderSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    Surface(
        color = when {
            taskselected -> MaterialTheme.colors.surface.copy(alpha = 0.90f).compositeOver(Color.White)
            else -> MaterialTheme.colors.surface.copy(alpha = 0.50f)
        },
        contentColor = when {
            taskselected -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSurface
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier.clickable {
            if(taskselected) {
                onTaskSelected(false)
                onReminderSelected(false)
            }
            else
                onTaskSelected(true)
        },
    ) {

        Text(
            text = text,
            color= when { taskselected -> colorResource(id = R.color.taskdivider)
                else -> Color.White
            },
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun TaskSheet(reminderTxt: String,
                      onReminderTxtChange: (String) -> Unit,
                      onClickReminder: () -> Unit,
                      onDueDateSelect: (year: Int,month: Int,day:Int) -> Unit,
                      onTimeSelect: (hour:Int,minute:Int,am:Boolean) -> Unit,
                      onSaveReminder: () -> Unit,
                      dueDateSelected: Boolean,
                      dueDate: String,
                      reminderSelected: Boolean,
                      onReminderSelected: (Boolean) -> Unit,
                      taskselected: Boolean,
                      onTaskSelected: (Boolean) -> Unit,
                    ){

    var (datePickerSelected,onDatePickerSelected) = remember { mutableStateOf(false) }

    Surface(modifier = Modifier
        .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 4.dp,topEnd = 4.dp),
        color = MaterialTheme.colors.surface.copy(alpha = 0.12f),
    ) {
        Column(modifier = Modifier.padding(start=24.dp)) {
            Row() {
                AnimatedVisibility(visible = datePickerSelected) {
                    Dialog(onDismissRequest = {
                        onDatePickerSelected(false)
                    }) {
                        HataDatePicker(onDatePickerSelected,onDueDateSelect)
                    }
                }
            }

            AnimatedVisibility(visible = !reminderSelected) {
                Row(){
                    OutlinedTextField(
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = Color.White.copy(alpha = 0.90f).compositeOver(Color.Black)
                        ),
                        value = reminderTxt,
                        onValueChange = { onReminderTxtChange(it) },
                        enabled =  when { (reminderSelected) -> false
                            else -> true
                        }

                    )
                    IconButton(
                        enabled =  when { (reminderSelected) -> false
                            else -> true
                        },
                        onClick = { onSaveReminder() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_action_save),
                            tint = Color.White.copy(alpha = 0.90f).compositeOver(Color.Black),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 16.dp),

                            )
                    }
                }
            }

            Row(){
                Column() {
                    IconButton( enabled =  when { (reminderSelected) -> false
                        else -> true
                    },
                        onClick = {
                        if(datePickerSelected)
                            onDatePickerSelected(false)
                        else
                            onDatePickerSelected(true)

                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_action_pick_date),
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.90f).compositeOver(Color.Black),
                            modifier = Modifier
                                .padding(start = 8.dp)

                        )

                    }
                    AnimatedVisibility(visible = dueDateSelected) {
                        DateChip(text = dueDate)
                    }
                }
                
                IconButton( enabled =  when { (reminderSelected) -> false
                                    else -> true
                                },
                    onClick = {  }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_action_tag),
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.90f).compositeOver(Color.Black),
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )
                }

                /*IconButton(onClick = { onClickReminder() }) {
                    Icon(imageVector = Icons.Filled.Notifications,
                        tint = colorResource(id = R.color.taskdivider),
                        contentDescription = "Reminder")
                }*/

                IconButton(onClick = {
                    if(reminderSelected) {
                        onReminderSelected(false)
                    }
                    else {
                        onReminderSelected(true)
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Notifications,
                        tint = Color.White.copy(alpha = 0.90f).compositeOver(Color.Black),
                        contentDescription = "Reminder")
                }
            }
        }

    }
}



/*@Composable
fun DatePicker(){
    AndroidView(factory = { context ->
        MaterialDatePicker.Builder.datePicker().build().apply { addOnPositiveButtonClickListener { println("DAte>>>>>>>>") } }.requireView()
    }) {

    }

}*/

/*@Preview
@Composable
fun SamplePreview(){
    SampleColumn()
}*/

data class TaskContentUpdates(
    val onReminderTxtChange: (String) -> Unit,
    val onClickReminder: () -> Unit,
    val onDueDateSelect: (year: Int,month: Int,day:Int) -> Unit,
    val onTimeSelect: (hour:Int,minute:Int,am:Boolean) -> Unit,
    val onTimeSelected: (Boolean) -> Unit,
)



class TabContent(val homescreen: HataHomeScreens, val content: @Composable () -> Unit)
