package com.oi.hata.task.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import com.oi.hata.R
import com.oi.hata.common.reminder.ui.SelectionState
import com.oi.hata.common.ui.HataTaskSheetIconButton
import com.oi.hata.common.ui.components.HataDatePicker
import com.oi.hata.common.ui.reminder.ReminderOptions
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.data.model.GroupTask
import com.oi.hata.task.data.model.Task
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.roundToInt

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun TaskGroup(
    taskSize: Int = 0,
    groupTask: GroupTask,
    groupContentUpdates: GroupContentUpdates,
    groupscroll: ScrollState,
) {

    val groupTransitionState =
        groupTransition(groupTask = groupTask, groupContentUpdates = groupContentUpdates)
    val scope = rememberCoroutineScope()

    Surface(
        elevation = 0.dp,
        modifier = Modifier
            .padding(16.dp)
            .focusable(groupContentUpdates.selectedTaskGroup.Group!!.name.equals(groupTask.Group!!.name)),
        color = groupTransitionState.colorAlpha,
        onClick = {
            groupContentUpdates.onSelectedTaskGroup(groupTask)
            scope.launch {
                groupscroll.animateScrollTo(
                    1, animationSpec = tween(
                        durationMillis = 2000,
                        delayMillis = 75,
                        easing = LinearOutSlowInEasing
                    )

                )
            }
        },
        border = BorderStroke(groupTransitionState.borderAlpha, Color.White),
        shape = MaterialTheme.shapes.medium.copy(topStart = CornerSize(28.dp))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .background(color = groupTransitionState.colorAlpha)
            ) {

                if (groupTask.Group.id == 2L) {
                    Text(
                        text = (groupContentUpdates.importantTasksCount + groupTask.tasks!!.size).toString(),
                        style = MaterialTheme.typography.overline,
                        color = groupTransitionState.contentAlpha
                    )
                } else {
                    AnimatedContent(targetState = groupTask.tasks!!.size) { count ->
                        Text(
                            text = count.toString(),
                            style = MaterialTheme.typography.overline,
                            color = groupTransitionState.contentAlpha
                        )
                    }
                }

                Icon(
                    painter = painterResource(R.drawable.ic_baseline_grain_24),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp, top = 4.dp)
                        .size(12.dp),
                    tint = colorResource(id = R.color.cal_col).compositeOver(Color.Black)
                )
            }

            Spacer(
                modifier = Modifier
                    .size(1.dp, 24.dp)
                    .background(color = groupTransitionState.dividerAlpha)
            )
            Text(
                text = groupTask.Group!!.name,
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.body1,
                color = groupTransitionState.contentAlpha.copy(alpha = 0.90f)
            )
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun TaskList(
    taskListItemContentUpdates: TaskListItemContentUpdates,
    todayTask: Task,
    groupTask: GroupTask?,
    taskscroll: ScrollState,
    color: Color,
    height: Dp,
    displayToday: Boolean,
    groupscroll: Int,
    alertDismiss: Boolean,
    modifier: Modifier,
    onTaskSelected: () -> Unit,
    onAlertDismiss: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TodayAlert(
            modifier = modifier,
            todayTask = todayTask,
            alertDismiss = alertDismiss,
            onTimeout = onAlertDismiss
        )
        Surface(
            modifier = Modifier
                .height(height),
            //.graphicsLayer { translationY = offset }
            color = colorResource(id = R.color.bottombar).copy(alpha = 0.98f),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AddTaskHeader(groupTask = groupTask!!, onTaskSelected = onTaskSelected)
                Divider(thickness = 1.dp, color = Color.Gray)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(taskscroll)

                ) {
                    DismissableTasks(
                        modifier = modifier,
                        color = color,
                        tasks = groupTask.tasks,
                        onTaskSelected = onTaskSelected,
                        taskListItemContentUpdates = taskListItemContentUpdates,
                        displayToday = displayToday
                    )
                }
            }
        }
    }

}

