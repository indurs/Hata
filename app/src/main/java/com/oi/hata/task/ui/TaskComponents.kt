package com.oi.hata.task.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
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
import com.oi.hata.task.data.model.ImportantGroupTask
import com.oi.hata.task.data.model.Task
import com.oi.hata.ui.theme.shapes
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

import kotlin.math.max
import kotlin.math.roundToInt
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
fun TaskGroup(  taskSize: Int = 0,
                groupTask: GroupTask,
                taskContentUpdates: TaskContentUpdates,
                groupscroll:ScrollState,
            ){

    val groupTransitionState = groupTransition(groupTask = groupTask,taskContentUpdates = taskContentUpdates)
    val scope = rememberCoroutineScope()

    Surface(elevation = 0.dp,
        modifier = Modifier
            .padding(16.dp)
            .focusable(taskContentUpdates.selectedTaskGroup.Group!!.name.equals(groupTask.Group!!.name)),
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
                    if(groupTask.Group.id == 2L){
                        Text(
                            text = (groupTask.tasks!!.size + taskContentUpdates.importantGroupTask.tasks!!.size).toString(),
                            style = MaterialTheme.typography.overline,
                            color = groupTransitionState.contentAlpha
                        )
                    }else{
                        Text(
                            text = groupTask.tasks!!.size.toString(),
                            style = MaterialTheme.typography.overline,
                            color = groupTransitionState.contentAlpha
                        )
                    }

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

@ExperimentalAnimationApi
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
                        DismissableTasks(groupTask = groupTask, taskContentUpdates = taskContentUpdates)
                        /*groupTask?.let{
                            it.tasks!!.mapIndexed { index, task ->
                                if (index > 0) {
                                    //Divider(thickness = 0.20.dp,color = Color.Yellow.copy(alpha = 0.50f))
                                    Spacer(modifier = Modifier.height(8.dp).pointerInput(Unit){
                                        coroutineScope {
                                            launch {
                                                detectDragGestures(
                                                    onDrag = {change, dragAmount ->  }
                                                )
                                            }
                                        }
                                    }
                                        
                                    )
                                }
                                    TaskItem(task = task,taskContentUpdates = taskContentUpdates)
                            }
                        }*/
                    }
                }
        }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun TaskRow(
    task: Task,
    taskContentUpdates: TaskContentUpdates
){
    var unread by remember { mutableStateOf(false) }
    var delete by remember { mutableStateOf(false) }


    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd) unread = !unread
            it != DismissValue.DismissedToEnd
            it == DismissValue.DismissedToStart
        }

    )

    LaunchedEffect(dismissState.currentValue){
        println("LaunchedEffect >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ dismissState.currentValue + " TAASK "+task.task)
        println("LaunchedEffect >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>TARGET"+ dismissState.targetValue + " TAASK "+task.task +"DIRECTION "+dismissState.dismissDirection)
        if(dismissState.currentValue == DismissValue.DismissedToStart)
            taskContentUpdates.onDeleteTask(task)
    }

    LaunchedEffect(dismissState.targetValue){
        println("LaunchedEffect >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ dismissState.targetValue + " TAASK "+task.task)

        if(dismissState.targetValue == DismissValue.DismissedToEnd)
            taskContentUpdates.onTaskCompleted(task)
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
            modifier = Modifier.padding(vertical = 4.dp),
            directions = setOf( DismissDirection.EndToStart,),
            dismissThresholds = { direction ->
                FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.3f else 0.5f)
            },
            background = {
                val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                println("background >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>TARGET"+ dismissState.targetValue + " TAASK "+task.task +"DIRECTION "+direction
                )
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
                    DismissDirection.StartToEnd ->  if(!unread) Icons.Default.Done else Icons.Default.Clear
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
                        contentDescription = "Localized description",
                        modifier = Modifier.scale(scale)
                    )
                }
            },
            dismissContent = {
                Card(
                    shape = RectangleShape,
                    elevation = animateDpAsState(
                        if (dismissState.dismissDirection != null) 4.dp else 0.dp
                    ).value
                ) {
                    TaskItem(delete = delete,dismissState = dismissState, task = task, taskContentUpdates = taskContentUpdates)
                }
            }
        )
    }

}

