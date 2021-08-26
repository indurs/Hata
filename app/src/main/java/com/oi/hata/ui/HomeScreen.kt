package com.oi.hata.ui


import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
               monthCalendar: java.util.ArrayList<java.util.ArrayList<Int>>
){

    val todayRemindersState = homeViewModel.getReminders(ReminderUtil.TODAY).collectAsState(initial = ArrayList())

    val calendarDateTasksState = homeViewModel.getTasksForCalendarDate(8,homeViewModel.selectedCalendarDate).collectAsState(
        initial = ArrayList()
    )

    val tomorrowRemindersState = homeViewModel.getReminders(ReminderUtil.TOMORROW).collectAsState(initial = ArrayList())
    val tasksForMonthState = homeViewModel.getTasksForMonth(homeViewModel.currentMonth).collectAsState(
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
        monthCalendar = monthCalendar,
        tasksForMonth = tasksForMonthState.value!!,
        selectedCalendarDate = homeViewModel.selectedCalendarDate
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


       Box() {

           Column() {
               Surface(modifier = Modifier.fillMaxSize()) {

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
                       calendarDateTasksState = calendarDateTasksState
                   )

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

){

    val selectedTabIndex = currentTab.ordinal

    var tasksScrollState = rememberScrollState()

    val tabsOffset = with(LocalDensity.current) { HOME_TABS_OFFSET.toPx() }

    var offset = (tabsOffset - tasksScrollState.value)

    val coroutineScope = rememberCoroutineScope()
    Column(
        Modifier
            .fillMaxWidth()
            ) {

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
        Text(
            text = "Tasks",
            style = MaterialTheme.typography.subtitle1,
            color = colorResource(id = R.color.taskdivider)
        )

        DismissableTasks(
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
    Box {

        HataCalendarSection(
            tasksScrollState = tasksScrollState,
            calendarContentUpdates = calendarContentUpdates,
            calhorscrollState = calhorscrollState,
        )

       /* TaskSectionTitle(
            date = calendarContentUpdates.selectedCalendarDate,
            tasksScrollState = tasksScrollState)*/

        CalendarTasksSection(
            tasksScrollState = tasksScrollState,
            onTaskSelected=onTaskSelected,
            taskListItemContentUpdates = taskListItemContentUpdates,
            taskselected = taskselected,
            calendarDateTasks = calendarDateTasksState,
            date = calendarContentUpdates.selectedCalendarDate
        )

    }
}

@Composable
fun TaskSectionTitle(
                    date: Int,
                    tasksScrollState: ScrollState
                    ){

    val titleOffset = with(LocalDensity.current) { (CALENDAR_SIZE+CAL_TASK_TITLE_SPACE).toPx() }

    val offset = (titleOffset - tasksScrollState.value)

    Column(modifier = Modifier.graphicsLayer { translationY = offset }) {
        //Spacer(modifier = Modifier.height(CAL_TASK_TITLE_SPACE + CALENDAR_SIZE))
        Text(
            text = date.toString(),
            style = MaterialTheme.typography.overline,
            color = colorResource(id = R.color.taskdivider )
        )
    }
}


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun CalendarTasksSection(
                                date:Int,
                                tasksScrollState: ScrollState,
                                onTaskSelected: () -> Unit,
                                taskListItemContentUpdates: TaskListItemContentUpdates,
                                taskselected: Boolean,
                                calendarDateTasks: List<Task>?,
                        ){

    val calendarOffset = with(LocalDensity.current) { CAL_CALENDAR_OFFSET.toPx() }
    var offset = (calendarOffset - tasksScrollState.value)

    var titileSize = (MIN_TITLE_SIZE + tasksScrollState.value).coerceAtMost(MAX_TITLE_SIZE)

        Column(modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(tasksScrollState)) {
            Spacer(modifier = Modifier.height(CAL_TASK_TITLE_SPACE + CALENDAR_SIZE ))
            Text(
                text = "25",
                fontSize = titileSize.sp,
                style = MaterialTheme.typography.overline,
                color = colorResource(id = R.color.taskdivider )
            )
            DismissableTasks(
                tasks = calendarDateTasks,
                onTaskSelected = onTaskSelected,
                taskselected = taskselected,
                taskListItemContentUpdates = taskListItemContentUpdates
            )
            Spacer(modifier = Modifier.height(48.dp))
        }


}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun HataCalendarSection(
    tasksScrollState: ScrollState,
    calendarContentUpdates: CalendarContentUpdates,
    calhorscrollState:ScrollState
){
    val calendarOffset = with(LocalDensity.current) { CAL_CALENDAR_OFFSET.toPx() }
    val calMinOffset = with(LocalDensity.current) { (CAL_CALENDAR_OFFSET - 120.dp).toPx() }

    var offset = (calendarOffset - tasksScrollState.value)

    AnimatedVisibility(
        visible = offset > calMinOffset
    ) {
        HataCalendarSectionContent(
            tasksScrollState = tasksScrollState,
            calendarContentUpdates = calendarContentUpdates,
            calhorscrollState = calhorscrollState,
            offset = offset
        )
    }

}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun HataCalendarSectionContent(
    tasksScrollState: ScrollState,
    calendarContentUpdates: CalendarContentUpdates,
    calhorscrollState:ScrollState,
    offset: Float
){

    Column(
        Modifier
            .graphicsLayer { translationY = offset }
            .fillMaxWidth()
            .horizontalScroll(calhorscrollState)) {

                Row( Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
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
                                        CalendarColumnItem(num = 0, null)
                                    }
                                }
                                if(i < weedDayarray.size && calendarContentUpdates.monthCalendar[j][i] != 0 ) {
                                    CalendarColumnItem(
                                        num = calendarContentUpdates.monthCalendar[j][i] ,
                                        calendarContentUpdates = calendarContentUpdates)
                                }
                            }
                        }
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
            Text(text = week)
        }


}

@ExperimentalMaterialApi
@Composable
fun CalendarColumnItem(num: Int, calendarContentUpdates: CalendarContentUpdates?){

    var daySurfaceModifier: Modifier = Modifier.size(CAL_DATE_COL_SIZE)

    if(num == 0){
        Text(text = " ",daySurfaceModifier.padding(4.dp))
    }else{
        CalendarColumnSurface(
            modifier = daySurfaceModifier.background(Color.White),
            calendarContentUpdates = calendarContentUpdates,
            date = num
        ){
            Text(text = num.toString(), color = Color.Black)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CalendarColumnSurface(
                          modifier: Modifier,
                          calendarContentUpdates: CalendarContentUpdates?,
                          date: Int,
                          content: @Composable () -> Unit
){
    Box(contentAlignment = Alignment.Center){
        Surface( modifier = modifier.padding(4.dp),
            color  = colorResource(id = R.color.sep),
            shape= RoundedCornerShape(8.dp),
            elevation = 1.dp) {

        }
        Surface(modifier = modifier.padding(4.dp),
            onClick = { calendarContentUpdates!!.setCalendarView()
                        calendarContentUpdates!!.onSelectCalendarDate(date)
                      },
            color = Color.White,
            shape = RoundedCornerShape(4.dp),
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

data class CalendarContentUpdates(
    val onSelectCalendarDate: (Int) -> Unit,
    val selectedCalendarDate: Int,
    val setCalendarView: () -> Unit,
    val calendarView: Boolean,
    val monthCalendar: java.util.ArrayList<java.util.ArrayList<Int>>,
    val tasksForMonth: TreeMap<Int,CalendarColumn>
)

private val CAL_DATE_COL_SIZE = 56.dp
private val CALENDAR_SIZE = CAL_DATE_COL_SIZE * 6
private val TASK_SECTION_TITLE_SIZE = 56.dp
private val CAL_TASK_TITLE_SPACE = 48.dp
private val CAL_CALENDAR_OFFSET = 48.dp
private val CAL_MIN_OFFSET = CAL_CALENDAR_OFFSET/2
private val HOME_TABS_OFFSET = 16.dp
private val CAL_TASKS_TITLE_OFFSET = CALENDAR_SIZE + CAL_TASK_TITLE_SPACE

private val MIN_TITLE_SIZE = 16
private val MAX_TITLE_SIZE = 48

class TabContent(val homescreen: HataHomeScreens, val content: @Composable () -> Unit)