@Composable
private fun TodayAlert(
    todayTask: Task,
    alertDismiss: Boolean,
    modifier: Modifier,
    onTimeout: () -> Unit

) {

    val todayTransition =
        taskItemTodayToastTransition(alertDismiss = alertDismiss, task = todayTask)

    var modifier = Modifier.alpha(todayTransition.contentAlpha)

    LaunchedEffect(todayTask) {
        delay(1000)
        onTimeout()
    }

    Surface(
        modifier = modifier.padding(8.dp),
        color = colorResource(id = R.color.bottombar),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = if (todayTask.todaytask) stringResource(id = R.string.task_del_alert) else stringResource(
                    id = R.string.task_del_alert_2
                ),
                color = Color.Yellow,
                style = MaterialTheme.typography.overline
            )
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun TaskRow(
    taskListItemContentUpdates: TaskListItemContentUpdates,
    task: Task,
    taskselected: Boolean,
    displayToday: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onTaskSelected: () -> Unit,

) {
    var unread by remember { mutableStateOf(false) }
    var delete by remember { mutableStateOf(false) }

    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd) unread = !unread
            it != DismissValue.DismissedToEnd
            it == DismissValue.DismissedToStart
        }

    )

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == DismissValue.DismissedToStart)
            taskListItemContentUpdates.onDeleteTask(task)
    }

    LaunchedEffect(dismissState.targetValue) {

        if (dismissState.targetValue == DismissValue.DismissedToEnd)
            taskListItemContentUpdates.onTaskCompleted(task)
    }
    /*DisposableEffect(dismissState.currentValue) {
        onDispose {
            if(dismissState.currentValue == DismissValue.DismissedToStart)
                taskContentUpdates.onDeleteTask(task)
        }
    }*/

    AnimatedVisibility(visible = (dismissState.currentValue != DismissValue.DismissedToStart)) {
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier.padding(vertical = 2.dp),
            directions = setOf(DismissDirection.EndToStart),
            dismissThresholds = { direction ->
                FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.3f else 0.5f)
            },
            background = {
                val direction = dismissState.dismissDirection ?: return@SwipeToDismiss

                val color by animateColorAsState(
                    when (dismissState.targetValue) {
                        DismissValue.Default -> Color.LightGray
                        DismissValue.DismissedToEnd -> Color.Green
                        DismissValue.DismissedToStart -> Color.Red
                    }
                )
                val alignment = when (direction) {
                    DismissDirection.StartToEnd -> Alignment.CenterStart
                    DismissDirection.EndToStart -> Alignment.CenterEnd
                }
                val icon = when (direction) {
                    DismissDirection.StartToEnd -> if (!unread) Icons.Default.Done else Icons.Default.Clear
                    DismissDirection.EndToStart -> Icons.Default.Delete
                }
                val scale by animateFloatAsState(
                    if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                )

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color)
                        .padding(horizontal = 20.dp),
                    contentAlignment = alignment
                ) {
                    Icon(
                        icon,
                        contentDescription = "",
                        modifier = Modifier.scale(scale)
                    )
                }
            },
            dismissContent = {
                Card(
                    backgroundColor = color,
                    shape = RectangleShape,
                    elevation = animateDpAsState(
                        if (dismissState.dismissDirection != null) 4.dp else 0.dp
                    ).value
                ) {
                    TaskItem(
                        modifier = modifier,
                        task = task,
                        taskListItemContentUpdates = taskListItemContentUpdates,
                        onTaskSelected = onTaskSelected,
                        taskselected = taskselected,
                        displayToday = displayToday
                    )
                }
            }
        )
    }

}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
private
fun TaskItem(
    taskListItemContentUpdates: TaskListItemContentUpdates,
    task: Task,
    displayToday: Boolean,
    taskselected: Boolean = false,
    modifier: Modifier = Modifier,
    onTaskSelected: () -> Unit,

) {

    val taskItemCompleteTransitionState = taskItemCompleteTransition(task = task)
    val taskItemImportantTransitionState = taskItemImportantTransition(task = task)
    val taskItemTodayTransitionState = taskItemTodayTransition(task = task)

    var taskclickmodifier =
        if (taskselected)
            Modifier.clickable(enabled = false, onClick = { })
        else
            Modifier.clickable(enabled = true, onClick = {
                onTaskSelected()
                taskListItemContentUpdates.onTaskItemClick(task.id)
            })
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp)

        //.swipeToDismiss { taskContentUpdates.deleteTask(task) }
        /*.clickable {
            taskContentUpdates.onTaskSelected()
            taskContentUpdates.onTaskItemClick(task.id)
        }*/
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            Column(modifier = Modifier
                .align(
                    Alignment
                        .CenterVertically
                )
                .clip(CircleShape)
                .clipToBounds()
                .clickable { taskListItemContentUpdates.onTaskCompleted(task) }


            ) {
                Box(
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(R.drawable.ic_action_circle),
                        contentDescription = null,
                        tint = colorResource(id = R.color.dec)
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_action_check),
                        contentDescription = null,
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.Center),
                        tint = taskItemCompleteTransitionState.contentAlpha
                    )
                }
            }

            Column(
                modifier = taskclickmodifier
                    .align(
                        Alignment
                            .CenterVertically
                    )
                    .weight(3f)


                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 2.dp)
            ) {
                Text(
                    text = task.task,
                    style = MaterialTheme.typography.body2,
                    color = taskItemCompleteTransitionState.colorAlpha
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    AnimatedVisibility(visible = displayToday || task.todaytask) {
                        Box(modifier = Modifier
                            .clip(CircleShape)
                            .clipToBounds()
                            .clickable {
                                taskListItemContentUpdates.onTaskSetForToday(task)
                            }
                            .padding(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_action_today),
                                modifier = Modifier
                                    .size(24.dp),
                                contentDescription = null,
                                tint = taskItemTodayTransitionState.colorAlpha
                            )

                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(modifier = Modifier
                        .clip(CircleShape)
                        .clipToBounds()
                        .clickable { taskListItemContentUpdates.onTaskImportant(task) }
                        .padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_action_star),
                            modifier = Modifier
                                .size(24.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                        Icon(
                            modifier = Modifier
                                .size(24.dp),
                            painter = painterResource(R.drawable.ic_action_star_full),
                            contentDescription = null,
                            tint = taskItemImportantTransitionState.contentAlpha
                        )
                    }
                }
            }

        }

    }

}