@ExperimentalMaterialApi
@Composable
fun TaskItem(
            delete: Boolean,
            dismissState: DismissState,
            task: Task,
            taskContentUpdates: TaskContentUpdates
            ){

    val taskItemCompleteTransitionState = taskItemCompleteTransition(task = task,taskContentUpdates = taskContentUpdates)
    val taskItemImportantTransitionState = taskItemImportantTransition(task = task, taskContentUpdates = taskContentUpdates)

    Column(
        modifier = Modifier
            .fillMaxWidth().padding(top = 4.dp,bottom = 4.dp)

            //.swipeToDismiss { taskContentUpdates.deleteTask(task) }
            /*.clickable {
                taskContentUpdates.onTaskSelected()
                taskContentUpdates.onTaskItemClick(task.id)
            }*/
    ) {

        Row( modifier = Modifier
            .fillMaxWidth()

        ){
                Column(modifier = Modifier
                    .align(
                        Alignment
                            .CenterVertically
                    )
                    .clickable { taskContentUpdates.onTaskCompleted(task) }
                    .padding(8.dp)


                ) {
                    Box(
                    ){
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


            Text(
                text = task.task,
                modifier = Modifier
                    .padding(start = 4.dp, top = 16.dp, bottom = 16.dp)
                    .weight(3f)
                    .clickable {    taskContentUpdates.onTaskSelected()
                                    taskContentUpdates.onTaskItemClick(task.id)
                               }
                    /*.pointerInput(Unit) {
                        coroutineScope {
                            launch {
                                detectTapGestures(
                                    onTap = {
                                        taskContentUpdates.onTaskSelected()
                                        taskContentUpdates.onTaskItemClick(task.id)
                                    }
                                )
                            }
                        }
                    }*/
                    ,
                style = MaterialTheme.typography.body2,
                color = taskItemCompleteTransitionState.colorAlpha)
            Column(modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
               ) {
                Row(modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 8.dp)) {
                    Box(modifier = Modifier
                        .padding(top = 2.dp, end = 8.dp)
                        .clickable { taskContentUpdates.onTaskImportant(task) }){
                        Icon(
                            painter = painterResource(R.drawable.ic_action_today),
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(24.dp),
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.50f)
                        )
                    }
                    Box(modifier = Modifier.clickable { taskContentUpdates.onTaskImportant(task) }){
                        Icon(
                            painter = painterResource(R.drawable.ic_action_star),
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(24.dp),
                            contentDescription = null,
                            tint = colorResource(id = R.color.sample)
                        )
                        Icon(
                            modifier = Modifier
                                .padding(end = 16.dp)
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
private fun AddTaskHeader(taskContentUpdates: TaskContentUpdates){
    Row() {

        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(top = 16.dp, end = 24.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .border(
                            BorderStroke(1.dp, color = Color.White),
                            shape = MaterialTheme.shapes.medium
                        ),
                    color = colorResource(id = R.color.bottombar).copy(alpha = 0.98f),

                    onClick = { taskContentUpdates.onTaskSelected() }
                ) {
                    Row(){
                        Icon(
                            painter = painterResource(R.drawable.ic_action_add),
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterVertically)
                                .padding(start = 4.dp),
                            contentDescription = null,
                            tint = colorResource(id = R.color.sample)
                        )
                        Text(
                            modifier = Modifier.padding(top=4.dp,bottom = 4.dp,start = 8.dp,end = 8.dp),
                            text = "Task",
                            style = MaterialTheme.typography.overline,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }

                }

                    Text(
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
                            .align(Alignment.CenterStart),
                        text = "Tasks",
                        style = MaterialTheme.typography.overline,
                        fontSize = 12.sp,
                        color = Color.White
                    )


            }

            Spacer(modifier = Modifier.height(AddTaskHeader))

            Divider(thickness = 1.dp,color = Color.Gray)

        }
    }

}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun AddGroupButton(taskContentUpdates: TaskContentUpdates){

    Column(modifier = Modifier.clickable { taskContentUpdates.onAddgroupSelected() }) {
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
                    AnimatedVisibility(visible = taskContentUpdates.addGroupSelected) {
                        AddGroup(taskContentUpdates = taskContentUpdates)
                    }
                    AnimatedVisibility(visible = !taskContentUpdates.addGroupSelected) {
                        Row() {
                            Icon(
                                painter = painterResource(R.drawable.ic_action_add),
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 4.dp),
                                contentDescription = null,
                                tint = colorResource(id = R.color.sample)
                            )
                            Text(
                                modifier = Modifier.padding(top=4.dp,bottom = 4.dp,start = 4.dp,end = 8.dp),
                                text = "Group",
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
private fun AddGroup(taskContentUpdates: TaskContentUpdates){
    Row(){

        BasicTextField( value = taskContentUpdates.newGroup,
            modifier = Modifier
                .padding(8.dp)
                .size(height = 20.dp, width = 160.dp),
            onValueChange = { taskContentUpdates.onAddNewGroup(it)},
            textStyle = TextStyle(color = Color.White, ),
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
                    enabled = taskContentUpdates.newGroup.isNotEmpty(),
                    onClick = { taskContentUpdates.saveNewGroup(taskContentUpdates.newGroup) })
            ,
            contentDescription = null,
            tint = colorResource(id = R.color.sample)
        )
        Icon(
            painter = painterResource(R.drawable.ic_baseline_close_24),
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterVertically)
                .padding(start = 4.dp, end = 8.dp)
                .clickable { taskContentUpdates.onAddgroupSelected() }
            ,
            contentDescription = null,
            tint = colorResource(id = R.color.sample)
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

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DismissableTasks(groupTask: GroupTask?,taskContentUpdates: TaskContentUpdates,){

    Column {
        groupTask?.let {
            it.tasks!!.mapIndexed { index, item ->
                key(item.id){
                    TaskRow(task = item, taskContentUpdates = taskContentUpdates)
                }
            }

        }
    }
}



private fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed {
    // This `Animatable` stores the horizontal offset for the element.
    val offsetX = remember { Animatable(0f) }
    val offsetx = remember { mutableStateOf(0f) }

    pointerInput(Unit) {
        // Used to calculate a settling position of a fling animation.
        val decay = splineBasedDecay<Float>(this)
        // Wrap in a coroutine scope to use suspend functions for touch events and animation.
        coroutineScope {
            forEachGesture {
                // Wait for a touch down event
                // Interrupt any ongoing animation.
                offsetX.stop()
                // Prepare for drag events and record velocity of a fling.
                val velocityTracker = VelocityTracker()


                // Wait for drag events.
                awaitPointerEventScope {
                    val pointerId = awaitFirstDown().id
                    var change =
                        awaitHorizontalTouchSlopOrCancellation(pointerId) { change, over ->
                            println("awaitHorizontalTouchSlopOrCancellation>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

                            val originalX = offsetX.value
                            val newValue =
                                (originalX + change.positionChange().x)
                            change.consumePositionChange()
                            //offsetX.value = newValue

                            launch { offsetX.snapTo(newValue) }

                            change.consumeAllChanges()

                        }

                    while (change != null && change.pressed) {
                        change = awaitHorizontalDragOrCancellation(change.id)
                        if (change != null && change.pressed) {
                            println("awaitHorizontalDragOrCancellation>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

                            val originalX = offsetX.value
                            val newValue = (originalX + change.positionChange().x)



                            launch { offsetX.snapTo(newValue) }

                            // Record the velocity of the drag.
                            velocityTracker.addPosition(change.uptimeMillis, change.position)
                            change.consumeAllChanges()
                        }
                    }

                }
                // Dragging finished. Calculate the velocity of the fling.
                val velocity = velocityTracker.calculateVelocity().x

                // Calculate where the element eventually settles after the fling animation.
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)
                // The animation should end as soon as it reaches these bounds.
                offsetX.updateBounds(
                    lowerBound = 0f,
                    upperBound = size.width.toFloat()

                )
                launch {
                    if (targetOffsetX.absoluteValue <= (size.width)) {
                        // Not enough velocity; Slide back to the default position.
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        // Enough velocity to slide away the element to the edge.
                        offsetX.animateDecay(velocity, decay)
                        // The element was swiped away.
                        onDismissed()
                    }

                }
            }
        }
    }
        // Apply the horizontal offset to the element.
        .offset {
            /*if(offsetX.value.roundToInt() < -80 || offsetX.value.roundToInt() > 30) {
                IntOffset(offsetX.value.roundToInt(), 0)
            }else{
                IntOffset(0, 0)
            }*/
           IntOffset(offsetX.value.roundToInt(), 0)
        }

}


/*private fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed {
    // This `Animatable` stores the horizontal offset for the element.
    val offsetX = remember { Animatable(0f) }
    pointerInput(Unit) {
        // Used to calculate a settling position of a fling animation.
        val decay = splineBasedDecay<Float>(this)
        // Wrap in a coroutine scope to use suspend functions for touch events and animation.
        coroutineScope {
            while (true) {
                // Wait for a touch down event.
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                // Interrupt any ongoing animation.
                offsetX.stop()
                // Prepare for drag events and record velocity of a fling.
                val velocityTracker = VelocityTracker()


                // Wait for drag events.
                awaitPointerEventScope {

                    var change =
                        awaitHorizontalTouchSlopOrCancellation(pointerId) { change, over ->
                            println("awaitHorizontalTouchSlopOrCancellation>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

                            val originalX = offsetX.value
                            val newValue =
                                (originalX + change.positionChange().x)
                            change.consumePositionChange()
                            //offsetX.value = newValue

                            launch { offsetX.snapTo(newValue) }

                            change.consumeAllChanges()

                        }
                    horizontalDrag(pointerId) { change ->
                        // Record the position after offset
                        val horizontalDragOffset = offsetX.value + change.positionChange().x
                        launch {
                            // Overwrite the `Animatable` value while the element is dragged.
                            offsetX.snapTo(horizontalDragOffset)
                        }
                        // Record the velocity of the drag.
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        // Consume the gesture event, not passed to external
                        //change.consumeAllChanges()
                    }
                }
                // Dragging finished. Calculate the velocity of the fling.
                val velocity = velocityTracker.calculateVelocity().x
                // Calculate where the element eventually settles after the fling animation.
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)
                // The animation should end as soon as it reaches these bounds.
                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(),
                    upperBound = size.width.toFloat()
                )
                launch {
                    if (targetOffsetX.absoluteValue <= size.width) {
                        // Not enough velocity; Slide back to the default position.
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        // Enough velocity to slide away the element to the edge.
                        offsetX.animateDecay(velocity, decay)
                        // The element was swiped away.
                        onDismissed()
                    }
                }
            }
        }
    }
        // Apply the horizontal offset to the element.
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
}
*/

private fun Modifier.onItemclick(
    onDismissed: () -> Unit
): Modifier = composed {
    // This `Animatable` stores the horizontal offset for the element.
    val offsetX = remember { Animatable(0f) }
    pointerInput(Unit) {
        // Used to calculate a settling position of a fling animation.
        val decay = splineBasedDecay<Float>(this)
        // Wrap in a coroutine scope to use suspend functions for touch events and animation.
        coroutineScope {
            forEachGesture {
                // Wait for a touch down event
                // Interrupt any ongoing animation.
                offsetX.stop()
                // Prepare for drag events and record velocity of a fling.
                val velocityTracker = VelocityTracker()


                // Wait for drag events.
                awaitPointerEventScope {
                    val pointerId = awaitFirstDown().id
                    var change =
                        awaitTouchSlopOrCancellation(pointerId) { change, over ->
                            println("awaitTouchSlopOrCancellation>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

                        }

                }
            }
        }
    }
        // Apply the horizontal offset to the element.
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun TaskTopBar(modifier: Modifier,
               taskContentUpdates: TaskContentUpdates,
               groupScrollState: ScrollState){
    //val maxOffset = with(LocalDensity.current) { if(groupScrollState.value > 0 || tasksscroll.value > 0) (MinTopBarOffset - (groupScrollState.value + tasksscroll.value).toDp()) else MinTopBarOffset }

    Surface(modifier = modifier
        .fillMaxWidth()
        //.graphicsLayer { translationY = maxOffset.toPx() }
        ) {
        Header()
        Row() {
            Column() {
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

            AddGroupButton(taskContentUpdates = taskContentUpdates)

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
    taskContentUpdates: TaskContentUpdates
): TaskItemTransition {

    val transition = updateTransition(
        targetState = if (task.completed)
            SelectionState.Selected
        else SelectionState.Unselected,
        label = ""
    )

    val contentAlpha = transition.animateColor { state ->
        when (state) {
            SelectionState.Unselected -> colorResource(id = R.color.dec).copy(alpha = 0.0f)
            SelectionState.Selected -> colorResource(id = R.color.dec)
        }
    }

    val colorAlpha = transition.animateColor { state ->
        when (state) {
            SelectionState.Unselected -> Color.White
            SelectionState.Selected -> Color.White.copy(alpha = 0.30f)
        }
    }

    return remember(transition) {
        TaskItemTransition(contentAlpha = contentAlpha,colorAlpha = colorAlpha)
    }

}

@Composable
fun taskItemImportantTransition(
    task: Task,
    taskContentUpdates: TaskContentUpdates
): TaskItemTransition {
    println("TASK GROUP >>>>>>>>>>>>>>>>>>>"+ task.importantGroupId)
    val transition = updateTransition(
        targetState = if (task.importantGroupId == 2L )
            SelectionState.Selected
        else SelectionState.Unselected,
        label = ""
    )

    val contentAlpha = transition.animateColor { state ->
        when (state) {
            SelectionState.Unselected -> colorResource(id = R.color.sample).copy(alpha = 0.0f)
            SelectionState.Selected -> colorResource(id = R.color.sample)
        }
    }

    val colorAlpha = transition.animateColor { state ->
        when (state) {
            SelectionState.Unselected -> Color.White
            SelectionState.Selected -> Color.White.copy(alpha = 0.10f)
        }
    }

    return remember(transition) {
        TaskItemTransition(contentAlpha = contentAlpha,colorAlpha = colorAlpha)
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

data class
TaskContentUpdates(
    val onTaskSelected: () -> Unit,
    val taskselected: Boolean,
    val onTaskCompleted: (Task) -> Unit,
    val taskcompleted: List<Long>,
    val onTaskImportant: (Task) -> Unit,
    val taskImportant: List<Long>,
    val onTaskTxtChange: (String) -> Unit,
    val taskTxt: String,
    val onSaveTask: (Long) -> Unit,
    val onAddTaskSelected: () -> Unit,
    val onDueDateSelect: (year: Int, month: Int, day:Int) -> Unit,
    val dueDateSelected: Boolean,
    val onCloseTask: () -> Unit,
    val onTaskItemClick: (Long) -> Unit,
    val selectedTaskGroup: GroupTask,
    val importantGroupTask: ImportantGroupTask,
    val onSelectedTaskGroup: (GroupTask) -> Unit,
    val onAddgroupSelected: () -> Unit,
    val addGroupSelected: Boolean,
    val onAddNewGroup: (String) -> Unit,
    val saveNewGroup: (String) -> Unit,
    val onDeleteTask: (Task) -> Unit,
    val newGroup: String,
    val dueDate: String,
    val groupId: Long,
)

enum class TASK_MODE { INSERT, UPDATE, DELETE }

private val AddTaskHeader = 16.dp