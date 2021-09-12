package com.oi.hata.ui


import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oi.hata.R
import com.oi.hata.common.reminder.ui.ReminderViewModel
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.data.model.CalendarColumn
import com.oi.hata.task.data.model.Task
import com.oi.hata.task.ui.*
import com.oi.hata.task.ui.TaskContentUpdates
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.Year
import java.util.*
import kotlin.collections.ArrayList

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
               taskViewModel: TaskViewModel,
               reminderViewModel: ReminderViewModel,
               onTaskTabSelected: () -> Unit,
               onCustomReminderSelect: () -> Unit,

){

    val todayRemindersState = homeViewModel.getReminders(ReminderUtil.TODAY).collectAsState(initial = ArrayList())

    val calendarDateTasksState = homeViewModel.getTasksForCalendarDate(9
        ,homeViewModel.selectedCalendarDate).collectAsState(
        initial = ArrayList()
    )

    val tomorrowRemindersState = homeViewModel.getReminders(ReminderUtil.TOMORROW).collectAsState(initial = ArrayList())
    val tasksForMonthState = homeViewModel.getTasksForMonth(homeViewModel.selectedCalendarMonth!!).collectAsState(
        initial = TreeMap()
    )

    //var (taskselected,onTaskSelected) = remember { mutableStateOf(false) }
    var (diaryselected,onDiarySelected) = remember { mutableStateOf(false) }
    var (travelselected,onTravelSelected) = remember { mutableStateOf(false) }
    //var (reminderselected,onReminderSelected) = remember { mutableStateOf(false) }

    val taskContentUpdates = TaskContentUpdates(taskViewModel = taskViewModel,)
    val taskListItemContentUpdates = TaskListItemContentUpdates(taskViewModel = taskViewModel,false)
    val reminderContentUpdates = ReminderContentUpdates(reminderViewModel = reminderViewModel,taskViewModel = taskViewModel)
    val customReminderContentUpdates = CustomReminderContentUpdates(
                                        reminderViewModel = reminderViewModel,
                                            taskViewModel = taskViewModel,
        onCustomReminderSelect = onCustomReminderSelect
    )

    val calendarContentUpdates = CalendarContentUpdates(
        onSelectCalendarDate = { homeViewModel.onSelectCalendarDate(it) },
        setCalendarView = { homeViewModel.setCalendarView() },
        calendarView = homeViewModel.calendarView,
        monthCalendar = homeViewModel.getMonthCalendarScreen(selectedCalendarMonth = ReminderUtil.monthsNum[homeViewModel.selectedCalendarMonth]!!),
        tasksForMonth = tasksForMonthState.value!!,
        selectedCalendarDate = homeViewModel.selectedCalendarDate,
        selectedCalendarMonth = homeViewModel.selectedCalendarMonth!!,
        onSelectCalendarMonth = { homeViewModel.onSelectCalendarMonth(it) },
        selectedCalendarYear = homeViewModel.selectedCalendarYear
    )

    HomeScreen(
        onTaskTabSelected = onTaskTabSelected,
        currenttab = homeViewModel.currentTab,
        onTabChange = { homeViewModel.onSelectTab(it) },
        scaffoldState = scaffoldState,
        nocontent = true,
        todayReminders = todayRemindersState.value,
        tomorrowReminders = tomorrowRemindersState.value,
        diaryselected = diaryselected,
        onDiarySelected = onDiarySelected,
        travelselected = travelselected,
        onTravelSelected = onTravelSelected,
        onTaskSelected = { taskViewModel.onTaskSelected() },
        taskselected = taskViewModel.taskselected,
        taskContentUpdates = taskContentUpdates,
        reminderContentUpdates = reminderContentUpdates,
        customReminderContentUpdates = customReminderContentUpdates,
        taskListItemContentUpdates = taskListItemContentUpdates,
        calendarContentUpdates = calendarContentUpdates,
        calendarDateTasksState = calendarDateTasksState.value
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
    tomorrowReminders: List<Task>,
    calendarDateTasksState: List<Task>,
    diaryselected: Boolean,
    onDiarySelected: (Boolean) -> Unit,
    travelselected: Boolean,
    onTravelSelected: (Boolean) -> Unit,
    onTaskTabSelected: () -> Unit,
    onTaskSelected: () -> Unit,
    taskselected: Boolean,
    reminderContentUpdates: ReminderContentUpdates,
    customReminderContentUpdates: CustomReminderContentUpdates,
    taskContentUpdates: TaskContentUpdates,
    taskListItemContentUpdates: TaskListItemContentUpdates,
    calendarContentUpdates: CalendarContentUpdates
) {
    var calendarhorscroll = rememberScrollState()

    HomeTabContent(
        currentTab = currenttab,
        onTabChange,
        tabs = HataHomeScreens.values().map { it.title },
        scaffoldState = scaffoldState,
        nocontent = nocontent,
        todayReminders = todayReminders,
        tomorrowReminders = tomorrowReminders,
        onTaskSelected = onTaskSelected,
        taskselected = taskselected,
        taskListItemContentUpdates = taskListItemContentUpdates,
        calhorscrollState = calendarhorscroll,
        calendarContentUpdates = calendarContentUpdates,
        calendarDateTasksState = calendarDateTasksState,
        reminderContentUpdates = reminderContentUpdates,
        taskContentUpdates = taskContentUpdates,
        customReminderContentUpdates = customReminderContentUpdates,
        diaryselected = diaryselected,
        onDiarySelected = onDiarySelected,
        travelselected = travelselected,
        onTravelSelected = onTravelSelected,
        onTaskTabSelected = onTaskTabSelected
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
                           tomorrowReminders: List<Task>,
                           onTaskSelected: () -> Unit,
                           taskselected: Boolean,
                           taskListItemContentUpdates: TaskListItemContentUpdates,
                           calhorscrollState:ScrollState,
                           calendarContentUpdates: CalendarContentUpdates,
                           calendarDateTasksState: List<Task>,
                           reminderContentUpdates: ReminderContentUpdates,
                           customReminderContentUpdates: CustomReminderContentUpdates,
                           taskContentUpdates: TaskContentUpdates,
                           diaryselected: Boolean,
                           onDiarySelected: (Boolean) -> Unit,
                           travelselected: Boolean,
                           onTravelSelected: (Boolean) -> Unit,
                           onTaskTabSelected: () -> Unit,
                           ){
    val selectedTabIndex = currentTab.ordinal

    var tasksScrollState = rememberScrollState()
    var homescrollState = rememberScrollState()

    val tabsOffset = with(LocalDensity.current) { HOME_TABS_OFFSET.toPx() }
    val calyearoffset = with(LocalDensity.current) { (CAL_CALENDAR_OFFSET ).toPx() }

    Box(modifier = Modifier
        .fillMaxSize()
        .scrollable(
            enabled = true,
            state = tasksScrollState,
            orientation = Orientation.Vertical,
            reverseDirection = true
        )
        .background(color = MaterialTheme.colors.background)) {

        Box(modifier = Modifier.fillMaxSize()){
            Spacer(modifier = Modifier.height(58.dp))

            when(currentTab){
                HataHomeScreens.Today -> {
                    TodayScreen(
                        todayreminders = todayReminders,
                        onTaskSelected=onTaskSelected,
                        taskselected = taskselected,
                        taskListItemContentUpdates = taskListItemContentUpdates
                    )
                }
                HataHomeScreens.Tomorrow -> {
                    TodayScreen(
                        todayreminders = tomorrowReminders,
                        onTaskSelected=onTaskSelected,
                        taskselected = taskselected,
                        taskListItemContentUpdates = taskListItemContentUpdates
                    )
                }

                HataHomeScreens.Calendar -> {
                    HataCalendar(
                        calhorscrollState = calhorscrollState,
                        calendarContentUpdates = calendarContentUpdates,
                        calendarDateTasksState = calendarDateTasksState,
                        taskListItemContentUpdates = taskListItemContentUpdates,
                        onTaskSelected = onTaskSelected,
                        taskselected = taskselected,
                        tasksScrollState = tasksScrollState
                    )
                }
            }
        }
        Column(
        ) {
            Box(){
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        modifier = Modifier
                            .background(color = MaterialTheme.colors.surface)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(8.dp),
                            painter = painterResource(R.drawable.cloudy),
                            contentDescription = null
                        )
                    }

                    ScrollableTabRow(
                        selectedTabIndex = selectedTabIndex,
                        indicator = {},
                        divider = {},


                        ) {
                        tabs.forEachIndexed { index, tab ->
                            Tab(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .clipToBounds(),
                                selected = selectedTabIndex == index,
                                onClick = {
                                    onTabChange(HataHomeScreens.values()[index])
                                },
                                //text = { tabContent.homescreen.title }
                            ) {
                                ChoiceChipContent(
                                    text = tab,
                                    selected = index == selectedTabIndex,
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                }
            }

        }

                AnimatedVisibility(
                    visible = taskselected,
                    Modifier.align(Alignment.BottomCenter)
                )
                {
                    ReminderBar(
                        reminderContentUpdates = reminderContentUpdates,
                        customReminderContentUpdates = customReminderContentUpdates,
                        taskContentUpdates = taskContentUpdates,
                        onTaskSelected = { onTaskSelected() },
                        taskselected = taskselected
                    )
                }
                AnimatedVisibility(
                    visible = !taskselected,
                    Modifier.align(Alignment.BottomCenter)
                ) {
                    BottomBar(
                        diaryselected = diaryselected,
                        onDiarySelected = onDiarySelected,
                        travelselected = travelselected,
                        onTravelSelected = onTravelSelected,
                        onTaskTabSelected = onTaskTabSelected
                    )
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


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun TodayScreen(onTaskSelected: () -> Unit,
                        taskListItemContentUpdates: TaskListItemContentUpdates,
                        taskselected: Boolean,
                        todayreminders: List<Task>?,

){

    Column() {
        Spacer(modifier = Modifier.height(TODAY_SCREEN_OFFSET))
        Text(
            modifier = Modifier.padding(start = 12.dp,top = 16.dp),
            text = "Tasks",
            style = MaterialTheme.typography.h5,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(4.dp))
        //var modifier = Modifier.background(color = colorResource(id = R.color.bottombar))
        var color = MaterialTheme.colors.background.copy(alpha=0.90f).compositeOver(Color.White).copy(alpha = 0.50f)
        var tasklistModifier = Modifier.padding(start = 8.dp,end = 8.dp)

        DismissableTasks(
            modifier = Modifier,
            color = color,
            tasklistModifier = tasklistModifier,
            tasks = todayreminders,
            onTaskSelected = onTaskSelected,
            taskselected = taskselected,
            taskListItemContentUpdates = taskListItemContentUpdates
        )
    }

}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun HataCalendar(
    calhorscrollState:ScrollState,
    calendarContentUpdates: CalendarContentUpdates,
    calendarDateTasksState: List<Task>,
    taskListItemContentUpdates: TaskListItemContentUpdates,
    onTaskSelected: () -> Unit,
    taskselected: Boolean,
    tasksScrollState: ScrollState
){

    var monthChoose by rememberSaveable { (mutableStateOf(false)) }

    Box(modifier = Modifier.fillMaxSize()) {

        HataCalendarSection(
            tasksScrollState = tasksScrollState,
            calendarContentUpdates = calendarContentUpdates,
            calhorscrollState = calhorscrollState,
            monthChoose = monthChoose,
            onMonthChoose = { monthChoose = !monthChoose }
        )


        CalendarTasksSection(
            tasksScrollState = tasksScrollState,
            onTaskSelected = onTaskSelected,
            taskListItemContentUpdates = taskListItemContentUpdates,
            taskselected = taskselected,
            calendarDateTasks = calendarDateTasksState,
            date = calendarContentUpdates.selectedCalendarDate,
            month = calendarContentUpdates.selectedCalendarMonth,
            year = calendarContentUpdates.selectedCalendarYear,
            monthChoose = monthChoose
        )

    }

}


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private  fun CalendarTasksSection(
                                monthChoose: Boolean,
                                date:Int,
                                month: String,
                                year: Int,
                                tasksScrollState: ScrollState,
                                onTaskSelected: () -> Unit,
                                taskListItemContentUpdates: TaskListItemContentUpdates,
                                taskselected: Boolean,
                                calendarDateTasks: List<Task>?,
                        ){
        var coroutineScope = rememberCoroutineScope()


        var taskOffset = with(LocalDensity.current) { CAL_TASKS_TITLE_OFFSET.toPx() }
        var offset = (taskOffset-tasksScrollState.value)
        var titileSize = (MIN_TITLE_SIZE + tasksScrollState.value).coerceAtMost(MAX_TITLE_SIZE)

        SideEffect {

            if(offset < 100 ){
                coroutineScope.launch {
                    tasksScrollState.scrollTo(1)
                    offset += tasksScrollState.value
                }

            }
        }
        if(monthChoose)
            offset += 200

        Column(modifier = Modifier
            .graphicsLayer { translationY = offset }
            .scrollable(
                tasksScrollState,
                orientation = Orientation.Vertical,
                reverseDirection = true

            )

            ) {


            TasksHeader(
                date = date,
                month = month,
                year = year,
                titileSize = titileSize,
                color = colorResource(id = R.color.apr)
            )

            //var modifier = Modifier.background(color = colorResource(id = R.color.bottombar).copy(alpha = 0.94f))
            //var color = colorResource(id = R.color.bottombar).copy(alpha=0.98f)
            var color = MaterialTheme.colors.background.copy(alpha=0.90f).compositeOver(Color.White).copy(alpha = 0.30f)
            println("calendarDateTasks" + calendarDateTasks!!.size)

            DismissableTasks(
                modifier = Modifier,
                color = color,
                tasks = calendarDateTasks,
                onTaskSelected = onTaskSelected,
                taskselected = taskselected,
                taskListItemContentUpdates = taskListItemContentUpdates
            )
            Spacer(modifier = Modifier.height(48.dp))
        }

}

@Composable
fun TasksHeader(
    date:Int,
    month: String,
    year: Int,
    titileSize: Int,
    color: Color
){
    var today = GregorianCalendar().get(Calendar.DAY_OF_MONTH)
    Spacer(modifier = Modifier.height(16.dp))
    Row() {
        Text(
            modifier = Modifier.padding(start = 8.dp,bottom = 8.dp),
            text = if(date != 0) date.toString() else "",
            fontSize = titileSize.sp,
            style = MaterialTheme.typography.overline,
            color = if(date == today) color else Color.White
        )
        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = if(date != 0) ReminderUtil.getNumSuffix(date) else "",
            fontSize = titileSize.sp/2,
            style = MaterialTheme.typography.overline,
            color = if(date == today) color else Color.White
        )
        Text(
            modifier = Modifier.padding(start = 6.dp,top=12.dp),
            text = month,
            fontSize = titileSize.sp/2,
            style = MaterialTheme.typography.overline,
            color = if(date == today) color else Color.White
        )

        Text(
            modifier = Modifier.padding(start = 6.dp,top = 12.dp),
            text = year.toString(),
            fontSize = titileSize.sp/2,
            style = MaterialTheme.typography.overline,
            color = if(date == today) color else Color.White
        )
    }

}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun HataCalendarSection(
    tasksScrollState: ScrollState,
    calendarContentUpdates: CalendarContentUpdates,
    calhorscrollState:ScrollState,
    monthChoose: Boolean,
    onMonthChoose: () -> Unit
){
    val calendarOffset = with(LocalDensity.current) { CAL_CALENDAR_OFFSET.toPx() }

    var offset = (calendarOffset - tasksScrollState.value)

    var taskOffset = with(LocalDensity.current) { CAL_TASKS_TITLE_OFFSET.toPx() }
    val calMinOffset = taskOffset/2

    var taskoffset = (taskOffset-tasksScrollState.value)


    AnimatedVisibility(
        visible = taskoffset > calMinOffset,
        enter =
            fadeIn(animationSpec = tween(100))
        ,
        exit =
            fadeOut(animationSpec = tween(700))
        ,
    ) {
        HataCalendarSectionContent(
            tasksScrollState = tasksScrollState,
            calendarContentUpdates = calendarContentUpdates,
            calhorscrollState = calhorscrollState,
            offset = offset,
            monthChoose = monthChoose,
            onMonthChoose = onMonthChoose
        )
    }

}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun HataCalendarSectionContent(
    monthChoose: Boolean,
    tasksScrollState: ScrollState,
    calendarContentUpdates: CalendarContentUpdates,
    calhorscrollState:ScrollState,
    onMonthChoose: () -> Unit,
    offset: Float
){


    Column(
        Modifier
            .graphicsLayer { translationY = offset }
            .fillMaxSize()
            .horizontalScroll(calhorscrollState),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.fillMaxWidth()){
            Column(
                modifier = Modifier.background(
                    shape = RoundedCornerShape(4.dp),
                    color = colorResource(id = R.color.cal_mon_year_sec)),
                horizontalAlignment  = Alignment.CenterHorizontally
            ) {

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)) {
                    CalendarMonthYearSection(
                        calendarContentUpdates = calendarContentUpdates,
                        onMonthChoose = onMonthChoose
                    )
                }


                Spacer(modifier = Modifier.width(CAL_DATE_COL_SIZE * 7))

                AnimatedVisibility(visible = monthChoose) {
                    MonthsSurface(monthChoose = monthChoose,onMonthChoose = onMonthChoose,calendarContentUpdates)
                }

            }
        }

        Row( Modifier.fillMaxWidth()) {
            ReminderUtil.WEEKNAMES.values().forEach {
                CalendarWeekName(week = it.name)
            }

        }
            Column() {
                for (i in 0..5){
                    Row(Modifier.fillMaxWidth(),){
                        for(j in 0..6){
                            var weedDayarray = calendarContentUpdates.monthCalendar[j]

                            if(i < weedDayarray.size){
                                if(calendarContentUpdates.monthCalendar[j][i] == 0){
                                    CalendarColumnItem(date = 0, setCalendarView = {},onSelectCalendarDate = {},calendarColumn = null)
                                }
                            }
                            if(i < weedDayarray.size && calendarContentUpdates.monthCalendar[j][i] != 0 ) {
                                CalendarColumnItem(
                                    date = calendarContentUpdates.monthCalendar[j][i] ,
                                    setCalendarView = calendarContentUpdates.setCalendarView,
                                    onSelectCalendarDate = calendarContentUpdates.onSelectCalendarDate,
                                    calendarColumn = calendarContentUpdates.tasksForMonth[calendarContentUpdates.monthCalendar[j][i]]
                                )
                            }
                        }
                    }
                }
            }
    }

}

@ExperimentalAnimationApi
@Composable
fun CalendarMonthYearSection(
                            onMonthChoose: ()->Unit,
                            calendarContentUpdates: CalendarContentUpdates,
                           ){
    Column(Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row() {
            Column(
                modifier = Modifier.clickable(enabled = true,onClick = {
                    onMonthChoose()
                })
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = colorResource(id = R.color.cal_mon_year),
                    elevation = 2.dp,
                    //border = BorderStroke(1.dp, MaterialTheme.colors.primary)
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp,top=8.dp,end=16.dp,bottom = 8.dp),
                        text = ReminderUtil.monthsAbr[calendarContentUpdates.selectedCalendarMonth].toString(),
                        color = Color.Black,
                        style = MaterialTheme.typography.caption
                    )


                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.clickable(enabled = true,onClick = {
                    onMonthChoose()
                })
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = colorResource(id = R.color.cal_mon_year),
                    //border = BorderStroke(1.dp,MaterialTheme.colors.primary)
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp,top=8.dp,end=16.dp,bottom = 6.dp),
                        text = calendarContentUpdates.selectedCalendarYear.toString(),
                        color = Color.Black,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }

    }
}

