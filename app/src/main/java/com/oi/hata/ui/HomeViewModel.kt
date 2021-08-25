package com.oi.hata.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.oi.hata.common.reminder.data.local.datasource.HataReminderDatasource
import com.oi.hata.common.reminder.data.local.model.HataReminder
import com.oi.hata.common.reminder.data.local.model.ReminderMaster
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.data.HataTaskDatasource
import com.oi.hata.task.data.model.CalendarColumn
import com.oi.hata.task.data.model.CalendarTaskItem
import com.oi.hata.task.data.model.Task
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
import java.util.*
import javax.annotation.concurrent.Immutable
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    val hataReminderDatasource: HataReminderDatasource,
    val hataTaskDatasource: HataTaskDatasource
): ViewModel(){

    private val _homeUiState = MutableLiveData<HomeUiState>()
    val homeUiState: LiveData<HomeUiState> = _homeUiState

    var currentTab by mutableStateOf(HataHomeScreens.Today)
    var tasksForMonth by mutableStateOf<TreeMap<Int,MutableList<CalendarTaskItem>>>(TreeMap())
    var calendar = GregorianCalendar()
    var currentMonth by mutableStateOf(calendar.get(Calendar.MONTH)+1)
    var calendarView by mutableStateOf(true)
    var selectedCalendarDate by mutableStateOf(0)

    fun onSelectTab(hataScreens: HataHomeScreens){
        currentTab = hataScreens
    }

    /*fun getReminderOption(): String{
        if(hataReminderValues!=null){
            return hataReminderValues!!.reminderOption
        }else
            return ""
    }*/

    fun getReminders(whenType: String): Flow<List<Task>> = flow{

        hataReminderDatasource.getReminders(whenType).flowOn(Dispatchers.IO).collect {
            emit(it)
        }

    }

     fun getTasksForMonth(month:Int):Flow<TreeMap<Int, CalendarColumn>> = flow{

        hataTaskDatasource.getTasksForMonth(month).collect {
            emit(it)
        }
    }

    fun getTasksForCalendarDate(month: Int,date:Int) = flow {
        hataTaskDatasource.getTasksForCalendarDate(month,date).flowOn(Dispatchers.IO).collect {
            emit(it)
        }
    }

    init{
        Log.d("HOME VIEW MODEL","INITIAL>>>>>>>>>>>>>>>>>>*********************>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
    }

    fun getMonthCalendar():ArrayList<ArrayList<Int>>{
        return ReminderUtil.getMonthCalendarScreen(8)
    }

    fun onSelectCalendarDate(date: Int){
        selectedCalendarDate = date
    }

    fun setCalendarView(){
        calendarView = !calendarView
    }

}


@Immutable
data class HomeUiState(
    val currentTab: HataHomeScreens? = null
)