@ExperimentalMaterialApi
@Composable
private fun AddTaskHeader(
    groupTask: GroupTask,
    onTaskSelected: () -> Unit,

) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Column(Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable { onTaskSelected() }) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(top = 10.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .border(
                            BorderStroke(1.dp, color = Color.White),
                            shape = MaterialTheme.shapes.medium
                        ),
                    color = colorResource(id = R.color.bottombar).copy(alpha = 0.98f),

                    ) {
                    Row(Modifier.padding(2.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.ic_action_add),
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterVertically)
                                .padding(start = 4.dp),
                            contentDescription = null,
                            tint = colorResource(id = R.color.header2)
                        )
                        Text(
                            modifier = Modifier.padding(
                                top = 4.dp,
                                bottom = 4.dp,
                                start = 8.dp,
                                end = 8.dp
                            ),
                            text = stringResource(id = R.string.task),
                            style = MaterialTheme.typography.overline,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }

                }

                Text(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp, end = 8.dp)
                        .align(Alignment.CenterStart),
                    text = groupTask.Group!!.name,
                    style = MaterialTheme.typography.subtitle1,
                    color = colorResource(id = R.color.header2)
                )
            }

            //Spacer(modifier = Modifier.height(AddTaskHeader))

        }
    }

}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun AddGroupButton(groupContentUpdates: GroupContentUpdates) {

    Column(modifier = Modifier.clickable { groupContentUpdates.onAddgroupSelected() }) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Surface(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 16.dp, end = 16.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .border(
                        BorderStroke(1.dp, color = Color.White),
                        shape = MaterialTheme.shapes.medium
                    ),
                color = colorResource(id = R.color.bottombar).copy(alpha = 0.98f),
            ) {
                Row() {
                    AnimatedVisibility(visible = groupContentUpdates.addGroupSelected) {
                        AddGroup(groupContentUpdates = groupContentUpdates)
                    }
                    AnimatedVisibility(visible = !groupContentUpdates.addGroupSelected) {
                        Row(modifier = Modifier.padding(6.dp)) {
                            Icon(
                                painter = painterResource(R.drawable.ic_action_add),
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 4.dp),
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                            Text(
                                modifier = Modifier.padding(
                                    top = 2.dp,
                                    bottom = 2.dp,
                                    start = 4.dp,
                                    end = 8.dp
                                ),
                                text = stringResource(id = R.string.group),
                                style = MaterialTheme.typography.overline,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }

                    }

                }
            }
        }

        Spacer(modifier = Modifier.height(AddTaskHeader))

    }
}

