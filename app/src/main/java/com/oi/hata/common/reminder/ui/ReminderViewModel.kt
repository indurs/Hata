package com.oi.hata.common.reminder.ui

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oi.hata.common.reminder.data.local.datasource.HataReminderDatasource
import com.oi.hata.common.reminder.data.local.model.ReminderMaster
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.common.util.ReminderUtil.CUSTOM
import com.oi.hata.common.util.ReminderUtil.EVERYDAY
import com.oi.hata.common.util.ReminderUtil.PICKADATE
import com.oi.hata.common.util.ReminderUtil.TODAY
import com.oi.hata.common.util.ReminderUtil.TOMORROW
import com.oi.hata.data.HataDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(val hataReminderDatasource: HataReminderDatasource): ViewModel() {

    var months by mutableStateOf<List<String>>(emptyList())
    var dates by mutableStateOf<List<Int>>(emptyList())
    var weeks by mutableStateOf<List<String>>(emptyList())
    var weeknums by mutableStateOf<List<Int>>(emptyList())

    var reminder by mutableStateOf("")
    var choosenType by mutableStateOf(ReminderUtil.WhenSelectType.NONE)
    var reminderTime by mutableStateOf("Time")
    var reminderDueDate by mutableStateOf("")
    var reminderTxt by mutableStateOf("")

    var dueDateSelected by mutableStateOf(false)
    var pickDateSelected by mutableStateOf(false)
    var reminderSelected by mutableStateOf(false)
    var timeSelected by mutableStateOf(false)
    var taskselected by mutableStateOf(false)
    var reminderOptSelected by mutableStateOf(ReminderUtil.NONE)
    var reminderCustomSelected by mutableStateOf(false)
    var pickAdate by mutableStateOf("")

    lateinit var dueDate: LocalDate
    var reminderEndYear: Int = 0

    init{
       // Log.d("REMINDER VIEW MODEL","INITIAL>>>>>>>>>>>>>>>>>>")
    }

    fun onMonthSelected(month: String){
        Log.d("onMonthSelected", month)
        val mths = mutableListOf<String>()
        mths.addAll(months)
        if(mths.contains(month))
            mths.remove(month)
        else
            mths.add(month)

        months = mths

        buildReminder()

        //_selectedMonths.value = month
    }

    fun onDateSelected(date: Int){
        //_selectedDates.value = date
        //Log.d("onDateSelected ", "" + date)
        val dts = mutableListOf<Int>()
        dts.addAll(dates)
        if(dts.contains(date))
            dts.remove(date)
        else
            dts.add(date)

        dates = dts

        buildReminder()
    }

    fun onWeekSelected(week: String){
        //_selectedDates.value = date
        //Log.d("onWeekSelected ", week)
        val wks = mutableListOf<String>()
        wks.addAll(weeks)
        if(wks.contains(week))
            wks.remove(week)
        else
            wks.add(week)

        weeks = wks
        if(weeks.size == 0)
            weeknums = emptyList()
        buildReminder()
    }

    fun onWeekNumSelected(weeknum: Int){
        //_selectedDates.value = date
        Log.d("onWeekNumSelected ", ""+weeknum)
        val wkns = mutableListOf<Int>()
        wkns.addAll(weeknums)
        if(wkns.contains(weeknum))
            wkns.remove(weeknum)
        else
            wkns.add(weeknum)

        weeknums = wkns
        buildReminder()
    }

    fun onReminderTxtChange(reminder: String){
        reminderTxt = reminder
    }

    fun onPickaDateSelected(selected: Boolean){
        pickDateSelected = selected
    }

    fun onReminderOptionSelected(remOption:String){
        reminderOptSelected = remOption
        pickAdate = ""

        when(remOption){
            TODAY -> {
                val today = LocalDate.now()
                addMonthDate(today)
                pickDateSelected = false
            }
            TOMORROW -> {
                val tomorrow = LocalDate.now().plusDays(1)
                addMonthDate(tomorrow)
                pickDateSelected = false
            }
            EVERYDAY -> {
                pickDateSelected = false
            }
            PICKADATE -> {
                pickDateSelected = true
            }
        }
    }

    fun onReminderCustomClick(){
        pickAdate = ""
        if(!reminderCustomSelected) {
            reminderOptSelected = ReminderUtil.NONE
            reminderCustomSelected = true
            clearCustomReminderValues()
        }else
            reminderCustomSelected = false
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

    fun onPickaDateSelect(year: Int,month: Int, day: Int){

        val dts = mutableListOf<Int>()
        dts.add(day)
        dates = dts

        val mths = mutableListOf<String>()
        mths.add(ReminderUtil.monthsStr[month]!!)
        months = mths

        reminderEndYear = year
        pickAdate = constructDate(year,month,day)

        buildReminder()
    }

    fun onTimeSelect(hour: Int, minute: Int, am: Boolean){
        var am_pm = ""
        if(am) am_pm = ReminderUtil.AM_PM.AM.name else ReminderUtil.AM_PM.PM.name
        reminderTime = hour as String + ":" + minute as String + am_pm
    }

    fun onTimeSelected(){
        timeSelected = !timeSelected
    }

    fun onTaskSelected(selected: Boolean){
        taskselected = selected
    }

    fun onReminderSelected(selected: Boolean){
        reminderSelected = selected
    }

    fun constructDate(year: Int,month: Int, day: Int): String{
        var date = LocalDate.of(year,month,day)
        return date.toString()
    }

    fun addMonthDate(date: LocalDate){
        val dts = mutableListOf<Int>()
        System.out.println((date.dayOfMonth))
        dts.add(date.dayOfMonth)
        dates = dts

        val mths = mutableListOf<String>()
        mths.add(ReminderUtil.monthsStr[date.monthValue]!!)
        months = mths
        buildReminder()
    }

    fun buildReminder(){

        if(reminderOptSelected == TODAY ||
            reminderOptSelected == TOMORROW ||
            reminderOptSelected == PICKADATE){

            choosenType =  ReminderUtil.WhenSelectType.MONTHDATE
            reminderCustomSelected = false

        }

        if(reminderCustomSelected) {
            setCustomReminderType()
            reminder = buildReminderStr()
        }else{
            reminder = ""
        }

        Log.d("buildreminder ", " choosen type"+ choosenType + "reminder >>>>>>>>>>>>>>>>>>>>>>>>>"+reminder)

    }

    fun setCustomReminderType(){
        if(!months.isEmpty() && dates.isEmpty() && weeks.isEmpty())
            choosenType = ReminderUtil.WhenSelectType.MONTH
        if(months.isEmpty() && !dates.isEmpty() && weeks.isEmpty())
            choosenType =  ReminderUtil.WhenSelectType.DATE
        if(months.isEmpty() && dates.isEmpty() && !weeks.isEmpty())
            choosenType =  ReminderUtil.WhenSelectType.WEEK
        if(months.isEmpty() && dates.isEmpty() && !weeks.isEmpty() && !weeknums.isEmpty())
            choosenType =  ReminderUtil.WhenSelectType.WEEKNUM
        if(!months.isEmpty()){
            if(!dates.isEmpty()){
                choosenType =  ReminderUtil.WhenSelectType.MONTHDATE
            }
            else if(!weeks.isEmpty()){
                if(!weeknums.isEmpty()){
                    choosenType =  ReminderUtil.WhenSelectType.MONTHWEEKNUM
                }else{
                    choosenType =  ReminderUtil.WhenSelectType.MONTHWEEK
                }
            }
        }
    }

    fun clearCustomReminderValues(){
        months = emptyList()
        dates = emptyList()
        weeks = emptyList()
        weeknums = emptyList()
        choosenType = ReminderUtil.WhenSelectType.NONE
    }

    fun clearReminderValues(){
        clearCustomReminderValues()

        reminderTime = ""
        reminderDueDate = ""
        reminderOptSelected = ""
        pickAdate = ""

    }

    fun getTodaysReminders(): Flow<List<ReminderMaster>> = flow{

        hataReminderDatasource.getTodaysReminders().flowOn(Dispatchers.IO).collect {
                    emit(it)
        }

    }

    /* getTodaysReminders(): Flow<List<ReminderMaster>> = flow{

       viewModelScope.launch {
           hataReminderDatasource.getTodaysReminders().collect { emit(it) }
       }

    }
    */

    fun buildReminderStr(): String{
        Log.d("buildReminderStr","buildReminderStr >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        var reminder = ""
        when(choosenType) {
            ReminderUtil.WhenSelectType.DATE -> {
                reminder = dates.map {
                    buildNumSuffix(it)
                }.joinToString(separator = ",", postfix = ReminderPreSuffix.EVERY_MONTH.title)

            }
            ReminderUtil.WhenSelectType.MONTH -> {
                reminder = months.joinToString(separator = ",", prefix = ReminderPreSuffix.EVERY_DAY.title )
            }
            ReminderUtil.WhenSelectType.WEEK -> {
                reminder = weeks.joinToString(
                    separator = ",",
                    postfix = ReminderPreSuffix.EVERY_MONTH.title
                )
            }
            ReminderUtil.WhenSelectType.WEEKNUM -> {
                reminder = weeknums.map {
                    buildNumSuffix(it)
                }.joinToString(separator = ",",
                    postfix =  " " + weeks.joinToString(separator = ",") + ReminderPreSuffix.EVERY_MONTH.title)
            }
            ReminderUtil.WhenSelectType.MONTHDATE -> {
                reminder = dates.map {
                    buildNumSuffix(it)
                }.joinToString(
                    separator = " , ",
                    postfix = ReminderPreSuffix.OF.title + " " + months.joinToString(separator = ",")
                )
            }
            ReminderUtil.WhenSelectType.MONTHWEEK -> {
                reminder = weeks.joinToString(
                    separator = " , ",
                    postfix = " " + ReminderPreSuffix.OF.title + " " + months.joinToString(separator = ",")
                )
            }
            ReminderUtil.WhenSelectType.MONTHWEEKNUM -> {
                reminder = weeknums.map {
                    buildNumSuffix(it)
                }.joinToString(
                    separator = ",",
                    postfix = " " + weeks.joinToString(separator = ",") + ReminderPreSuffix.OF.title +  " " +months.joinToString(
                        separator = ","
                    )
                )
            }
            //ReminderUtil.WhenSelectType.
        }

        return reminder
    }

    fun buildNumSuffix(num: Int): String{

        if((num == 1) || (num == 21) || num == 31)
            return num.toString() + ReminderPreSuffix.ST.title
        else if(num==2 || num == 22)
            return num.toString() +  ReminderPreSuffix.ND.title
        else if(num==3 || num == 23)
            return num.toString() + ReminderPreSuffix.RD.title
        else
            return num.toString() + ReminderPreSuffix.TH.title
    }

    fun saveReminder(){
        Log.d("saveReminder >>>","saveReminder>>>>>>>>>>>>>>>>>>>whenSelectType "+choosenType!!.name + "reminderOptSelected "+reminderOptSelected)

        viewModelScope.launch {
            hataReminderDatasource.insertReminder(
                reminderTxt = reminderTxt,
                alarmScreenVal = reminder,
                reminderTime = reminderTime,
                reminderDueDate = dueDate,
                whenSelectType = choosenType!!.name,
                reminderMonths = months,
                reminderDates = dates,
                reminderWeeks = weeks,
                reminderWeekNum = weeknums,
                reminderEndYear = reminderEndYear,
                reminderOption = reminderOptSelected,
                remoptPickDate = pickAdate
            )
        }

    }

    enum class ReminderPreSuffix(val title: String) {
        EVERY_DAY("Every day in"),
        EVERY_MONTH(" of every month"),
        ST("st"),
        ND("nd"),
        RD("rd"),
        TH("th"),
        OF("of")
    }


}