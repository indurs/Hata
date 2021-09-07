package com.oi.hata.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oi.hata.common.reminder.data.local.datasource.HataReminderDatasource
import com.oi.hata.common.reminder.data.local.model.HataReminder
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.data.HataTaskDatasource
import com.oi.hata.task.data.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
                                        val hataReminderDatasource: HataReminderDatasource,
                                        val hataTaskDatasource: HataTaskDatasource
                                        ): ViewModel(){

    var hataReminderValues: HataReminder? = null
    var taskTxt by mutableStateOf("")
    var reminderDueDate by mutableStateOf("")
    var dueDateSelected by mutableStateOf(false)
    var reminderTime by mutableStateOf("Time")
    var taskselected by mutableStateOf(false)
    var taskCompleted by mutableStateOf<List<Long>>(emptyList())
    var taskImportant by mutableStateOf<List<Long>>(emptyList())
    var todaysTasks by mutableStateOf<List<Long>>(emptyList())
    var alertDismiss by (mutableStateOf(false))

    var todayTask by mutableStateOf(Task(0, 0, 0, 0, "", "", 0, 0, 0, false, false,null))

    var addgroupSelected by mutableStateOf(false)
    var newGroup by mutableStateOf("")
    var deleteTask by mutableStateOf(Task(0,0,0,0,"","", 0,0,0,false,false,null))

    var selectedTaskGroup by mutableStateOf(GroupTask(Group(1,"Tasks"), emptyList()))

    var dueDate: Int = 0
    var dueMonth: Int = 0
    var dueYear: Int = 0

    var taskUIState: TaskUIState = TaskUIState(null,null)

        /*init {
        viewModelScope.launch {
            getGroupTask("Tasks").flowOn(Dispatchers.IO).collect {
                println("getGroupTask >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>COLLECT")

                selectedTaskGroup = it
            }
        }

    }*/

    fun onTaskTxtChange(reminder: String){
        taskTxt = reminder
    }

    fun onDueDateSelect(year: Int,month: Int, day: Int){

        reminderDueDate = buildDate(year,month,day)

        var localDateTime = LocalDateTime.of(year,month,day,0,0)

        //var offsetDateTime = OffsetDateTime.now()

        println("day "+day + " month "+month +"year "+year)
        dueDate = day
        dueMonth = month
        dueYear = year
        dueDateSelected = true
    }

    fun buildDate(year: Int,month: Int, day: Int): String{
        var mth = if(month < 10) "0" + month.toString() else month.toString()
        var dt = if(day < 10) "0" + day.toString() else day.toString()
        return dt + "-" + mth + "-" + year.toString()
    }

    fun saveTask(groupId: Long){
        viewModelScope.launch {
            if(taskUIState.task != null ){
                taskUIState.task!!.task = taskTxt
                taskUIState.task!!.taskDueDate = dueDate
                hataReminderDatasource.updateTaskReminder(
                    hataReminder = taskUIState.hataReminder!!, task = taskUIState.task!!
                )
            }else{
                var impGroupid: Long
                if(groupId == 2L)
                    impGroupid = 2
                else
                    impGroupid = 22

                hataReminderDatasource.insertTaskReminder(
                    hataReminder = taskUIState.hataReminder,
                    task = Task(
                        task = taskTxt,
                        taskDueDate = dueDate,
                        taskDueMonth = dueMonth,
                        taskDueYear = dueYear,
                        tag ="",
                        taskReminderId = 0,
                        taskGroupId = groupId,
                        completed = false,
                        importantGroupId = impGroupid,
                        todaytask = false,
                        taskCreateDate = OffsetDateTime.now()
                    )
                )
            }

        }
        resetValues()

    }

    fun saveTaskReminder(hataReminder: HataReminder){
        taskUIState.hataReminder = null
        if(hataReminder.reminderOption != ReminderUtil.CUSTOM)
            taskUIState.hataReminder = hataReminder
        else {
            taskUIState.hataReminder = hataReminderValues
        }
    }



    fun resetValues(){
        hataReminderValues = null
        taskUIState.hataReminder = null
        taskUIState.task = null
        reminderDueDate = ""
        taskTxt = ""
        reminderTime = ""
        dueDateSelected = false
    }

    fun resetTaskReminder(){
        hataReminderValues = null
        taskUIState.hataReminder = null
    }

    fun resetReminder(){
        //println("resetReminder >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        hataReminderValues = null
    }

    fun setReminder(hataReminder: HataReminder){
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
                reminderDueDate = buildDate(it.task!!.taskDueYear,it.task!!.taskDueMonth,it.task!!.taskDueDate)
                dueDate = it.task!!.taskDueDate
                dueMonth = it.task!!.taskDueMonth
                dueYear = it.task!!.taskDueYear
                taskTxt = it.task!!.task
                dueDateSelected = true
            }
        }
    }

    fun onTaskSelected(){
        //println("onTaskSelected >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        taskselected = !taskselected
    }

    fun onTaskCompleted(task: Task){
        viewModelScope.launch {
            updateTaskAsCompleted(task)
            setCompleteTask(task)
        }
    }

    fun onTaskImportant(task: Task){

        viewModelScope.launch {
            updateTaskAsImportant(task)
            setImportantTask(task)
        }
    }

    fun onTaskSetForToday(task: Task){
        //println("onTaskSetForToday>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+  task.todaytask)

        viewModelScope.launch {
            updateTaskAsToday(task)
            setTaskToToday(task)
        }
    }

    fun onAlertDismiss(){
        alertDismiss = !alertDismiss
    }

    suspend fun setImportantTask(task: Task){
        withContext(Dispatchers.IO){
            var tasks = mutableListOf<Long>()

            tasks.addAll(taskImportant)

            if(tasks.contains(task.id))
                tasks.remove(task.id)
            else
                tasks.add(task.id)

            taskImportant = tasks
        }
    }

    suspend fun updateTaskAsImportant(task: Task){

        var importantGroupId: Long

        if(task.taskGroupId == 2L){
            withContext(Dispatchers.IO){
                hataTaskDatasource.deleteTask(task)
            }
        }else{
            if(task.importantGroupId == 2L){
                importantGroupId = 22
            }else{
                importantGroupId = 2L
            }
            withContext(Dispatchers.IO){
                hataTaskDatasource.updateTask(task = Task(id = task.id,
                    taskReminderId = task.taskReminderId,
                    taskGroupId = task.taskGroupId,
                    importantGroupId = importantGroupId,
                    task=task.task,
                    tag = "",
                    taskDueDate = task.taskDueDate,
                    taskDueMonth = task.taskDueMonth,
                    taskDueYear = task.taskDueYear,
                    completed = task.completed,
                    todaytask = task.todaytask,
                    taskCreateDate = task.taskCreateDate
                ))
            }
        }
    }

    suspend fun updateTaskAsCompleted(task: Task){
        var importantGroupId = 2L

        if(task.taskGroupId != 2L){
            importantGroupId = 22
        }
        withContext(Dispatchers.IO){
            hataTaskDatasource.updateTask(task = Task(id = task.id,
                taskReminderId = task.taskReminderId,
                taskGroupId = task.taskGroupId,
                importantGroupId = importantGroupId,
                task=task.task,
                tag = "",
                taskDueDate = task.taskDueDate,
                taskDueMonth = task.taskDueMonth,
                taskDueYear = task.taskDueYear,
                completed = !task.completed,
                todaytask = task.todaytask,
                taskCreateDate = task.taskCreateDate
            ))
        }
    }

    suspend fun updateTaskAsToday(task: Task){
        todayTask = task
        alertDismiss = false

        withContext(Dispatchers.IO){
            hataTaskDatasource.updateTask(task = Task(id = task.id,
                taskReminderId = task.taskReminderId,
                taskGroupId = task.taskGroupId,
                importantGroupId = task.importantGroupId,
                task=task.task,
                tag = "",
                taskDueDate = task.taskDueDate,
                taskDueMonth = task.taskDueMonth,
                taskDueYear = task.taskDueYear,
                completed = task.completed,
                todaytask = !task.todaytask,
                taskCreateDate = task.taskCreateDate
            ))
        }
    }

    fun setCompleteTask(task: Task){
        var tasks = mutableListOf<Long>()

        tasks.addAll(taskCompleted)
        if(tasks.contains(task.id))
            tasks.remove(task.id)
        else
            tasks.add(task.id)
        task.completed = true

        taskCompleted = tasks
    }

    suspend fun setTaskToToday(task: Task){
        withContext(Dispatchers.IO){
            var tasks = mutableListOf<Long>()

            tasks.addAll(todaysTasks)

            if(tasks.contains(task.id)) {
                tasks.remove(task.id)
            }
            else {
                tasks.add(task.id)
            }

            todaysTasks = tasks
        }
    }

    fun OnAddNewGroup(group: String){
        newGroup = group
    }

    fun saveNewGroup(group: String){
        var groupId:Long = 0
        viewModelScope.launch {
            groupId = hataTaskDatasource.insertGroup(Group(0,group))
        }

        selectedTaskGroup = GroupTask(Group(groupId,group), emptyList())
        addgroupSelected = false
        newGroup = ""
    }

    fun onDeleteTask(task: Task){
        //deleteTask = task
        viewModelScope.launch {
            hataTaskDatasource.deleteTask(task)
        }
    }

    fun deleteTask(){
        viewModelScope.launch {
            hataTaskDatasource.deleteTask(deleteTask)
        }
    }

    fun onSelectedTaskGroup(task: GroupTask){
        selectedTaskGroup = task
    }

    fun onAddgroupSelected(){
        println("onAddgroupSelected >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        addgroupSelected = !addgroupSelected
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
            emit(it)
        }
    }

    fun getGroupTask(groupName:String): Flow<GroupTask> = flow {

        hataReminderDatasource.getTasksForGroup(groupName).flowOn(Dispatchers.IO).collect {
            //println("getGroupTask >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ it.Group!!.name + " " +it.tasks!!.size + " group "+ it.tasks[0].taskGroupId +" igropu id "+it.tasks[0].importantGroupId)
            emit(it)
        }
    }

    fun getImportantTaskCount(): Flow<Int> = hataReminderDatasource.getImportantTasksCount()



    val IMPORTANT_GROUP_ID: Long = 2

}