@Composable
private fun AddGroup(groupContentUpdates: GroupContentUpdates) {
    Row() {

        BasicTextField(
            value = groupContentUpdates.newGroup,
            modifier = Modifier
                .padding(start = 8.dp, end = 4.dp, top = 6.dp, bottom = 4.dp)
                .size(height = 22.dp, width = 160.dp),
            onValueChange = { groupContentUpdates.onAddNewGroup(it) },
            textStyle = TextStyle(color = Color.White),
            enabled = true,
            cursorBrush = SolidColor(Color.Yellow),
        )
        Icon(
            painter = painterResource(R.drawable.ic_action_add),
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterVertically)
                .padding(start = 4.dp, end = 8.dp)
                .clickable(
                    enabled = groupContentUpdates.newGroup.isNotEmpty(),
                    onClick = { groupContentUpdates.saveNewGroup(groupContentUpdates.newGroup) }),
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
        Icon(
            painter = painterResource(R.drawable.ic_baseline_close_24),
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterVertically)
                .padding(start = 4.dp, end = 8.dp)
                .clickable { groupContentUpdates.onAddgroupSelected() },
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
    }
}


@Composable
fun Header() {
    Spacer(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
    )
}

@Composable
fun StaggeredGrid(
    columns: Int = 2,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val rows = (measurables.size / 2) + 1

        val rowWidths = IntArray(rows) { 0 }
        val rowHeights = IntArray(rows) { 0 }

        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints)

            val row = index / columns
            rowWidths[row] += placeable.width
            rowHeights[row] = max(rowHeights[row], placeable.height)

            placeable
        }


        val width = rowWidths.maxOrNull()?.coerceIn(constraints.minWidth, constraints.maxWidth)
            ?: constraints.minWidth

        val height = rowHeights.sum().coerceIn(constraints.minHeight, constraints.maxHeight)

        val rowsY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowsY[i] = rowHeights[i - 1] + rowsY[i - 1]
        }

        layout(width, height) {

            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index / columns
                placeable.place(rowX[row], rowsY[row])
                rowX[row] = rowX[row] + placeable.width
            }
        }
    }
}


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DismissableTasks(
    taskListItemContentUpdates: TaskListItemContentUpdates,
    tasks: List<Task>?,
    taskselected: Boolean = false,
    displayToday: Boolean,
    color: Color,
    modifier: Modifier,
    tasklistModifier: Modifier = Modifier,
    onTaskSelected: () -> Unit,

) {

    Surface(color = MaterialTheme.colors.primary) {
        val transitionState = remember { MutableTransitionState(SelectionState.Unselected) }

    }

    Column(modifier = tasklistModifier) {

        tasks?.let {
            tasks.mapIndexed { index, item ->

                key(item.id) {
                    TaskRow(
                        modifier = modifier,
                        color = color,
                        task = item,
                        taskselected = taskselected,
                        displayToday = displayToday,
                        taskListItemContentUpdates = taskListItemContentUpdates,
                        onTaskSelected = onTaskSelected,
                    )
                }
            }

        }
    }
}


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun TaskTopBar(
    groupContentUpdates: GroupContentUpdates,
    modifier: Modifier,

) {
    //val maxOffset = with(LocalDensity.current) { if(groupScrollState.value > 0 || tasksscroll.value > 0) (MinTopBarOffset - (groupScrollState.value + tasksscroll.value).toDp()) else MinTopBarOffset }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = colorResource(id = R.color.bottombar).copy(alpha = 0.98f)

        //.graphicsLayer { translationY = maxOffset.toPx() }
    ) {
        Header()
        Row() {
            Column(modifier = Modifier.padding(12.dp)) {
                Surface(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { groupContentUpdates.onBackTaskScreen() }
                        .border(
                            BorderStroke(1.dp, color = Color.White),
                            shape = CircleShape
                        ),
                    color = colorResource(id = R.color.bottombar).copy(alpha = 0.98f),
                ) {

                    Icon(
                        modifier = Modifier.padding(6.dp),
                        imageVector = Icons.Outlined.ArrowBack,
                        tint = MaterialTheme.colors.primary,
                        contentDescription = stringResource(id = R.string.back_icon)
                    )

                }
            }

            AddGroupButton(groupContentUpdates = groupContentUpdates)

        }
    }
}


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun TaskSheet(
    customReminderContentUpdates: CustomReminderContentUpdates,
    reminderContentUpdates: ReminderContentUpdates,
    taskContentUpdates: TaskContentUpdates,
    taskselected: Boolean,
    modifier: Modifier = Modifier,
    onTaskSelected: () -> Unit,

) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = colorResource(id = R.color.bottombar).copy(alpha = 0.98f),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Column() {

            AnimatedVisibility(
                visible = reminderContentUpdates.reminderSelected && taskselected,
            ) {

                Row(
                ) {
                    ReminderOptions(
                        reminderContentUpdates = reminderContentUpdates,
                        customReminderContentUpdates = customReminderContentUpdates
                    )
                }
            }

            AnimatedVisibility(
                visible = taskselected

            ) {

                Row {
                    TaskContent(
                        taskContentUpdates = taskContentUpdates,
                        reminderContentUpdates = reminderContentUpdates,
                        onTaskSelected = onTaskSelected
                    )
                }
            }

        }
    }
}


