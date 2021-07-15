package com.oi.hata.common.reminder.ui

import android.text.Layout
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import com.oi.hata.R
import com.oi.hata.common.reminder.data.local.model.ReminderMaster
import com.oi.hata.common.reminder.ui.ReminderViewModel
import com.oi.hata.common.ui.components.DateChip
import com.oi.hata.common.ui.components.HataDatePicker
import com.oi.hata.common.ui.components.ReminderItem
import com.oi.hata.ui.HomeViewModel
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

/*
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun HomeScreen(scaffoldState: ScaffoldState = rememberScaffoldState(),
               homeViewModel: HomeViewModel,
               reminderViewModel: ReminderViewModel,
               onClickReminder: () -> Unit,
                ){

    val todayRemindersState = reminderViewModel.getTodaysReminders().collectAsState(initial = ArrayList())

   HomeScreen(
       currenttab = homeViewModel.currentTab,
       onTabChange = { homeViewModel.onSelectTab(it) },
       scaffoldState = scaffoldState,
       nocontent = true,
       reminderTxt = reminderViewModel.reminderTxt,
       onReminderTxtChange = { reminderViewModel.onReminderTxtChange(it) },
       onDueDateSelect = { year, month, day -> reminderViewModel.onDueDateSelect(year = year,month = month,day = day) },
       onTimeSelect = {hour,minute,am -> reminderViewModel.onTimeSelect(hour,minute,am)},
       onClickReminder = onClickReminder,
       onSaveReminder = { reminderViewModel.saveReminder()},
       dueDateSelected = reminderViewModel.dueDateSelected,
       dueDate = reminderViewModel.reminderDueDate,
       todayReminders = todayRemindersState.value
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
                       onReminderTxtChange: (String) -> Unit,
                       onClickReminder: () -> Unit,
                       onDueDateSelect: (year: Int,month: Int,day:Int) -> Unit,
                       onTimeSelect: (hour:Int,minute:Int,am:Boolean) -> Unit,
                       onSaveReminder: () -> Unit,
                       dueDateSelected: Boolean,
                       dueDate: String,
                       todayReminders: List<ReminderMaster>
                       
) {

    Log.d("HomeScreen", "HomeScreen>>>>>>>>3333>>>>>>>>>")
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {},
        topBar = {
        },
        bottomBar = { },
        content= {
            Surface(modifier = Modifier.fillMaxSize(),) {
                HomeTabContent(
                    currentTab = currenttab,
                    onTabChange,
                    tabs = HataHomeScreens.values().map{it.title},
                    scaffoldState = scaffoldState,
                    nocontent = nocontent,
                    todayReminders = todayReminders
                    )

                Column(verticalArrangement = Arrangement.Bottom) {
                    BottomBar(reminderTxt,onReminderTxtChange,onClickReminder,onSaveReminder,onDueDateSelect,onTimeSelect,dueDateSelected,dueDate)
                }
            }

        }
    )

}

@Composable
fun TopBar(){





}

@ExperimentalMaterialApi
@Composable
private fun HomeTabContent(currentTab: HataHomeScreens,
                           onTabChange: (HataHomeScreens) -> Unit,
                           tabs: List<String>,
                           scaffoldState: ScaffoldState,
                           nocontent: Boolean,
                           todayReminders: List<ReminderMaster>
                            ){
    Log.d("HomeTabContent", "HomeScreen>>>>>>>>>>>>>>>>>")
    val selectedTabIndex = currentTab.ordinal

    val coroutineScope = rememberCoroutineScope()
    Column {
        Column() {

                Row(modifier = Modifier
                    .background(color = Color.White)
                    .fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                    Image(modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp),painter = painterResource(R.drawable.cloudy), contentDescription = null)
                }


        }

        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = {},
            divider = {},
            backgroundColor = colorResource(id = R.color.sample)
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

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
private fun BottomBar(reminderTxt: String,
                      onReminderTxtChange: (String) -> Unit,
                      onClickReminder: () -> Unit,
                      onSaveReminder: () -> Unit,
                      onDueDateSelect: (year: Int,month: Int,day:Int) -> Unit,
                      onTimeSelect: (hour:Int,minute:Int,am:Boolean) -> Unit,
                      dueDateSelected: Boolean,
                      dueDate: String
                    ){
    var (taskselected,onTaskSelected) = remember { mutableStateOf(false) }
    var (diaryselected,onDiarySelected) = remember { mutableStateOf(false) }
    var (travelselected,onTravelSelected) = remember { mutableStateOf(false) }

    var (datePickerSelected,onDatePickerSelected) = remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = colorResource(id = R.color.sample).copy(alpha = 0.60f),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Column() {
            AnimatedVisibility(visible = taskselected) {
                Row {
                    TaskSheet(reminderTxt,datePickerSelected,onDatePickerSelected,onReminderTxtChange,onClickReminder,onDueDateSelect,onTimeSelect,onSaveReminder,dueDateSelected,dueDate)
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment =  Alignment.CenterVertically) {

                ChipContent(text = "Task", taskselected,onSelected = {  onTaskSelected(it) } )
                Spacer(modifier = Modifier.width(20.dp))
                ChipContent(text = "Diary", diaryselected,onSelected = {  onDiarySelected(it) } )
                Spacer(modifier = Modifier.width(20.dp))
                ChipContent(text = "Travel", travelselected,onSelected = {  onTravelSelected(it) } )
            }

        }

    }
}

@Composable
private fun TodayScreen(todayreminders: List<ReminderMaster>){
    Column() {
        Text(text = "Tasks", style = MaterialTheme.typography.h5,color = Color.Black, modifier = Modifier.padding(4.dp))
            Surface(color = colorResource(id = R.color.sample).copy(alpha = 0.10f),modifier = Modifier.padding(8.dp)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    items(todayreminders){ reminder ->
                        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier
                            .fillMaxWidth()
                            .padding(1.dp)){
                            Surface(modifier = Modifier
                                .fillMaxWidth(),shape = RoundedCornerShape(2.dp),color = colorResource(id = R.color.sample).copy(alpha = 0.04f)
                            ){
                                Text(text = reminder.reminderText,modifier = Modifier.padding(16.dp),style = MaterialTheme.typography.body2,color = Color.Black)
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
            else -> colorResource(id = R.color.sample).copy(alpha = 0.30f)
        },
        contentColor = when {
            selected -> MaterialTheme.colors.onSurface
            else -> Color.Black
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier,

    ) {
        Text(
            text = text,
            color = when {
                selected -> MaterialTheme.colors.onSurface
                else -> Color.Black
            },
            style = MaterialTheme.typography.body2,
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
            selected -> MaterialTheme.colors.onSurface.copy(alpha = 0.20f)
            else -> colorResource(id = R.color.sample).copy(alpha = 0.90f)
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
            style = MaterialTheme.typography.body2,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}


@ExperimentalAnimationApi
@Composable
private fun TaskSheet(reminderTxt: String,
                      datePickerSelected: Boolean,
                      onDatePickerSelected: (Boolean) -> Unit,
                      onReminderTxtChange: (String) -> Unit,
                      onClickReminder: () -> Unit,
                      onDueDateSelect: (year: Int,month: Int,day:Int) -> Unit,
                      onTimeSelect: (hour:Int,minute:Int,am:Boolean) -> Unit,
                      onSaveReminder: () -> Unit,
                      dueDateSelected: Boolean,
                      dueDate: String
                    ){


    Surface(modifier = Modifier
        .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 4.dp,topEnd = 4.dp),
        color = MaterialTheme.colors.surface.copy(alpha = 0.12f)
    ) {
        Column(modifier = Modifier.padding(start=24.dp)) {
            Row(){
                OutlinedTextField(value = reminderTxt,
                    onValueChange = { onReminderTxtChange(it) },)
                IconButton(onClick = { onSaveReminder() }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_action_save),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 16.dp)

                    )
                }
            }
            AnimatedVisibility(visible = datePickerSelected) {
                Dialog(onDismissRequest = {
                    onDatePickerSelected(false)
                }) {
                    HataDatePicker(onDatePickerSelected,onDueDateSelect)
                }
            }

            Row(){
                Column() {
                    IconButton(onClick = {
                        if(datePickerSelected)
                            onDatePickerSelected(false)
                        else
                            onDatePickerSelected(true)

                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_action_pick_date),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 8.dp)

                        )

                    }
                    AnimatedVisibility(visible = dueDateSelected) {
                        DateChip(text = dueDate)
                    }
                }
                
                IconButton(onClick = { onClickReminder() }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_action_tag),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )
                }
                IconButton(onClick = { onClickReminder() }) {
                    Icon(imageVector = Icons.Filled.Notifications, contentDescription = "Reminder")
                }
            }
        }

    }
}

*/


class TabContent(val homescreen: HataHomeScreens, val content: @Composable () -> Unit)
