package com.oi.hata.task.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.oi.hata.common.reminder.ui.ReminderViewModel
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.data.model.Group
import com.oi.hata.task.data.model.GroupTask
import com.oi.hata.ui.TaskViewModel
import kotlinx.coroutines.delay

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun TaskGroups(scaffoldState: ScaffoldState = rememberScaffoldState(),
               taskViewModel: TaskViewModel,
               reminderViewModel: ReminderViewModel,
               onCustomReminderSelect: () -> Unit
){



    val grouptasksState = taskViewModel.getGroupTasks().collectAsState(initial = emptyList())
    val groupTaskState = taskViewModel.getGroupTask(taskViewModel.selectedTaskGroup.Group!!.name).collectAsState(initial = GroupTask(
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
        groupId = groupTaskState.value!!.Group!!.id,
        selectedTaskGroup = taskViewModel.selectedTaskGroup,
        onSelectedTaskGroup = { taskViewModel.onSelectedTaskGroup(it) }
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


    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {},
        topBar = {
        },
        bottomBar = {
            /*BottomBar(
                reminderContentUpdates = reminderContentUpdates,
                taskContentUpdates = taskContentUpdates,
                customReminderContentUpdates = customReminderContentUpdates
            )*/


        },
        content= {
            Box(modifier = Modifier.fillMaxSize()) {

                val groupscroll = rememberScrollState(0)
                val taskscroll = rememberScrollState(0)

                Groups(
                    modifier = Modifier,
                    groupTasks = grouptasksState.value,
                    groupscroll = groupscroll,
                    taskContentUpdates = taskContentUpdates
                )

                AnimatedVisibility(
                    visible = !taskContentUpdates.taskselected,
                    Modifier.align(Alignment.BottomCenter)
                ) {
                    Tasks(
                        groupTask = groupTaskState.value,
                        groupscroll = groupscroll.value,
                        taskContentUpdates = taskContentUpdates,
                        taskscroll = taskscroll
                    )
                }
                AnimatedVisibility(
                    visible = taskContentUpdates.taskselected,
                    Modifier.align(Alignment.BottomCenter)
                    )
                {
                    BottomBar(
                        reminderContentUpdates = reminderContentUpdates,
                        taskContentUpdates = taskContentUpdates,
                        customReminderContentUpdates = customReminderContentUpdates
                    )
                }
                TaskTopBar(modifier = Modifier.statusBarsPadding(),groupScrollState = groupscroll)

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
          taskscroll: ScrollState,
){

    val maxOffset = with(LocalDensity.current) { (MinTopOffset+ MaxGroupHeight).toPx() }
    val minOffset = with(LocalDensity.current) { MinTopOffset.toPx() }

    val offset = (maxOffset - taskscroll.value + groupscroll ).coerceAtLeast(minOffset)

    val height = with(LocalDensity.current){ (200.dp  - groupscroll.toDp() + taskscroll.value.toDp())  }


    TaskList(
        modifier = modifier,
        groupTask = groupTask,
        taskContentUpdates = taskContentUpdates,
        height = height,
        groupscroll = groupscroll,
        taskscroll = taskscroll,
        offset = offset
    )


}



@ExperimentalMaterialApi
@Composable
fun Groups(
    modifier: Modifier,
    groupTasks: List<GroupTask>,
    groupscroll:ScrollState,
    taskContentUpdates: TaskContentUpdates,
){
    val maxOffset = with(LocalDensity.current) { MinTopOffset.toPx() }
    val height = with(LocalDensity.current){ (500.dp + groupscroll.value.toDp()) }
    val offset = maxOffset - groupscroll.value

    //val offset = if(taskListState.isScrollInProgress ) (maxOffset - 200) else maxOffset

    Column(modifier.fillMaxWidth(),
    ) {

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTopOffset)
        )
        Surface(modifier = Modifier
            .fillMaxWidth(),color = MaterialTheme.colors.background) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(groupscroll)) {
                
                StaggeredGrid(modifier = Modifier.animateContentSize(),taskContentUpdates = taskContentUpdates) {
                    Crossfade(targetState = taskContentUpdates.selectedTaskGroup,
                        modifier = Modifier.animateContentSize(tween(50)),
                        animationSpec = keyframes {
                            durationMillis = 700
                            0.01f at 500
                            1f at 1000
                        }
                    ) {
                        TaskGroup(groupTask = it, taskContentUpdates = taskContentUpdates,groupscroll = groupscroll)
                    }

                    groupTasks.forEach {
                        if(!taskContentUpdates.selectedTaskGroup.Group!!.name.equals(it.Group!!.name)){
                            TaskGroup(
                                groupTask = it,
                                taskContentUpdates = taskContentUpdates,
                                groupscroll = groupscroll
                            )
                        }
                    }
                }
            }
        }
    }
}

private val MinTopOffset = 48.dp
private val MaxGroupHeight = 400.dp
private val MaxTaskListHeight = 300.dp

