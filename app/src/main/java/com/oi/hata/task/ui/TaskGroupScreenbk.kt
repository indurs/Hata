/*package com.oi.hata.task.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.oi.hata.common.reminder.ui.ReminderViewModel
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.data.model.Group
import com.oi.hata.task.data.model.GroupTask
import com.oi.hata.ui.TaskViewModel

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun TaskGroups(scaffoldState: ScaffoldState = rememberScaffoldState(),
               taskViewModel: TaskViewModel,
               reminderViewModel: ReminderViewModel,
               onCustomReminderSelect: () -> Unit
){



    val grouptasksState = taskViewModel.getGroupTasks().collectAsState(initial = emptyList())
    val groupTaskState = taskViewModel.getGroupTask("Tasks").collectAsState(initial = GroupTask(
        Group(0,""), emptyList()
    ))

    val taskContentUpdates = TaskContentUpdates(
        onTaskSelected = { taskViewModel.onTaskSelected() },
        onTaskTxtChange = { taskViewModel.onTaskTxtChange(it) },
        onSaveTask = { taskViewModel.saveTask(it) },
        onAddTaskSelected = { taskViewModel.onTaskSelected() },
        onDueDateSelect = { year, month, day -> taskViewModel.onDueDateSelect(year = year,month = month,day = day) },
        onCloseTask = taskViewModel::resetValues,
        onTaskItemClick = { taskViewModel.onTaskItemClick(it) },
        taskselected = taskViewModel.taskselected,
        taskTxt = taskViewModel.taskTxt,
        dueDateSelected = taskViewModel.dueDateSelected,
        dueDate = taskViewModel.reminderDueDate,
        groupId = groupTaskState.value!!.Group!!.id
    )

    val reminderContentUpdates = ReminderContentUpdates(
        onTimeSelect = { hour, minute, am -> reminderViewModel.onTimeSelect(hour,minute,am)},
        onTimeSelected = reminderViewModel::onReminderTimeSelected,
        onReminderSelected = reminderViewModel::onReminderSelected,
        onReminderOptSelected = reminderViewModel::onReminderOptionSelected ,
        onPickaDate = { year, month, day -> reminderViewModel.onPickaDateSelect(year = year,month = month, day = day) },
        onPickaDateSelected = reminderViewModel::onPickaDateSelected,
        resetReminder = taskViewModel::resetReminder,
        initReminderValues = { taskViewModel.setReminderFromTaskUIState()
            reminderViewModel.initReminderValues(taskViewModel.getTaskReminder())
        },
        onClearReminderValues =  {
            taskViewModel.resetTaskReminder()
            reminderViewModel.initReminderValues(taskViewModel.getTaskReminder())
        },
        reminderSelected = reminderViewModel.reminderSelected,
        reminderOptSelected = reminderViewModel.reminderOptSelected,
        pickaDateSelected = reminderViewModel.pickDateSelected,
        pickRemDate = reminderViewModel.pickAdate,
        timeSelected = reminderViewModel.reminderTimeSelected,
        reminder = taskViewModel.getReminderTxt(),
        reminderTime = reminderViewModel.reminderTime,
    )

    val customReminderContentUpdates = CustomReminderContentUpdates(
        onCustomReminderSelect = onCustomReminderSelect,
        onCustomReminderInitialize = {
                                        if(reminderViewModel.reminderOptSelected != ReminderUtil.CUSTOM){
                                            taskViewModel.resetReminder()
                                        }
                                            reminderViewModel.onReminderCustomClick(taskViewModel.getReminder())
                                        },
        onCompleteReminder = { taskViewModel.saveTaskReminder(reminderViewModel.getReminderValues()) },
        onCloseReminder = taskViewModel::resetReminder,
        customreminder = taskViewModel.getCustomReminderTxt(reminderViewModel.reminderOptSelected),
    )

    val groupscroll = rememberScrollState(0)
    val taskListState = remember { LazyListState() }

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {},
        topBar = {
        },
        bottomBar = {
            BottomBar(
                reminderContentUpdates = reminderContentUpdates,
                taskContentUpdates = taskContentUpdates,
                customReminderContentUpdates = customReminderContentUpdates
            )


        },
        content= {
            Box(modifier = Modifier.fillMaxSize()) {

                    TaskTopBar(modifier = Modifier.statusBarsPadding(),groupScrollState = groupscroll)
                    Column() {
                        Groups(
                            modifier = Modifier.padding(4.dp),
                            groupTasks = grouptasksState.value,
                            taskListState = taskListState,
                            groupscroll = groupscroll
                        )
                        AnimatedVisibility(
                            visible = !taskContentUpdates.taskselected,
                        ) {
                            Tasks(
                                groupTask = groupTaskState.value,
                                groupscroll = groupscroll.value,
                                taskListState = taskListState,
                                taskContentUpdates = taskContentUpdates
                            )
                        }
                    }

            }
        }
    )

}

@ExperimentalMaterialApi
@Composable
fun Tasks(modifier: Modifier = Modifier,
          taskContentUpdates: TaskContentUpdates,
          groupTask: GroupTask?,
          groupscroll: Int,
          taskListState: LazyListState
){

    val maxOffset = with(LocalDensity.current) { (MinTopOffset+ MaxGroupHeight).toPx() }
    val minOffset = with(LocalDensity.current) { MinTopOffset.toPx() }

    //val offset =  if(groupscroll > 0) (maxOffset + groupscroll) else (maxOffset - tasksscroll.value)

    //val height = with(LocalDensity.current){ (300.dp + tasksscroll.value.toDp() - groupscroll.toDp()) }

    val height = with(LocalDensity.current){
        400.dp
    }
    println("HEIGHT >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+height)
    val initialScrollDP = 40.dp
    val initialScroll = with(LocalDensity.current) { initialScrollDP.toPx() }

    LaunchedEffect(key1 = groupTask){
        //taskListState.animateScrollToItem(5,scrollOffset = 300)
    }

    TaskList(
        groupTask = groupTask,
        taskContentUpdates = taskContentUpdates,
        taskListState = taskListState,
        height = height,
        groupscroll = groupscroll)


}



@Composable
fun Groups(
            modifier: Modifier,
            groupTasks: List<GroupTask>,
            taskListState: LazyListState,
            groupscroll:ScrollState
){
    val maxOffset = with(LocalDensity.current) { MinTopOffset.toPx() }
    val height = with(LocalDensity.current){ (500.dp + groupscroll.value.toDp()) }
    //val offset = if(taskscroll > 0) (maxOffset - taskscroll) else maxOffset
    //val offset = if(taskListState.isScrollInProgress ) (maxOffset - 200) else maxOffset

    Column(modifier = modifier
        .graphicsLayer { translationY = maxOffset }
        .height(height)
        ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTopOffset)
        )

        StaggeredGrid(modifier = Modifier.verticalScroll(groupscroll)) {
            groupTasks.forEach {
                TaskGroup(groupTask = it)
            }
        }
    }

}

private val MinTopOffset = 24.dp
private val MaxGroupHeight = 400.dp
private val MaxTaskListHeight = 300.dp
*/