@ExperimentalAnimationApi
@Composable
private fun TaskContent(
    taskContentUpdates: TaskContentUpdates,
    reminderContentUpdates: ReminderContentUpdates,
    modifier: Modifier = Modifier,
    onTaskSelected: () -> Unit

) {

    var (datePickerSelected, onDatePickerSelected) = remember { mutableStateOf(false) }
    val taskTransitionState = taskTransition(reminderContentUpdates.reminderSelected)


    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
        color = colorResource(id = R.color.bottombar).copy(alpha = 0.98f),
    ) {
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Row() {
                AnimatedVisibility(visible = datePickerSelected) {
                    Dialog(onDismissRequest = {
                        onDatePickerSelected(false)
                    }) {
                        HataDatePicker(onDatePickerSelected, taskContentUpdates.onDueDateSelect)
                    }
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 12.dp)
            ) {
                HataTaskSheetIconButton(
                    onClick = {
                        onTaskSelected()
                        taskContentUpdates.onCloseTask()
                        reminderContentUpdates.onReminderOptSelected(ReminderUtil.NONE)
                    },
                    painter = painterResource(id = R.drawable.ic_baseline_close_24),
                    contentDescription = stringResource(id = R.string.close)
                )
            }


            Row() {
                OutlinedTextField(
                    modifier = Modifier.size(348.dp, 56.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.White.copy(alpha = 0.90f)
                            .compositeOver(Color.Black),
                        textColor = Color.White
                    ),
                    value = taskContentUpdates.taskTxt,
                    onValueChange = { taskContentUpdates.onTaskTxtChange(it) },
                    enabled = when {
                        (reminderContentUpdates.reminderSelected) -> false
                        else -> true
                    }

                )
                IconButton(
                    modifier = Modifier.align(Alignment.Bottom),
                    enabled = when {
                        (reminderContentUpdates.reminderSelected) -> false
                        else -> true
                    },
                    onClick = {
                        onTaskSelected()
                        taskContentUpdates.onSaveTask(taskContentUpdates.groupId)
                        reminderContentUpdates.onReminderOptSelected(ReminderUtil.NONE)
                    }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_action_save),
                        tint = Color.White.copy(alpha = taskTransitionState.contentAlpha)
                            .compositeOver(Color.Black),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp, top = 20.dp),

                        )
                }
            }

            Row(Modifier.padding(start = 24.dp)) {
                Column() {
                    IconButton(enabled = when {
                        (reminderContentUpdates.reminderSelected) -> false
                        else -> true
                    },
                        onClick = {
                            if (datePickerSelected)
                                onDatePickerSelected(false)
                            else
                                onDatePickerSelected(true)

                        }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_outline_event_24),
                            contentDescription = null,
                            tint = Color.White.copy(alpha = taskTransitionState.contentAlpha)
                                .compositeOver(Color.Black),
                            modifier = Modifier
                                .padding(start = 8.dp)

                        )

                    }
                    AnimatedVisibility(visible = taskContentUpdates.dueDateSelected) {
                        Text(
                            text = taskContentUpdates.dueDate,
                            color = colorResource(id = R.color.pickdate),
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }

                Column() {
                    IconButton(onClick = {
                        if (reminderContentUpdates.reminderSelected) {
                            reminderContentUpdates.onReminderSelected(false)
                        } else {
                            reminderContentUpdates.initReminderValues()
                            reminderContentUpdates.onReminderSelected(true)
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_outline_notifications_24),
                            tint = Color.White.copy(alpha = 0.90f).compositeOver(Color.Black),
                            modifier = Modifier
                                .padding(start = 24.dp),
                            contentDescription = stringResource(id = R.string.reminder_icon)
                        )
                    }

                    AnimatedVisibility(visible = reminderContentUpdates.reminder.isNotEmpty()) {
                        Text(
                            text = reminderContentUpdates.reminder,
                            color = colorResource(id = R.color.pickdate),
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
                        )
                    }
                }

            }
        }

    }
}