@Composable
fun MonthsSurface(

                    monthChoose: Boolean,
                    onMonthChoose: ()->Unit,
                    calendarContentUpdates: CalendarContentUpdates
){
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {


        Column(modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(){
                for(i in 1..6){
                    Month(ReminderUtil.monthsStr[i]!!,onMonthChoose,calendarContentUpdates)
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(){
                for(i in 7..12){
                    Month(ReminderUtil.monthsStr[i]!!,onMonthChoose,calendarContentUpdates)
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }

}

@Composable
fun Month(
    month: String,
    onMonthChoose: ()->Unit,
    calendarContentUpdates: CalendarContentUpdates
){
    Box(modifier = Modifier.padding(start=4.dp,end=2.dp,top=2.dp,bottom = 4.dp)){
        Surface(
            shape = RoundedCornerShape(70),
            color = colorResource(id = R.color.cal_mon_year_sec),
            border = BorderStroke(1.dp, MaterialTheme.colors.primary)) {
            Column(Modifier.clickable {
                onMonthChoose()
                calendarContentUpdates.onSelectCalendarMonth(month)
            }) {
                Text(
                    modifier = Modifier.padding(start=12.dp,end=12.dp,top=4.dp,bottom = 4.dp),
                    text = month,
                    style = MaterialTheme.typography.caption,
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun CalendarWeekName(week: String){

        var wkNameSurfaceModifier: Modifier = Modifier
            .size(CAL_DATE_COL_SIZE)
            .background(
                Color.Transparent
            )

        Column(modifier = wkNameSurfaceModifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = week, color = Color.White, style = MaterialTheme.typography.h5, fontSize = 12.sp)
        }
}

@ExperimentalMaterialApi
@Composable
fun CalendarColumnItem(date: Int,
                       calendarColumn: CalendarColumn?,
                       setCalendarView: ()->Unit,
                       onSelectCalendarDate: (Int) -> Unit,){

    var daySurfaceModifier: Modifier = Modifier.size(CAL_DATE_COL_SIZE)

    var today = GregorianCalendar().get(Calendar.DAY_OF_MONTH)

    if(date == 0){
        Text(text = " ",daySurfaceModifier.padding(4.dp))
    }else{
        CalendarColumnSurface(
            modifier = daySurfaceModifier,
            calendarColumn = calendarColumn,
            setCalendarView = setCalendarView,
            onSelectCalendarDate = onSelectCalendarDate,
            date = date
        ){
            Text(
                text = date.toString(),
                color = if(date == today) colorResource(id = R.color.apr) else Color.White,
                style = if(date == today) MaterialTheme.typography.h6 else MaterialTheme.typography.caption)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CalendarColumnSurface(
                          modifier: Modifier,
                          calendarColumn: CalendarColumn?,
                          setCalendarView: ()->Unit,
                          onSelectCalendarDate: (Int) -> Unit,
                          date: Int,
                          content: @Composable () -> Unit
){
    Box(
        contentAlignment = Alignment.Center){
        Surface( modifier = modifier.padding(4.dp),
            color  = colorResource(id = R.color.white),
            shape= RoundedCornerShape(8.dp),
            elevation = 1.dp) {

        }
        Surface(modifier = modifier
            .background(color = MaterialTheme.colors.background.copy(alpha = 0.50f))
            .padding(4.dp)
            ,color = colorResource(id = R.color.cal_mon_year_sec),
            onClick = {

                        setCalendarView()
                        onSelectCalendarDate(date)
                      },
            shape = if(calendarColumn!=null && calendarColumn.due>0) RoundedCornerShape(4.dp,20.dp,4.dp,4.dp) else RoundedCornerShape(4.dp),
            elevation = 1.dp
        ) {

            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                content()
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (calendarColumn != null) {
                    if(calendarColumn.important > 0){
                        Column(
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Icon(
                                modifier = Modifier
                                    .size(6.dp),
                                painter = painterResource(R.drawable.ic_action_star_full),
                                contentDescription = null,
                                tint = colorResource(id = R.color.header1)
                            )
                        }
                    }
                }
            }

            /*if (calendarColumn != null) {

                if(calendarColumn.due > 0){
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End
                    ) {
                        Surface(
                            modifier = Modifier.size(6.dp),
                            color = colorResource(id = R.color.jul),
                            shape = RoundedCornerShape(4.dp)
                        ) {

                        }
                    }
                }

            }*/
        }

    }

}

@Composable
private fun ChoiceChipContent(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = when {
            selected -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.surface.copy(alpha = 0.30f)
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clipToBounds(),

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

data class CalendarContentUpdates(
    val onSelectCalendarDate: (Int) -> Unit,
    val selectedCalendarDate: Int,
    val selectedCalendarMonth: String,
    val onSelectCalendarMonth: (String) -> Unit,
    val selectedCalendarYear: Int,
    val setCalendarView: () -> Unit,
    val calendarView: Boolean,
    val monthCalendar: java.util.ArrayList<java.util.ArrayList<Int>>,
    val tasksForMonth: TreeMap<Int,CalendarColumn>
)

private val CAL_DATE_COL_SIZE = 56.dp
private val CALENDAR_SIZE = CAL_DATE_COL_SIZE * 6
private val TASK_SECTION_TITLE_SIZE = 56.dp
private val CAL_TASK_TITLE_SPACE = 120.dp
private val CAL_CALENDAR_OFFSET = 104.dp
private val TODAY_SCREEN_OFFSET = 116.dp
private val CAL_MIN_OFFSET = CAL_CALENDAR_OFFSET/2
private val HOME_TABS_OFFSET = 16.dp
private val MON_YEAR_SECTION_OFFSET = 72.dp
private val CAL_TASKS_TITLE_OFFSET = CALENDAR_SIZE + CAL_TASK_TITLE_SPACE + MON_YEAR_SECTION_OFFSET


private val TASKS_OFFSET = 148.dp

private val MIN_TITLE_SIZE = 28
private val MAX_TITLE_SIZE = 48

class TabContent(val homescreen: HataHomeScreens, val content: @Composable () -> Unit)

