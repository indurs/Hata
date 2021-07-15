package com.oi.hata.task.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.insets.statusBarsPadding
import com.oi.hata.R
import com.oi.hata.common.reminder.ui.SelectionState
import com.oi.hata.common.ui.HataDivider
import com.oi.hata.common.ui.HataTaskSheetIconButton
import com.oi.hata.common.ui.components.HataDatePicker
import com.oi.hata.common.ui.reminder.ReminderOptions
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.data.model.GroupTask
import com.oi.hata.task.data.model.Task
import kotlinx.coroutines.launch

import kotlin.math.max
import kotlin.reflect.KFunction0

@ExperimentalMaterialApi
@Composable
fun TaskItem(
                task: Task,
                taskselected: Boolean,
                onTaskItemClick: () -> Unit
){
    val taskTransitionState = taskTransition(taskselected)

    var modifier =
        if(taskselected)
            Modifier.clickable(enabled = false, onClick = {})
        else
            Modifier.clickable(enabled = true, onClick = { onTaskItemClick() })

    Column(
        modifier = modifier.
        background(color = MaterialTheme.colors.surface.copy(alpha = taskTransitionState.selectedAlpha).compositeOver(Color.White)),
    ) {
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier
            .fillMaxWidth()

        ){

            Text(
                text = task.task,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.body2,
                color = Color.White.copy(alpha = taskTransitionState.contentAlpha))

        }

    }
}

@ExperimentalMaterialApi
@Composable
fun TaskGroup(
                groupTask: GroupTask,
                taskContentUpdates: TaskContentUpdates,
                groupscroll:ScrollState,
            ){

    val groupTransitionState = groupTransition(groupTask = groupTask,taskContentUpdates = taskContentUpdates)
    val scope = rememberCoroutineScope()

    Surface(elevation = 0.dp,
        modifier = Modifier.padding(16.dp).focusable(taskContentUpdates.selectedTaskGroup.Group!!.name.equals(groupTask.Group!!.name)),
        color = groupTransitionState.colorAlpha,
        onClick = {
                    taskContentUpdates.onSelectedTaskGroup(groupTask)
                    scope.launch {
                        groupscroll.animateScrollTo(1,animationSpec = tween(
                            durationMillis = 2000,
                            delayMillis = 75,
                            easing = LinearOutSlowInEasing
                        )

                        )
                    }
                  },
        border = BorderStroke(groupTransitionState.borderAlpha, Color.White),
        shape = MaterialTheme.shapes.medium.copy(topStart = CornerSize(28.dp) )
    ){
        Row(modifier = Modifier.padding(16.dp),verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier
                .align(Alignment.CenterVertically)
                .background(color = groupTransitionState.colorAlpha)){

                    Text(
                        text = groupTask.tasks!!.size.toString(),
                        style = MaterialTheme.typography.overline,
                        color = groupTransitionState.contentAlpha
                         )

                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_grain_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 8.dp, top = 4.dp)
                            .size(12.dp),
                        tint = MaterialTheme.colors.onSurface
                    )
                }

            Spacer(modifier = Modifier
                .size(1.dp, 24.dp)
                .background(color = groupTransitionState.dividerAlpha))
            Text(
                text = groupTask.Group!!.name,
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.body1,
                color = groupTransitionState.contentAlpha.copy(alpha = 0.90f)
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun TaskList(
            modifier: Modifier,
            groupTask: GroupTask?,
            height: Dp,
            taskContentUpdates: TaskContentUpdates,
            groupscroll: Int,
            taskscroll: ScrollState,
            offset: Float
    ){

        Surface(
            modifier = Modifier
                .height(height),
                //.graphicsLayer { translationY = offset }
            color = colorResource(id = R.color.bottombar).copy(alpha = 0.98f),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            ) {
                Column {
                    AddTaskHeader(taskContentUpdates)
                    
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(taskscroll)

                    ) {
                        groupTask?.let{
                            it.tasks!!.mapIndexed { index, task ->
                                if (index > 0) {

                                    Divider(thickness = 0.20.dp,color = Color.Yellow.copy(alpha = 0.50f))
                                }
                                //Text(text = task.task,modifier = Modifier.padding(8.dp))
                                Column(

                                ) {
                                    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier
                                        .fillMaxWidth()

                                    ){

                                        Text(
                                            text = task.task,
                                            modifier = Modifier.padding(16.dp),
                                            style = MaterialTheme.typography.body2,
                                            color = Color.White)
                                    }

                                }
                            }
                        }
                    }
                }
        }
}