@Composable
fun taskTransition(taskselected: Boolean): TaskTransition {

    val transition = updateTransition(
        targetState = if (taskselected) SelectionState.Selected else SelectionState.Unselected,
        label = ""
    )

    val selectedAlpha = transition.animateFloat(label = "") { state ->
        when (state) {
            SelectionState.Unselected -> 0.9f
            SelectionState.Selected -> 0.98f
        }
    }
    val contentAlpha = transition.animateFloat(label = "") { state ->
        when (state) {
            SelectionState.Unselected -> 0.9f
            SelectionState.Selected -> 0.30f
        }
    }

    return remember(transition) {
        TaskTransition(selectedAlpha, contentAlpha)
    }
}

class TaskTransition(
    selectedAlpha: State<Float>,
    contentAlpha: State<Float>
) {
    val selectedAlpha by selectedAlpha
    val contentAlpha by contentAlpha
}


class GroupTransition(
    borderAlpha: State<Dp>,
    contentAlpha: State<Color>,
    colorAlpha: State<Color>,
    dividerAlpha: State<Color>
) {
    val borderAlpha by borderAlpha
    val contentAlpha by contentAlpha
    val colorAlpha by colorAlpha
    val dividerAlpha by dividerAlpha
}

@Composable
fun groupTransition(
    groupTask: GroupTask,
    groupContentUpdates: GroupContentUpdates
): GroupTransition {

    val transition = updateTransition(
        targetState = if (groupTask.Group!!.name.equals(groupContentUpdates.selectedTaskGroup.Group!!.name))
            SelectionState.Selected
        else SelectionState.Unselected,
        label = ""
    )

    val borderAlpha = transition.animateDp(
        transitionSpec = {
            when {
                SelectionState.Unselected isTransitioningTo SelectionState.Selected ->
                    tween(durationMillis = 5000)
                else ->
                    spring(stiffness = 50f)
            }
        }, label = ""

    ) { state ->
        when (state) {
            SelectionState.Unselected -> 0.dp
            SelectionState.Selected -> 1.dp
        }
    }
    val contentAlpha = transition.animateColor(label="") { state ->
        when (state) {
            SelectionState.Unselected -> Color.Black
            SelectionState.Selected -> Color.White
        }
    }

    val colorAlpha = transition.animateColor(transitionSpec = {
        when {
            SelectionState.Unselected isTransitioningTo SelectionState.Selected ->
                tween(durationMillis = 5000)
            else ->
                spring(stiffness = 50f)
        }
    }, label = "") { state ->
        when (state) {
            SelectionState.Unselected -> Color.White
            SelectionState.Selected -> MaterialTheme.colors.background
        }
    }

    val dividerAlpha = transition.animateColor(label="") { state ->
        when (state) {
            SelectionState.Unselected -> Color.Black.copy(alpha = 0.10f)
            SelectionState.Selected -> Color.White.copy(alpha = 0.20f)
        }
    }

    return remember(transition) {
        GroupTransition(borderAlpha, contentAlpha, colorAlpha, dividerAlpha)
    }
}

