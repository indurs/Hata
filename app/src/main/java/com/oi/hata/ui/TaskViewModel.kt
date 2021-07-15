package com.oi.hata.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oi.hata.common.reminder.data.local.datasource.HataReminderDatasource
import com.oi.hata.common.reminder.data.local.model.HataReminder
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.data.model.Group
import com.oi.hata.task.data.model.GroupTask
import com.oi.hata.task.data.model.Task
import com.oi.hata.task.data.model.TaskUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(val hataReminderDatasource: HataReminderDatasource): ViewModel(){

    var hataReminderValues: HataReminder? = null
    var taskTxt by mutableStateOf("")
    var reminderDueDate by mutableStateOf("")
    var dueDateSelected by mutableStateOf(false)
    var reminderTime by mutableStateOf("Time")
    var taskselected by mutableStateOf(false)

    var selectedTaskGroup by mutableStateOf(GroupTask(Group(1,"Tasks"), emptyList()))

    lateinit var dueDate: LocalDate

    var taskUIState: TaskUIState = TaskUIState(null,null)

    fun onTaskTxtChange(reminder: String){
        taskTxt = reminder
    }


    fun onDueDateSelect(year: Int,month: Int, day: Int){
        var mth = if(month < 10) "0" + month.toString() else month.toString()
        var dt = if(day < 10) "0" + day.toString() else day.toString()

        reminderDueDate = year.toString() + "-" + mth + "-" + dt

        var localDateTime = LocalDateTime.of(year,month,day,0,0)

        //var offsetDateTime = OffsetDateTime.now()
        dueDate = LocalDate.parse(reminderDueDate, DateTimeFormatter.ISO_LOCAL_DATE)
        dueDateSelected = true
    }

    fun saveTask(groupId: Long){
        viewModelScope.launch {
            if(taskUIState.task != null && taskUIState.task!!.taskReminderId > 0 ){
                taskUIState.task!!.task = taskTxt
                taskUIState.task!!.taskDueDate = dueDate
                hataReminderDatasource.updateTaskReminder(
                    hataReminder = taskUIState.hataReminder!!, task = taskUIState.task!!
                )
            }else{
                hataReminderDatasource.insertTaskReminder(
                    hataReminder = taskUIState.hataReminder!!, task = Task(task = taskTxt,taskDueDate = dueDate,tag="",taskReminderId = 0,taskGroupId = groupId)
                )
            }

        }
        resetValues()

    }

    fun saveTaskReminder(hataReminder: HataReminder){
        println("saveTaskReminder>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+hataReminder.reminderOption)
        taskUIState.hataReminder = null
        if(hataReminder.reminderOption != ReminderUtil.CUSTOM)
            taskUIState.hataReminder = hataReminder
        else {
            println(" saveTaskReminder TIME >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+hataReminderValues!!.reminderTime)
            taskUIState.hataReminder = hataReminderValues
        }
    }

    fun resetValues(){
        println("resetValues >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

        hataReminderValues = null
        taskUIState.hataReminder = null
        taskUIState.task = null
        reminderDueDate = ""
        taskTxt = ""
        reminderTime = ""
        dueDateSelected = false
    }

    fun resetTaskReminder(){
        println("resetTaskReminder >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        hataReminderValues = null
        taskUIState.hataReminder = null
    }

    fun resetReminder(){
        println("resetReminder >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        hataReminderValues = null
    }

    fun setReminder(hataReminder: HataReminder){
        println("setReminder "+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+hataReminder.reminderMonths)

        hataReminderValues = hataReminder
    }

    fun setReminderFromTaskUIState(){
        hataReminderValues = taskUIState.hataReminder
    }

    /*fun  onTaskItemClick(reminderId: Long) {
        Log.d("onTaskItemClick","onTaskItemClick>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        viewModelScope.launch {
            hataReminderDatasource.getReminder(reminderId).flowOn(Dispatchers.IO).collect {
                taskUIState.hataReminder = it
                taskselected = true
                reminderDueDate = it.
            }
        }

    }*/

    fun  onTaskItemClick(taskId: Long) {

        viewModelScope.launch {
            hataReminderDatasource.getTask(taskId).flowOn(Dispatchers.IO).collect {
                taskUIState = it
                taskselected = true
                reminderDueDate = it.task!!.taskDueDate.toString()
                dueDate = it.task!!.taskDueDate
                taskTxt = it.task!!.task
                dueDateSelected = true
            }
        }

    }

    fun onTaskSelected(){
        println("onTaskSelected >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        taskselected = !taskselected
    }

    fun onSelectedTaskGroup(task: GroupTask){
        selectedTaskGroup = task
    }

    fun getReminder(): HataReminder?{

        if(hataReminderValues!=null){
            println("gET Remidner >>>>>>>>>>>>>>>>>>>>>>>>>>"+hataReminderValues!!.reminderMonths)
        }else{
            println("gET Remidner >>>>>>>>>>>>>>>>>>>>>>>>>> NULL")
        }
        return hataReminderValues
    }

    fun getTaskReminder(): HataReminder?{
        return taskUIState.hataReminder
    }

    fun getCustomReminderTxt(optSelected: String): String{
        var reminder = ""
        if(optSelected != ReminderUtil.CUSTOM)
            return reminder
        if(hataReminderValues!=null){
            println("getCustomReminderTxt >>>>>>>>>>>>>>>>>>>>>>>> 7777777777777777777777777777777777 ************************************************************************************")
            reminder = hataReminderValues!!.alarmScreenVal
        }
        return reminder
    }

    fun getReminderTxt(): String {
        if(taskUIState.hataReminder!=null){
            if(taskUIState.hataReminder!!.reminderOption == ReminderUtil.CUSTOM)
                return taskUIState.hataReminder!!.alarmScreenVal
            else
                return taskUIState.hataReminder!!.reminderOption
        }
        return ""
    }

    fun getGroupTasks(): Flow<List<GroupTask>> = flow {
        hataReminderDatasource.getGroupTasks().flowOn(Dispatchers.IO).collect {
            println("no of gropus >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+it.size)

            emit(it)
        }
    }

    fun getGroupTask(groupName:String): Flow<GroupTask> = flow {
        hataReminderDatasource.getGroupTask(groupName).flowOn(Dispatchers.IO).collect {
            println("getGroupTask >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+it.Group)
            emit(it)
        }
    }

}
