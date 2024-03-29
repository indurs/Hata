package com.oi.hata.task.ui

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.oi.hata.R
import com.oi.hata.common.reminder.ui.ReminderViewModel
import com.oi.hata.task.data.model.Group
import com.oi.hata.task.data.model.GroupTask
import com.oi.hata.task.data.model.Task

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun TaskGroups(
    taskViewModel: TaskViewModel,
    reminderViewModel: ReminderViewModel,
    onCustomReminderSelect: () -> Unit,
    onBackTaskScreen: () -> Unit
) {
    val grouptasksState = taskViewModel.getGroupTasks().collectAsState(initial = emptyList())

    val groupTaskState =
        taskViewModel.getGroupTask(taskViewModel.selectedTaskGroup.Group!!.name).collectAsState(
            initial = GroupTask(
                Group(1, stringResource(id = R.string.tasks)), emptyList()
            )
        )


    val importantTasksCountState = taskViewModel.getImportantTaskCount().collectAsState(initial = 0)

    val taskContentUpdates = TaskContentUpdates(
        taskViewModel = taskViewModel,
        groupId = groupTaskState.value!!.Group!!.id
    )

    val taskListItemContentUpdates =
        TaskListItemContentUpdates(taskViewModel = taskViewModel)

    val reminderContentUpdates =
        ReminderContentUpdates(reminderViewModel = reminderViewModel, taskViewModel = taskViewModel)

    val customReminderContentUpdates = CustomReminderContentUpdates(
        reminderViewModel = reminderViewModel,
        taskViewModel = taskViewModel,
        onCustomReminderSelect = onCustomReminderSelect
    )

    val groupContentUpdates = GroupContentUpdates(
        taskViewModel = taskViewModel,
        importantTasksCount = importantTasksCountState.value,
        onBackTaskScreen = onBackTaskScreen
    )

    Box(modifier = Modifier.fillMaxSize()) {

        val groupscroll = rememberScrollState(0)
        val taskscroll = rememberScrollState(0)

        Groups(
            modifier = Modifier,
            groupTasks = grouptasksState.value,
            groupscroll = groupscroll,
            taskselected = taskViewModel.taskselected,
            groupContentUpdates = groupContentUpdates
        )

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = !taskViewModel.taskselected,
            /*enter = expandVertically(
                expandFrom = Alignment.Top,
                initialHeight = { 100 }
            ),
            exit = shrinkVertically(
                targetHeight = { fullHeight -> fullHeight / 2 },
                animationSpec = tween(100, easing = FastOutSlowInEasing)
            )*/

        ) {
            Tasks(
                groupTask = groupTaskState.value,
                groupscroll = groupscroll.value,
                todayTask = taskViewModel.todayTask,
                taskListItemContentUpdates = taskListItemContentUpdates,
                taskscroll = taskscroll,
                displayToday = true,
                onTaskSelected = { taskViewModel.onTaskSelected() },
                alertDismiss = taskViewModel.alertDismiss,
                onAlertDismiss = { taskViewModel.onAlertDismiss() }
            )
        }
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = taskViewModel.taskselected,
            /*enter = expandVertically(
                expandFrom = Alignment.Top,
                initialHeight = { 100 }
            ),
            exit = shrinkVertically(
                targetHeight = { fullHeight -> fullHeight / 2 },
                animationSpec = tween(100, easing = FastOutSlowInEasing)
            )*/
        )
        {
            TaskSheet(
                reminderContentUpdates = reminderContentUpdates,
                customReminderContentUpdates = customReminderContentUpdates,
                taskContentUpdates = taskContentUpdates,
                onTaskSelected = { taskViewModel.onTaskSelected() },
                taskselected = taskViewModel.taskselected
            )
        }
        TaskTopBar(
            modifier = Modifier.statusBarsPadding(),
            groupContentUpdates = groupContentUpdates
        )

    }

}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun Tasks(
    todayTask: Task,
    taskListItemContentUpdates: TaskListItemContentUpdates,
    groupTask: GroupTask?,
    taskscroll: ScrollState,
    groupscroll: Int,
    alertDismiss: Boolean,
    displayToday: Boolean,
    onTaskSelected: () -> Unit,
    onAlertDismiss: () -> Unit
) {

    val maxOffset = with(LocalDensity.current) { (MinTopOffset + MaxGroupHeight).toPx() }
    val minOffset = with(LocalDensity.current) { MinTopOffset.toPx() }

    val offset = (maxOffset - taskscroll.value + groupscroll).coerceAtLeast(minOffset)

    val height =
        with(LocalDensity.current) { (200.dp - groupscroll.toDp() + taskscroll.value.toDp()) }

    var color = MaterialTheme.colors.background.copy(alpha = 0.50f)

    TaskList(
        modifier = Modifier,
        color = color,
        todayTask = todayTask,
        groupTask = groupTask,
        onTaskSelected = onTaskSelected,
        taskListItemContentUpdates = taskListItemContentUpdates,
        height = height,
        groupscroll = groupscroll,
        taskscroll = taskscroll,
        displayToday = displayToday,
        alertDismiss = alertDismiss,
        onAlertDismiss = onAlertDismiss
    )


}

@ExperimentalAnimationApi
@SuppressLint("UnusedCrossfadeTargetStateParameter")
@ExperimentalMaterialApi
@Composable
private fun Groups(
    groupContentUpdates: GroupContentUpdates,
    groupTasks: List<GroupTask>,
    groupscroll: ScrollState,
    taskselected: Boolean,
    modifier: Modifier,
    ) {


    var horscroll = rememberScrollState(0)

    Column(
        modifier.fillMaxWidth(),
    ) {

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTopOffset)
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth(), color = MaterialTheme.colors.background
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(groupscroll, enabled = !taskselected)
            ) {

                StaggeredGrid(
                    modifier = Modifier

                        .animateContentSize()
                        .horizontalScroll(horscroll),
                ) {

                    Crossfade(targetState = groupContentUpdates.selectedTaskGroup,
                        modifier = Modifier.animateContentSize(tween(50)),
                        animationSpec = keyframes {
                            durationMillis = 300
                            0.01f at 200
                            1f at 400
                        }
                    ) {
                        groupTasks.forEach { task ->
                            if (groupContentUpdates.selectedTaskGroup.Group!!.name.equals(task.Group!!.name)) {
                                TaskGroup(
                                    groupTask = task,
                                    groupContentUpdates = groupContentUpdates,
                                    groupscroll = groupscroll
                                )
                            }
                        }
                    }

                    groupTasks.forEach {
                        if (!groupContentUpdates.selectedTaskGroup.Group!!.name.equals(it.Group!!.name)) {
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

private val MinTopOffset = 56.dp
private val MaxGroupHeight = 400.dp
private val MaxTaskListHeight = 300.dp