class TaskItemTransition(
    contentAlpha: State<Color>,
    colorAlpha: State<Color>
) {
    val contentAlpha by contentAlpha
    val colorAlpha by colorAlpha
}

@Composable
fun taskItemCompleteTransition(
    task: Task,
): TaskItemTransition {

    val transition = updateTransition(
        targetState = if (task.completed)
            SelectionState.Selected
        else SelectionState.Unselected,
        label = ""
    )

    val contentAlpha = transition.animateColor(label = "") { state ->
        when (state) {
            SelectionState.Unselected -> colorResource(id = R.color.dec).copy(alpha = 0.0f)
            SelectionState.Selected -> colorResource(id = R.color.dec)
        }
    }

    val colorAlpha = transition.animateColor(label = "") { state ->
        when (state) {
            SelectionState.Unselected -> Color.White
            SelectionState.Selected -> Color.White.copy(alpha = 0.30f)
        }
    }

    return remember(transition) {
        TaskItemTransition(contentAlpha = contentAlpha, colorAlpha = colorAlpha)
    }

}

@Composable
fun taskItemImportantTransition(
    task: Task,
): TaskItemTransition {
    val transition = updateTransition(
        targetState = if (task.importantGroupId == 2L)
            SelectionState.Selected
        else SelectionState.Unselected,
        label = ""
    )

    val contentAlpha = transition.animateColor(label = "") { state ->
        when (state) {
            SelectionState.Unselected -> MaterialTheme.colors.primary.copy(alpha = 0.0f)
            SelectionState.Selected -> MaterialTheme.colors.primary
        }
    }

    val colorAlpha = transition.animateColor(label = "") { state ->
        when (state) {
            SelectionState.Unselected -> Color.White
            SelectionState.Selected -> Color.White.copy(alpha = 0.10f)
        }
    }

    return remember(transition) {
        TaskItemTransition(contentAlpha = contentAlpha, colorAlpha = colorAlpha)
    }

}

class TaskItemTodayTransition(
    contentAlpha: State<Float>,
    colorAlpha: State<Color>
) {
    val contentAlpha by contentAlpha
    val colorAlpha by colorAlpha
}

