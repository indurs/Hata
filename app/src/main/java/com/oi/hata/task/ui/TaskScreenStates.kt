package com.oi.hata.task.ui

import androidx.compose.runtime.Stable
import com.oi.hata.common.reminder.ui.ReminderViewModel
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.data.model.GroupTask
import com.oi.hata.task.data.model.Task
import com.oi.hata.ui.TaskViewModel


fun TaskContentUpdates(taskViewModel: TaskViewModel, groupId: Long = 0): TaskContentUpdates {
    val taskContentUpdates = TaskContentUpdates(
        onTaskTxtChange = { taskViewModel.onTaskTxtChange(it) },
        onSaveTask = { taskViewModel.saveTask(it) },
        onDueDateSelect = { year, month, day -> taskViewModel.onDueDateSelect(year = year,month = month,day = day) },
        onCloseTask = taskViewModel::resetValues,
        taskTxt = taskViewModel.taskTxt,
        dueDateSelected = taskViewModel.dueDateSelected,
        dueDate = taskViewModel.reminderDueDate,
        groupId = groupId,
    )
    return taskContentUpdates
}

fun TaskListItemContentUpdates(taskViewModel: TaskViewModel,displayToday: Boolean) : TaskListItemContentUpdates{
    val taskListItemContentUpdates = TaskListItemContentUpdates(
        onTaskItemClick = { taskViewModel.onTaskItemClick(it) },
        onTaskCompleted = { taskViewModel.onTaskCompleted(it) },
        taskcompleted = taskViewModel.taskCompleted,
        taskImportant = taskViewModel.taskImportant,
        onTaskImportant = { taskViewModel.onTaskImportant(it) },
        onDeleteTask = { taskViewModel.onDeleteTask(it)},
        onTaskSetForToday = { taskViewModel.onTaskSetForToday(it)},
        todaysTasks = taskViewModel.todaysTasks,
        displayToday = displayToday
    )

    return taskListItemContentUpdates
}

fun ReminderContentUpdates(reminderViewModel: ReminderViewModel,taskViewModel: TaskViewModel): ReminderContentUpdates{
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
    return reminderContentUpdates
}

fun CustomReminderContentUpdates(reminderViewModel: ReminderViewModel,
                                 taskViewModel: TaskViewModel,
                                 onCustomReminderSelect: () -> Unit
) : CustomReminderContentUpdates{
    val customReminderContentUpdates = CustomReminderContentUpdates(
        onCustomReminderSelect = onCustomReminderSelect,
        onCustomReminderInit = {
            println("onCustomReminderInitialize >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            if(reminderViewModel.reminderOptSelected != ReminderUtil.CUSTOM){
                taskViewModel.resetReminder()
            }
            reminderViewModel.onReminderCustomClick(taskViewModel.getReminder())
        },
        onCompleteReminder = { taskViewModel.saveTaskReminder(reminderViewModel.getReminderValues()) },
        onCloseReminder = taskViewModel::resetReminder,
        customreminder = taskViewModel.getCustomReminderTxt(reminderViewModel.reminderOptSelected),
    )
    return customReminderContentUpdates
}

fun GroupContentUpdates(
    taskViewModel: TaskViewModel,
    importantTasksCount: Int,
    onBackTaskScreen: () -> Unit
): GroupContentUpdates{
    val groupContentUpdates = GroupContentUpdates(
        selectedTaskGroup = taskViewModel.selectedTaskGroup,
        onSelectedTaskGroup = { taskViewModel.onSelectedTaskGroup(it) },
        importantTasksCount = importantTasksCount,
        addGroupSelected = taskViewModel.addgroupSelected,
        onAddgroupSelected = { taskViewModel.onAddgroupSelected() },
        newGroup = taskViewModel.newGroup,
        onAddNewGroup = { taskViewModel.OnAddNewGroup(it)},
        saveNewGroup = { taskViewModel.saveNewGroup(it) },
        onBackTaskScreen = onBackTaskScreen
    )

    return groupContentUpdates
}

@Stable
interface TaskListItemState {

    var taskCompleted: List<Long>
    var taskImportant: List<Long>
    fun onTaskCompleted(task: Task)
    fun onTaskImportant(task: Task)
    fun onTaskItemClick(id: Long)

}

@Stable
interface TaskContentState {
    var taskTxt: String
    var taskselected: Boolean
    var dueDateSelected: Boolean
    fun onDueDateSelect (year: Int, month: Int, day:Int)
    fun onSaveTask (task: Task)
    fun onCloseTask ()
}

@Stable
interface GroupState{
   var selectedTaskGroup: GroupTask
   var addgroupSelected: Boolean
   var newGroup: String
   val saveNewGroup: (String) -> Unit
}

//fun TaskState(): TaskState =



/*
val taskContentUpdates = TaskContentUpdates(
        onTaskSelected = { taskViewModel.onTaskSelected() },
        onTaskTxtChange = { taskViewModel.onTaskTxtChange(it) },
        onSaveTask = { taskViewModel.saveTask(it) },
        onDueDateSelect = { year, month, day -> taskViewModel.onDueDateSelect(year = year,month = month,day = day) },
        onCloseTask = taskViewModel::resetValues,
        onTaskItemClick = { taskViewModel.onTaskItemClick(it) },
        taskselected = taskViewModel.taskselected,
        taskTxt = taskViewModel.taskTxt,
        dueDateSelected = taskViewModel.dueDateSelected,
        dueDate = taskViewModel.reminderDueDate,
        groupId = groupTaskState.value!!.Group!!.id,
        selectedTaskGroup = taskViewModel.selectedTaskGroup,
        onSelectedTaskGroup = { taskViewModel.onSelectedTaskGroup(it) },
        onTaskCompleted = { taskViewModel.onTaskCompleted(it) },
        taskcompleted = taskViewModel.taskCompleted,
        taskImportant = taskViewModel.taskImportant,
        importantTasksCount = importantTasksCountState.value,
        onTaskImportant = { taskViewModel.onTaskImportant(it) },
        addGroupSelected = taskViewModel.addgroupSelected,
        onAddgroupSelected = { taskViewModel.onAddgroupSelected() },
        newGroup = taskViewModel.newGroup,
        onAddNewGroup = { taskViewModel.OnAddNewGroup(it)},
        saveNewGroup = { taskViewModel.saveNewGroup(it) },
        onDeleteTask = { taskViewModel.onDeleteTask(it)}
    )
 */