@ExperimentalMaterialApi
@Composable
private fun AddTaskHeader(taskContentUpdates: TaskContentUpdates){
    Column {
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

                onClick = { taskContentUpdates.onTaskSelected() }
            ) {
                Text(
                    modifier = Modifier.padding(top=4.dp,bottom = 4.dp,start = 8.dp,end = 8.dp),
                    text = "Add Task",
                    style = MaterialTheme.typography.overline,
                    fontSize = 12.sp,
                    color = Color.White
                )

            }
        }
        
        Spacer(modifier = Modifier.height(AddTaskHeader))

        Divider(thickness = 1.dp,color = Color.Gray)
        
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
    modifier: Modifier = Modifier,
    columns: Int = 2,
    taskContentUpdates: TaskContentUpdates,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val rows = (measurables.size/2) + 1

        val rowWidths = IntArray(rows) { 0 } // Keep track of the width of each row
        val rowHeights = IntArray(rows) { 0 } // Keep track of the height of each row

        // Don't constrain child views further, measure them with given constraints
        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints)

            // Track the width and max height of each row
            val row = index / columns
            rowWidths[row] += placeable.width
            rowHeights[row] = max(rowHeights[row], placeable.height)

            placeable
        }

        // Grid's width is the widest row
        val width = rowWidths.maxOrNull()?.coerceIn(constraints.minWidth, constraints.maxWidth)
            ?: constraints.minWidth
        // Grid's height is the sum of each row
        val height = rowHeights.sum().coerceIn(constraints.minHeight, constraints.maxHeight)

        val rowsY = IntArray(rows) { 0 }
        for( i in 1 until  rows){
            rowsY[i] = rowHeights[i-1] + rowsY[i-1]
        }

        layout(width,height){
            // x co-ord we have placed up to, per row
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index/columns
                placeable.place(rowX[row], rowsY[row])
                rowX[row] = rowX[row] + placeable.width
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun TaskButton(onTaskAddClick: () -> Unit){
    Row() {
        Surface(modifier = Modifier.padding(8.dp),
            shape = RoundedCornerShape(4,4,4,8),
        ) {
            Text(text = "Add Group")
        }
        HataTaskSheetIconButton(onClick = {},
                                painter = painterResource(id = R.drawable.ic_baseline_done_24),
                                contentDescription = "Add"
                )
    }
}

@Composable
fun TaskTopBar(modifier: Modifier,groupScrollState: ScrollState){
    //val maxOffset = with(LocalDensity.current) { if(groupScrollState.value > 0 || tasksscroll.value > 0) (MinTopBarOffset - (groupScrollState.value + tasksscroll.value).toDp()) else MinTopBarOffset }

    Surface(modifier = modifier
        .fillMaxWidth()
        //.graphicsLayer { translationY = maxOffset.toPx() }
        ) {
        Header()
        Row() {
            IconButton(
                onClick = {  },
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .size(36.dp)
                    .background(color = Color.White, shape = CircleShape)

            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    }
}

private val MinTopBarOffset = 16.dp

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun BottomBar(
                        customReminderContentUpdates: CustomReminderContentUpdates,
                        reminderContentUpdates: ReminderContentUpdates,
                        taskContentUpdates: TaskContentUpdates
){
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = colorResource(id = R.color.bottombar).copy(alpha = 0.98f),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Column() {

            AnimatedVisibility(visible = reminderContentUpdates.reminderSelected && taskContentUpdates.taskselected,) {

                Row(
                ) {
                    ReminderOptions(
                        reminderContentUpdates = reminderContentUpdates,
                        customReminderContentUpdates = customReminderContentUpdates
                    )
                }
            }

            AnimatedVisibility(
                visible = taskContentUpdates.taskselected,

                ) {

                Row {
                    TaskContent(
                        taskContentUpdates = taskContentUpdates,
                        reminderContentUpdates = reminderContentUpdates,
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


){

    var (datePickerSelected,onDatePickerSelected) = remember { mutableStateOf(false) }
    val taskTransitionState = taskTransition(reminderContentUpdates.reminderSelected)

    Surface(modifier = Modifier
        .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 4.dp,topEnd = 4.dp),
        color = colorResource(id = R.color.bottombar).copy(alpha = 0.98f),
    ) {
        Column(modifier = Modifier.padding(start=8.dp)) {
            Row() {
                AnimatedVisibility(visible = datePickerSelected) {
                    Dialog(onDismissRequest = {
                        onDatePickerSelected(false)
                    }) {
                        HataDatePicker(onDatePickerSelected,taskContentUpdates.onDueDateSelect)
                    }
                }
            }
            Row(modifier = Modifier
                .align(Alignment.End)
                .padding(top = 12.dp)) {
                HataTaskSheetIconButton(
                    onClick = {
                        taskContentUpdates.onTaskSelected()
                        taskContentUpdates.onCloseTask()
                        reminderContentUpdates.onReminderOptSelected(ReminderUtil.NONE)
                    },
                    painter = painterResource(id = R.drawable.ic_baseline_close_24),
                    contentDescription = stringResource(id = R.string.close))
            }


            Row(){
                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.White.copy(alpha = 0.90f).compositeOver(Color.Black)
                    ),
                    value = taskContentUpdates.taskTxt,
                    onValueChange = { taskContentUpdates.onTaskTxtChange(it) },
                    enabled =  when { (reminderContentUpdates.reminderSelected) -> false
                        else -> true
                    }

                )
                IconButton(
                    modifier = Modifier.align(Alignment.Bottom),
                    enabled =  when { (reminderContentUpdates.reminderSelected) -> false
                        else -> true
                    },
                    onClick = {
                        taskContentUpdates.onTaskSelected()
                        taskContentUpdates.onSaveTask(taskContentUpdates.groupId)
                        reminderContentUpdates.onReminderOptSelected(ReminderUtil.NONE)
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
                    IconButton( enabled =  when { (reminderContentUpdates.reminderSelected) -> false
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
                    AnimatedVisibility(visible = taskContentUpdates.dueDateSelected) {
                        //DateChip(text = dueDate)
                        Text(
                            text = taskContentUpdates.dueDate,
                            color = colorResource(id = R.color.pickdate),
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(start = 8.dp,bottom = 8.dp)
                        )
                    }
                }

                IconButton( enabled =  when { (reminderContentUpdates.reminderSelected) -> false
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
                Column() {
                    IconButton(onClick = {
                        if(reminderContentUpdates.reminderSelected) {
                            reminderContentUpdates.onReminderSelected(false)
                        }
                        else {
                            reminderContentUpdates.initReminderValues()
                            reminderContentUpdates.onReminderSelected(true)
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Notifications,
                            tint = Color.White.copy(alpha = 0.90f).compositeOver(Color.Black),
                            modifier = Modifier
                                .padding(start = 16.dp),
                            contentDescription = "Reminder")
                    }

                    AnimatedVisibility(visible = reminderContentUpdates.reminder.isNotEmpty()) {
                        Text(
                            text = reminderContentUpdates.reminder,
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



@Composable
fun taskTransition(taskselected:Boolean): TaskTransition{

    val transition = updateTransition(
        targetState = if (taskselected) SelectionState.Selected else SelectionState.Unselected,
        label = ""
    )

    val selectedAlpha = transition.animateFloat { state ->
        when (state) {
            SelectionState.Unselected -> 0.9f
            SelectionState.Selected -> 0.98f
        }
    }
    val contentAlpha = transition.animateFloat { state ->
        when (state) {
            SelectionState.Unselected -> 0.9f
            SelectionState.Selected -> 0.30f
        }
    }

    return remember(transition) {
        TaskTransition(selectedAlpha,contentAlpha)
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
){
    val borderAlpha by borderAlpha
    val contentAlpha by contentAlpha
    val colorAlpha by colorAlpha
    val dividerAlpha by dividerAlpha
}

@Composable
fun groupTransition(
                    groupTask: GroupTask,
                    taskContentUpdates: TaskContentUpdates
                    ): GroupTransition{

    val transition = updateTransition(
        targetState = if (groupTask.Group!!.name.equals(taskContentUpdates.selectedTaskGroup.Group!!.name))
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
        }

    ) { state ->
        when (state) {
            SelectionState.Unselected -> 0.dp
            SelectionState.Selected -> 1.dp
        }
    }
    val contentAlpha = transition.animateColor { state ->
        when (state) {
            SelectionState.Unselected -> Color.Black
            SelectionState.Selected -> Color.White
        }
    }

    val colorAlpha = transition.animateColor( transitionSpec = {
        when {
            SelectionState.Unselected isTransitioningTo SelectionState.Selected ->
                tween(durationMillis = 5000)
            else ->
                spring(stiffness = 50f)
        }
    }) { state ->
        when (state) {
            SelectionState.Unselected -> Color.White
            SelectionState.Selected -> MaterialTheme.colors.background
        }
    }

    val dividerAlpha = transition.animateColor { state ->
        when (state) {
            SelectionState.Unselected -> Color.Black.copy(alpha = 0.10f)
            SelectionState.Selected -> Color.White.copy(alpha = 0.20f)
        }
    }

    return remember(transition) {
        GroupTransition(borderAlpha,contentAlpha,colorAlpha,dividerAlpha)
    }
}

data class CustomReminderContentUpdates(
    val onCustomReminderSelect: () -> Unit,
    val onCustomReminderInitialize: () -> Unit,
    val onCompleteReminder: () -> Unit,
    val onCloseReminder: () -> Unit,
    val customreminder: String,
)

data class ReminderContentUpdates(
    val onTimeSelect: (hour:Int, minute:Int, am:Boolean) -> Unit,
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

data class TaskContentUpdates(
    val onTaskSelected: () -> Unit,
    val onTaskTxtChange: (String) -> Unit,
    val onSaveTask: (Long) -> Unit,
    val onAddTaskSelected: () -> Unit,
    val onDueDateSelect: (year: Int, month: Int, day:Int) -> Unit,
    val onCloseTask: () -> Unit,
    val onTaskItemClick: (Long) -> Unit,
    val taskselected: Boolean,
    val selectedTaskGroup: GroupTask,
    val onSelectedTaskGroup: (GroupTask) -> Unit,
    val taskTxt: String,
    val dueDateSelected: Boolean,
    val dueDate: String,
    val groupId: Long
)

enum class TASK_MODE { INSERT, UPDATE, DELETE }

private val AddTaskHeader = 16.dp