@Composable
fun taskItemTodayTransition(
    task: Task,
): TaskItemTodayTransition {

    val transition = updateTransition(
        targetState = if (task.todaytask)
            SelectionState.Selected
        else SelectionState.Unselected,
        label = ""
    )

    val contentAlpha = transition.animateFloat(
        transitionSpec = { tween(durationMillis = 100) }, label = "alertAlpha"
    ) { state ->
        when (state) {
            SelectionState.Unselected -> 0.0f
            SelectionState.Selected -> 0.90f
        }
    }

    val colorAlpha = transition.animateColor(label="") { state ->
        when (state) {
            SelectionState.Unselected -> Color.White
            SelectionState.Selected -> colorResource(id = R.color.apr)
        }
    }

    return remember(transition) {
        TaskItemTodayTransition(contentAlpha = contentAlpha, colorAlpha = colorAlpha)
    }

}

@Composable
fun taskItemTodayToastTransition(
    alertDismiss: Boolean,
    task: Task
): TaskItemTodayTransition {

    val transition = updateTransition(
        targetState = if (!alertDismiss && (task.id > 0))
            SelectionState.Selected
        else SelectionState.Unselected,
        label = ""
    )

    val contentAlpha = transition.animateFloat(label = "") { state ->
        when (state) {
            SelectionState.Unselected -> 0.0f
            SelectionState.Selected -> 0.90f
        }
    }

    val colorAlpha = transition.animateColor(label="") { state ->
        when (state) {
            SelectionState.Unselected -> Color.White
            SelectionState.Selected -> Color.White.copy(alpha = 0.30f)
        }
    }

    return remember(transition) {
        TaskItemTodayTransition(contentAlpha = contentAlpha, colorAlpha = colorAlpha)
    }

}


data class CustomReminderContentUpdates(
    val onCustomReminderSelect: () -> Unit,
    val onCustomReminderInit: () -> Unit,
    val onCompleteReminder: () -> Unit,
    val onCloseReminder: () -> Unit,
    val customreminder: String,
)

data class ReminderContentUpdates(
    val onTimeSelect: (hour: Int, minute: Int, am: Boolean) -> Unit,
    val onTimeSelected: (Boolean) -> Unit,
    val onReminderSelected: (Boolean) -> Unit,
    val onReminderOptSelected: (String) -> Unit,
    val onPickaDate: (year: Int, month: Int, day: Int) -> Unit,
    val onPickaDateSelected: (Boolean) -> Unit,
    val resetReminder: () -> Unit,
    val initReminderValues: () -> Unit,
    val onClearReminderValues: () -> Unit,
    val reminderSelected: Boolean,
    val reminderOptSelected: String,
    val pickaDateSelected: Boolean,
    val pickRemDate: String,
    val timeSelected: Boolean,
    val reminder: String,
    val reminderTime: String,
)

data class
TaskContentUpdates(
    val onTaskTxtChange: (String) -> Unit,
    val taskTxt: String,
    val onSaveTask: (Long) -> Unit,
    val onDueDateSelect: (year: Int, month: Int, day: Int) -> Unit,
    val dueDateSelected: Boolean,
    val onCloseTask: () -> Unit,
    val dueDate: String,
    val groupId: Long,
)

data class TaskListItemContentUpdates(
    val onTaskCompleted: (Task) -> Unit,
    val taskcompleted: List<Long>,
    val onTaskImportant: (Task) -> Unit,
    val taskImportant: List<Long>,
    val todaysTasks: List<Long>,
    val onTaskItemClick: (Long) -> Unit,
    val onDeleteTask: (Task) -> Unit,
    val onTaskSetForToday: (Task) -> Unit
)

data class GroupContentUpdates(
    val selectedTaskGroup: GroupTask,
    val onSelectedTaskGroup: (GroupTask) -> Unit,
    val onAddgroupSelected: () -> Unit,
    val addGroupSelected: Boolean,
    val onAddNewGroup: (String) -> Unit,
    val saveNewGroup: (String) -> Unit,
    val onBackTaskScreen: () -> Unit,
    val newGroup: String,
    val importantTasksCount: Int
)

enum class TASK_MODE { INSERT, UPDATE, DELETE }

private val AddTaskHeader = 16.dp