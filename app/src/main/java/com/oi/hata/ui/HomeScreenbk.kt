/*package com.oi.hata.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.oi.hata.R
import com.oi.hata.common.reminder.data.local.model.HataReminder
import com.oi.hata.common.reminder.ui.ReminderViewModel
import com.oi.hata.common.reminder.ui.monthTransition
import com.oi.hata.common.ui.HataTaskSheetIconButton
import com.oi.hata.common.ui.components.DateChip
import com.oi.hata.common.ui.components.HataDatePicker
import com.oi.hata.common.ui.reminder.ReminderOptions
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.data.model.Task
import com.oi.hata.task.ui.TaskItem
import com.oi.hata.task.ui.taskTransition

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
fun HomeScreenbk(scaffoldState: ScaffoldState = rememberScaffoldState(),
               homeViewModel: HomeViewModel,
               reminderViewModel: ReminderViewModel,
               taskViewModel: TaskViewModel,
               onClickReminder: () -> Unit,
){

    val todayRemindersState = homeViewModel.getTodaysReminders().collectAsState(initial = ArrayList())
    //var (taskselected,onTaskSelected) = remember { mutableStateOf(false) }
    var (diaryselected,onDiarySelected) = remember { mutableStateOf(false) }
    var (travelselected,onTravelSelected) = remember { mutableStateOf(false) }
    //var (reminderselected,onReminderSelected) = remember { mutableStateOf(false) }

    HomeScreen(
        currenttab = homeViewModel.currentTab,
        onTabChange = { homeViewModel.onSelectTab(it) },
        scaffoldState = scaffoldState,
        nocontent = true,
        reminderTxt = taskViewModel.taskTxt,
        customreminder = taskViewModel.getCustomReminderTxt(reminderViewModel.reminderOptSelected),
        reminder = taskViewModel.getReminderTxt(),
        reminderTime = reminderViewModel.reminderTime,
        dueDateSelected = taskViewModel.dueDateSelected,
        dueDate = taskViewModel.reminderDueDate,
        todayReminders = todayRemindersState.value,
        taskselected = taskViewModel.taskselected,
        diaryselected = diaryselected,
        onDiarySelected = onDiarySelected,
        travelselected = travelselected,
        onTravelSelected = onTravelSelected,
        reminderSelected = reminderViewModel.reminderSelected,
        reminderOptSelected = reminderViewModel.reminderOptSelected,
        pickaDateSelected = reminderViewModel.pickDateSelected,
        pickRemDate = reminderViewModel.pickAdate,
        timeSelected = reminderViewModel.reminderTimeSelected,
        onTaskItemClick = { taskViewModel.onTaskItemClick(it) },
        taskContentUpdates = TaskContentUpdates(
            onReminderTxtChange = { taskViewModel.onTaskTxtChange(it) },
            onClickReminder = onClickReminder,
            onDueDateSelect = { year, month, day -> taskViewModel.onDueDateSelect(year = year,month = month,day = day) },
            onTimeSelect = { hour, minute, am -> reminderViewModel.onTimeSelect(hour,minute,am)},
            onTimeSelected = reminderViewModel::onReminderTimeSelected,
            onSaveReminder = taskViewModel::saveReminder,
            onTaskSelected = taskViewModel::onTaskSelected,
            onReminderSelected = reminderViewModel::onReminderSelected,
            onReminderOptSelected = reminderViewModel::onReminderOptionSelected ,
            resetReminder = taskViewModel::resetReminder,
            onPickaDate = { year, month, day -> reminderViewModel.onPickaDateSelect(year = year,month = month, day = day) },
            onPickaDateSelected = reminderViewModel::onPickaDateSelected,
            onReminderCustomClick = { if(reminderViewModel.reminderOptSelected != ReminderUtil.CUSTOM){
                                        taskViewModel.resetReminder() 
                                      }
                                      reminderViewModel.onReminderCustomClick(taskViewModel.getReminder())
                                    },
            onCompleteReminder = { taskViewModel.saveTaskReminder(reminderViewModel.getReminderValues()) },
            onCloseReminder =  taskViewModel::resetReminder ,
            initReminderValues = { taskViewModel.setReminderFromTaskUIState()
                                    reminderViewModel.initReminderValues(taskViewModel.getTaskReminder())
                                    },
            onClearReminderValues = {
                                        taskViewModel.resetTaskReminder()
                                        reminderViewModel.initReminderValues(taskViewModel.getTaskReminder())
                                    },
            onCloseTask = taskViewModel::resetValues
        )
    )

}

@ExperimentalAnimationApi


@ExperimentalMaterialApi
@Composable
private fun HomeScreen(
    currenttab: HataHomeScreens,
    onTabChange: (HataHomeScreens) -> Unit,
    scaffoldState: ScaffoldState,
    taskContentUpdates: TaskContentUpdates,
    nocontent: Boolean,
    reminderTxt: String,
    customreminder: String,
    reminder: String,
    reminderTime: String,
    dueDateSelected: Boolean,
    dueDate: String,
    todayReminders: List<Task>,
    taskselected: Boolean,
    diaryselected: Boolean,
    onDiarySelected: (Boolean) -> Unit,
    travelselected: Boolean,
    onTravelSelected: (Boolean) -> Unit,
    reminderSelected: Boolean,
    reminderOptSelected: String,
    pickaDateSelected: Boolean,
    pickRemDate: String,
    timeSelected: Boolean,
    onTaskItemClick: (Long) -> Unit

) {


    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {},
        topBar = {
        },
        bottomBar = {
            BottomBar(reminderTxt,customreminder,
                reminder,reminderTime,
                timeSelected,dueDateSelected,dueDate,
                taskselected,diaryselected,onDiarySelected,travelselected,
                onTravelSelected,reminderSelected,
                reminderOptSelected,
                pickaDateSelected,pickRemDate,
                taskContentUpdates
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
                        onReminderSelected = taskContentUpdates.onReminderSelected,
                        onTaskItemClick = onTaskItemClick,
                        taskselected = taskselected
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
                           todayReminders: List<Task>,
                           reminderSelected: Boolean,
                           onReminderSelected: (Boolean) -> Unit,
                           onTaskItemClick: (Long) -> Unit,
                           taskselected: Boolean
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
                    onClick =  {
                        onTabChange(HataHomeScreens.values()[index])
                               },
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
                    TodayScreen(todayReminders,onTaskItemClick,taskselected)
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
                      customreminder: String,
                      reminder:String,
                      reminderTime: String,
                      timeSelected: Boolean,
                      dueDateSelected: Boolean,
                      dueDate: String,
                      taskselected: Boolean,
                      diaryselected: Boolean,
                      onDiarySelected: (Boolean) -> Unit,
                      travelselected: Boolean,
                      onTravelSelected: (Boolean) -> Unit,
                      reminderSelected: Boolean,
                      reminderOptSelected: String,
                      pickaDateSelected: Boolean,
                      pickRemDate: String,
                      taskContentUpdates: TaskContentUpdates
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
                        onReminderOptSelected = taskContentUpdates.onReminderOptSelected,
                        reminderOptSelected = reminderOptSelected,
                        onReminderSelected = taskContentUpdates.onReminderSelected,
                        resetReminder = taskContentUpdates.resetReminder,
                        onPickaDate = taskContentUpdates.onPickaDate,
                        pickaDateSelected = pickaDateSelected,
                        onPickaDateSelected = taskContentUpdates.onPickaDateSelected,
                        pickRemDate = pickRemDate,
                        onClickReminder = taskContentUpdates.onClickReminder,
                        customreminder = customreminder,
                        onReminderCustomClick = taskContentUpdates.onReminderCustomClick,
                        reminderTime = reminderTime,
                        onTimeSelected = taskContentUpdates.onTimeSelected,
                        timeSelected = timeSelected,
                        onTimeSelect = taskContentUpdates.onTimeSelect,
                        onCompleteReminder = taskContentUpdates.onCompleteReminder,
                        onCloseReminder = taskContentUpdates.onCloseReminder,
                        onClearReminderValues = taskContentUpdates.onClearReminderValues
                    )
                }
            }

            AnimatedVisibility(visible = taskselected,

                ) {
                Row {
                    TaskSheet(reminderTxt,taskContentUpdates.onReminderTxtChange,customreminder,reminder,taskContentUpdates.onClickReminder,
                        taskContentUpdates.onDueDateSelect,taskContentUpdates.onTimeSelect,taskContentUpdates.onSaveReminder,
                        dueDateSelected,dueDate,reminderSelected,taskContentUpdates.onReminderSelected,
                        taskselected,diaryselected,taskContentUpdates.onTaskSelected,taskContentUpdates.onReminderOptSelected,
                        taskContentUpdates.initReminderValues,taskContentUpdates.onCloseTask)
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment =  Alignment.CenterVertically) {

                TaskChip(text = "Task",
                    taskselected,
                    onTaskSelected = {  taskContentUpdates.onTaskSelected(it) },
                    reminderSelected,
                    onReminderSelected = { taskContentUpdates.onReminderSelected(it) } )
                Spacer(modifier = Modifier.width(20.dp))
                ChipContent(text = "Diary", diaryselected,onSelected = {  onDiarySelected(it) } )
                Spacer(modifier = Modifier.width(20.dp))
                ChipContent(text = "Travel", travelselected,onSelected = {  onTravelSelected(it) } )
            }

        }

    }
}


@ExperimentalMaterialApi
@Composable
private fun TodayScreen(
                        todayreminders: List<Task>,
                        onTaskItemClick: (Long) -> Unit,
                        taskselected: Boolean
){

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Tasks",
            style = MaterialTheme.typography.subtitle1,
            color = colorResource(id = R.color.taskdivider)
        )


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()

                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                todayreminders.mapIndexed { index, task ->
                    item{
                        TaskItem(task = task,onTaskItemClick = { onTaskItemClick(task.id) },taskselected = taskselected)
                    }
                    if(index < (todayreminders.size - 1)){
                        item{
                            Divider(color = colorResource(id = R.color.taskdivider)
                                , thickness = 0.8.dp, )

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
        onClick = { if(selected)
            onSelected(false)
        else
            onSelected(true) }
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
        onClick = {
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
                      customreminder: String,
                      reminder: String,
                      onClickReminder: () -> Unit,
                      onDueDateSelect: (year: Int,month: Int,day:Int) -> Unit,
                      onTimeSelect: (hour:Int,minute:Int,am:Boolean) -> Unit,
                      onSaveReminder: () -> Unit,
                      dueDateSelected: Boolean,
                      dueDate: String,
                      reminderSelected: Boolean,
                      onReminderSelected: (Boolean) -> Unit,
                      taskselected: Boolean,
                      diaryselected: Boolean,
                      onTaskSelected: (Boolean) -> Unit,
                      onReminderOptSelected: (String) -> Unit,
                      initReminderValues: () -> Unit,
                      onCloseTask: () -> Unit

){

    var (datePickerSelected,onDatePickerSelected) = remember { mutableStateOf(false) }
    val taskTransitionState = taskTransition(reminderSelected)

    Surface(modifier = Modifier
        .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 4.dp,topEnd = 4.dp),
        color = MaterialTheme.colors.surface.copy(alpha = 0.12f),
    ) {
        Column(modifier = Modifier.padding(start=8.dp)) {
            Row() {
                AnimatedVisibility(visible = datePickerSelected) {
                    Dialog(onDismissRequest = {
                        onDatePickerSelected(false)
                    }) {
                        HataDatePicker(onDatePickerSelected,onDueDateSelect)
                    }
                }
            }
            Row(modifier = Modifier.align(Alignment.End).padding(top=12.dp)) {
                HataTaskSheetIconButton(
                    onClick = {
                        onTaskSelected(false)
                        onCloseTask()
                        onReminderOptSelected(ReminderUtil.NONE)
                    },
                    painter = painterResource(id = R.drawable.ic_baseline_close_24),
                    contentDescription = stringResource(id = R.string.close))
            }


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
                    modifier = Modifier.align(Alignment.Bottom),
                    enabled =  when { (reminderSelected) -> false
                        else -> true
                    },
                    onClick = {
                                onTaskSelected(false)
                                onSaveReminder()
                                onReminderOptSelected(ReminderUtil.NONE)
                                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_action_save),
                        tint = Color.White.copy(alpha = taskTransitionState.contentAlpha).compositeOver(Color.Black),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp,top = 20.dp),

                        )
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
                            tint = Color.White.copy(alpha = taskTransitionState.contentAlpha).compositeOver(Color.Black),
                            modifier = Modifier
                                .padding(start = 8.dp)

                        )

                    }
                    AnimatedVisibility(visible = dueDateSelected) {
                        //DateChip(text = dueDate)
                        Text(
                            text = dueDate,
                            color = colorResource(id = R.color.pickdate),
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(start = 8.dp,bottom = 8.dp)
                        )
                    }
                }

                IconButton( enabled =  when { (reminderSelected) -> false
                    else -> true
                },
                    onClick = {  }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_action_tag),
                        contentDescription = null,
                        tint = Color.White.copy(alpha = taskTransitionState.contentAlpha).compositeOver(Color.Black),
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )
                }

                /*IconButton(onClick = { onClickReminder() }) {
                    Icon(imageVector = Icons.Filled.Notifications,
                        tint = colorResource(id = R.color.taskdivider),
                        contentDescription = "Reminder")
                }*/
                Column() {
                    IconButton(onClick = {
                        if(reminderSelected) {
                            onReminderSelected(false)
                        }
                        else {
                            initReminderValues()
                            onReminderSelected(true)
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Notifications,
                            tint = Color.White.copy(alpha = 0.90f).compositeOver(Color.Black),
                            modifier = Modifier
                                .padding(start = 16.dp),
                            contentDescription = "Reminder")
                    }

                    AnimatedVisibility(visible = reminder.isNotEmpty()) {
                        Text(
                            text = reminder,
                            color = colorResource(id = R.color.pickdate),
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(start = 8.dp,bottom = 8.dp)
                        )
                    }
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
    val onDueDateSelect: (year: Int, month: Int, day:Int) -> Unit,
    val onTimeSelect: (hour:Int, minute:Int, am:Boolean) -> Unit,
    val onTimeSelected: (Boolean) -> Unit,
    val onSaveReminder: () -> Unit,
    val onTaskSelected: (Boolean) -> Unit,
    val onReminderSelected: (Boolean) -> Unit,
    val onReminderOptSelected: (String) -> Unit,
    val onPickaDate: (year: Int, month: Int, day: Int) -> Unit,
    val onPickaDateSelected: (Boolean) -> Unit,
    val onReminderCustomClick: () -> Unit,
    val resetReminder: () -> Unit,
    val onCompleteReminder: () -> Unit,
    val onCloseReminder: () -> Unit,
    val initReminderValues: () -> Unit,
    val onClearReminderValues: () -> Unit,
    val onCloseTask: () -> Unit
)

class TabContent(val homescreen: HataHomeScreens, val content: @Composable () -> Unit)

*/