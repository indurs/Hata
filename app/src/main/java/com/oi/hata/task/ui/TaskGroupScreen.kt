package com.oi.hata.task.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
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
import com.oi.hata.task.data.model.ImportantGroupTask
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
        Group(1,"Tasks"), emptyList()
    ))

    val importantTasksCountState = taskViewModel.getImportantTaskCount().collectAsState(initial = 0)

    /*val taskContentUpdates = TaskContentUpdates(
        onTaskTxtChange = { taskViewModel.onTaskTxtChange(it) },
        onSaveTask = { taskViewModel.saveTask(it) },
        onDueDateSelect = { year, month, day -> taskViewModel.onDueDateSelect(year = year,month = month,day = day) },
        onCloseTask = taskViewModel::resetValues,
        taskTxt = taskViewModel.taskTxt,
        dueDateSelected = taskViewModel.dueDateSelected,
        dueDate = taskViewModel.reminderDueDate,
        groupId = groupTaskState.value!!.Group!!.id,
    )*/

    val taskContentUpdates = TaskContentUpdates(taskViewModel = taskViewModel,groupId = groupTaskState.value!!.Group!!.id)

    /*val taskListItemContentUpdates = TaskListItemContentUpdates(
        onTaskItemClick = { taskViewModel.onTaskItemClick(it) },
        onTaskCompleted = { taskViewModel.onTaskCompleted(it) },
        taskcompleted = taskViewModel.taskCompleted,
        taskImportant = taskViewModel.taskImportant,
        onTaskImportant = { taskViewModel.onTaskImportant(it) },
        onDeleteTask = { taskViewModel.onDeleteTask(it)}
    )*/

    val taskListItemContentUpdates = TaskListItemContentUpdates(taskViewModel = taskViewModel,displayToday = true)




    /*val reminderContentUpdates = ReminderContentUpdates(
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
    )*/

    val reminderContentUpdates = ReminderContentUpdates(reminderViewModel = reminderViewModel,taskViewModel = taskViewModel)

    /*val customReminderContentUpdates = CustomReminderContentUpdates(
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
    )*/

    val customReminderContentUpdates = CustomReminderContentUpdates(reminderViewModel = reminderViewModel,
                                                                    taskViewModel = taskViewModel,
                                                                    onCustomReminderSelect = onCustomReminderSelect
                                                                    )

    /*val groupContentUpdates = GroupContentUpdates(
        selectedTaskGroup = taskViewModel.selectedTaskGroup,
        onSelectedTaskGroup = { taskViewModel.onSelectedTaskGroup(it) },
        importantTasksCount = importantTasksCountState.value,
        addGroupSelected = taskViewModel.addgroupSelected,
        onAddgroupSelected = { taskViewModel.onAddgroupSelected() },
        newGroup = taskViewModel.newGroup,
        onAddNewGroup = { taskViewModel.OnAddNewGroup(it)},
        saveNewGroup = { taskViewModel.saveNewGroup(it) },
    )*/

    val groupContentUpdates = GroupContentUpdates(taskViewModel = taskViewModel,
                                                    importantTasksCount = importantTasksCountState.value)




        Box(modifier = Modifier.fillMaxSize()) {

            val groupscroll = rememberScrollState(0)
            val taskscroll = rememberScrollState(0)

            Groups(
                modifier = Modifier,
                selectedGroup = groupTaskState.value,
                groupTasks = grouptasksState.value,
                groupscroll = groupscroll,
                taskselected = taskViewModel.taskselected,
                groupContentUpdates = groupContentUpdates
            )

            AnimatedVisibility(
                visible = !taskViewModel.taskselected,
                Modifier.align(Alignment.BottomCenter)
            ) {
                Tasks(
                    groupTask = groupTaskState.value,
                    groupscroll = groupscroll.value,
                    taskContentUpdates = taskContentUpdates,
                    taskListItemContentUpdates = taskListItemContentUpdates,
                    taskscroll = taskscroll,
                    onTaskSelected = { taskViewModel.onTaskSelected() }
                )
            }
            AnimatedVisibility(
                visible = taskViewModel.taskselected,
                Modifier.align(Alignment.BottomCenter)
                )
            {
                ReminderBar(
                    reminderContentUpdates = reminderContentUpdates,
                    customReminderContentUpdates = customReminderContentUpdates,
                    taskContentUpdates = taskContentUpdates,
                    onTaskSelected = { taskViewModel.onTaskSelected() },
                    taskselected = taskViewModel.taskselected
                )
            }
            TaskTopBar(modifier = Modifier.statusBarsPadding(),groupScrollState = groupscroll,groupContentUpdates = groupContentUpdates)

        }

}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun Tasks(modifier: Modifier = Modifier,
          taskContentUpdates: TaskContentUpdates,
          taskListItemContentUpdates: TaskListItemContentUpdates,
          onTaskSelected: () -> Unit,
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
        taskRowModifier = Modifier,
        groupTask = groupTask,
        onTaskSelected = onTaskSelected,
        taskListItemContentUpdates = taskListItemContentUpdates,
        height = height,
        groupscroll = groupscroll,
        taskscroll = taskscroll,
        offset = offset,
    )


}

@SuppressLint("UnusedCrossfadeTargetStateParameter")
@ExperimentalMaterialApi
@Composable
fun Groups(
    modifier: Modifier,
    groupTasks: List<GroupTask>,
    selectedGroup: GroupTask,
    groupscroll:ScrollState,
    taskselected:Boolean,
    groupContentUpdates: GroupContentUpdates,

){

    var horscroll = rememberScrollState(0)

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
                modifier = Modifier.verticalScroll(groupscroll, enabled = !taskselected)) {

                StaggeredGrid(modifier = Modifier
                    .animateContentSize()
                    .horizontalScroll(horscroll),) {

                    Crossfade(targetState = groupContentUpdates.selectedTaskGroup,
                        modifier = Modifier.animateContentSize(tween(50)),
                        animationSpec = keyframes {
                            durationMillis = 700
                            0.01f at 500
                            1f at 1000
                        }
                    ) {
                        groupTasks.forEach { task ->
                            if (groupContentUpdates.selectedTaskGroup.Group!!.name.equals(task.Group!!.name)) {
                                TaskGroup(groupTask = task, groupContentUpdates = groupContentUpdates,groupscroll = groupscroll)
                            }
                        }
                    }

                    groupTasks.forEach {
                        if(!groupContentUpdates.selectedTaskGroup.Group!!.name.equals(it.Group!!.name)){
                            TaskGroup(
                                groupTask = it,
                                groupContentUpdates = groupContentUpdates,
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

