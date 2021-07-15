package com.oi.hata.ui

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
fun HomeScreen(scaffoldState: ScaffoldState = rememberScaffoldState(),
               homeViewModel: HomeViewModel,
               onTaskTabSelected: () -> Unit
){

    val todayRemindersState = homeViewModel.getTodaysReminders().collectAsState(initial = ArrayList())
    //var (taskselected,onTaskSelected) = remember { mutableStateOf(false) }
    var (diaryselected,onDiarySelected) = remember { mutableStateOf(false) }
    var (travelselected,onTravelSelected) = remember { mutableStateOf(false) }
    //var (reminderselected,onReminderSelected) = remember { mutableStateOf(false) }

    HomeScreen(
        onTaskTabSelected = onTaskTabSelected,
        currenttab = homeViewModel.currentTab,
        onTabChange = { homeViewModel.onSelectTab(it) },
        scaffoldState = scaffoldState,
        nocontent = true,
        todayReminders = todayRemindersState.value,
        diaryselected = diaryselected,
        onDiarySelected = onDiarySelected,
        travelselected = travelselected,
        onTravelSelected = onTravelSelected,
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
    todayReminders: List<Task>,
    diaryselected: Boolean,
    onDiarySelected: (Boolean) -> Unit,
    travelselected: Boolean,
    onTravelSelected: (Boolean) -> Unit,
    onTaskTabSelected: () -> Unit,
) {

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {},
        topBar = {
        },
        bottomBar = {
            BottomBar(
                diaryselected = diaryselected,
                onDiarySelected = onDiarySelected,
                travelselected = travelselected,
                onTravelSelected = onTravelSelected,
                onTaskTabSelected = onTaskTabSelected
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
                    )

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
                    TodayScreen(todayReminders,)
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
private fun BottomBar(
                      diaryselected: Boolean,
                      onDiarySelected: (Boolean) -> Unit,
                      travelselected: Boolean,
                      onTravelSelected: (Boolean) -> Unit,
                      onTaskTabSelected: () -> Unit,

){


    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colors.surface.copy(alpha = 0.90f).compositeOver(Color.White),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Column() {

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment =  Alignment.CenterVertically) {

                TaskChip(text = "Task",
                    onTaskTabSelected =  onTaskTabSelected
                )
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
                        TaskItem(task = task,onTaskItemClick = {  },taskselected = true)
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
    taskselected: Boolean = false,
    onTaskTabSelected: () -> Unit
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

            onTaskTabSelected()
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
    val onTaskTabSelected: () -> Unit,
    val onCloseTask: () -> Unit
)

class TabContent(val homescreen: HataHomeScreens, val content: @Composable () -> Unit)

