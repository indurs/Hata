package com.oi.hata.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oi.hata.common.reminder.data.local.IHataReminderRepository
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.data.IHataTaskRepository
import com.oi.hata.task.data.model.CalendarColumn
import com.oi.hata.task.data.model.CalendarTaskItem
import com.oi.hata.task.data.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.annotation.concurrent.Immutable
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    val hataTaskRepository: IHataTaskRepository
) : ViewModel() {

    private val _homeUiState = MutableLiveData<HomeUiState>()
    val homeUiState: LiveData<HomeUiState> = _homeUiState

    var currentTab by mutableStateOf(HataHomeScreens.Today)
    var tasksForMonth by mutableStateOf<TreeMap<Int, MutableList<CalendarTaskItem>>>(TreeMap())
    var calendar = GregorianCalendar()
    var calendarView by mutableStateOf(true)
    var selectedCalendarDate by mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))
    var selectedCalendarMonth by mutableStateOf(ReminderUtil.monthsStr[calendar.get(Calendar.MONTH) + 1])
    var selectedCalendarYear by mutableStateOf(calendar.get(Calendar.YEAR))

    var prevSelectedMonth: String = ""

    fun onSelectTab(hataScreens: HataHomeScreens) {
        currentTab = hataScreens
    }


    fun getTasks(whenType: String): Flow<List<Task>> = flow {
        hataTaskRepository.getTasks(whenType).flowOn(Dispatchers.IO).collect {
            emit(it)
        }
    }

    fun getTasksForMonth(month: String): Flow<TreeMap<Int, CalendarColumn>> = flow {
        hataTaskRepository.getTasksForMonth(ReminderUtil.monthsNum[month]!!).flowOn(Dispatchers.IO).collect {
            emit(it)
        }
    }

    fun getTasksForCalendarDate(month: String, date: Int) = flow {
        var dt = date
        if (selectedCalendarMonth != prevSelectedMonth) {
            if (ReminderUtil.monthsStr[calendar.get(Calendar.MONTH) + 1] == selectedCalendarMonth) {
                dt = calendar.get(Calendar.DAY_OF_MONTH)
            } else {
                dt = 1
            }
            selectedCalendarDate = dt

            prevSelectedMonth = selectedCalendarMonth!!
        }

        hataTaskRepository.getTasks(ReminderUtil.monthsNum[month]!!, dt)
            .flowOn(Dispatchers.IO).collect {
            emit(it)
        }
    }

    fun onSelectCalendarDate(date: Int) {
        selectedCalendarDate = date
    }

    fun onSelectCalendarMonth(month: String) {
        prevSelectedMonth = selectedCalendarMonth!!
        selectedCalendarMonth = month
    }

    fun onSelectCalendarYear(year: Int) {
        selectedCalendarYear = year
    }

    fun setCalendarView() {
        calendarView = !calendarView
    }

    fun getMonthCalendarScreen(selectedCalendarMonth: Int): ArrayList<ArrayList<Int>> {
        return ReminderUtil.getMonthCalendarScreen(selectedCalendarMonth)
    }

}


@Immutable
data class HomeUiState(
    val currentTab: HataHomeScreens? = null
)