package com.oi.hata.common.reminder.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.oi.hata.common.reminder.data.local.model.HataReminder
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.common.util.ReminderUtil.CUSTOM
import com.oi.hata.common.util.ReminderUtil.EVERYDAY
import com.oi.hata.common.util.ReminderUtil.NONE
import com.oi.hata.common.util.ReminderUtil.PICKADATE
import com.oi.hata.common.util.ReminderUtil.TIME
import com.oi.hata.common.util.ReminderUtil.TODAY
import com.oi.hata.common.util.ReminderUtil.TOMORROW
import com.oi.hata.common.util.ReminderUtil.buildNumSuffix
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(): ViewModel() {

    var months by mutableStateOf<List<String>>(emptyList())
    var dates by mutableStateOf<List<Int>>(emptyList())
    var weeks by mutableStateOf<List<String>>(emptyList())
    var weeknums by mutableStateOf<List<Int>>(emptyList())

    var reminder by mutableStateOf("")
    var choosenType by mutableStateOf(ReminderUtil.WhenSelectType.NONE)
    var reminderTime by mutableStateOf(TIME)


    var pickDateSelected by mutableStateOf(false)
    var reminderSelected by mutableStateOf(false)

    var reminderOptSelected by mutableStateOf(NONE)
    var reminderPrevOpt = NONE
    var reminderCustomSelected by mutableStateOf(false)
    var pickAdate by mutableStateOf("")

    var reminderTimeSelected by mutableStateOf(false)


    var reminderEndYear: Int = 0


    fun onMonthSelected(month: String){
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
        val wkns = mutableListOf<Int>()
        wkns.addAll(weeknums)
        if(wkns.contains(weeknum))
            wkns.remove(weeknum)
        else
            wkns.add(weeknum)

        weeknums = wkns
        buildReminder()
    }

    fun onPickaDateSelected(selected: Boolean){
        pickDateSelected = selected
    }

    fun onReminderOptionSelected(remOption:String){
        reminderPrevOpt = reminderOptSelected
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

    fun onReminderCustomClick(hataReminder: HataReminder?){
        reminderPrevOpt = reminderOptSelected
        pickAdate = ""
        reminderCustomSelected = true
        reminderOptSelected = CUSTOM

        // clearCustomReminderValues()
        resetCustomReminderValues()

        if (hataReminder != null) {
            initReminderValues(hataReminder)
        }

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
        reminderTime = hour.toString() + ":" + minute.toString() + " " + am_pm
    }

    fun onReminderTimeSelected(selected: Boolean){
        reminderTimeSelected = selected
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
            reminderOptSelected = CUSTOM
        }else{
            reminder = ""
        }

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

    fun clearCustomReminderValues(hataReminder: HataReminder?){
        months = emptyList()
        dates = emptyList()
        weeks = emptyList()
        weeknums = emptyList()
        choosenType = ReminderUtil.WhenSelectType.NONE
        reminderOptSelected = CUSTOM

        reminderCustomSelected = false
        reminder = " "
    }

    fun resetCustomReminderValues(){
        months = emptyList()
        dates = emptyList()
        weeks = emptyList()
        weeknums = emptyList()
        choosenType = ReminderUtil.WhenSelectType.NONE
        reminder = ""
    }

    fun initReminderValues(hataReminder: HataReminder?){
        if(hataReminder!=null){
            if(hataReminder.reminderMonths!=null)
                months = hataReminder.reminderMonths
            if(hataReminder.reminderDates!=null)
                dates = hataReminder.reminderDates
            if(hataReminder.reminderWeeks !=null)
                weeks = hataReminder.reminderWeeks
            if(hataReminder.reminderWeekNum != null)
                weeknums = hataReminder.reminderWeekNum

            choosenType = ReminderUtil.WhenSelectType.valueOf(hataReminder.remCustomWhenSelectType)
            reminder = hataReminder.alarmScreenVal

            reminderOptSelected = hataReminder.reminderOption
            reminderTime = hataReminder.reminderTime
            pickAdate = hataReminder.remoptPickDate

            if(reminderOptSelected == CUSTOM)
                reminderCustomSelected = true
        }else{
            reminderOptSelected = NONE
            pickAdate = ""
            pickDateSelected = false
            reminderTimeSelected = false
            reminderTime = TIME

        }
    }

    fun buildReminderStr(): String{
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

        }

        return reminder
    }


    fun getReminderValues(): HataReminder{

        return HataReminder(
            reminderMonths = months,
            reminderDates = dates,
            reminderWeeks = weeks,
            reminderWeekNum = weeknums,
            alarmScreenVal = reminder,
            remCustomWhenSelectType = choosenType!!.name,
            reminderFormat = "",
            reminderOption = reminderOptSelected,
            remoptPickDate = pickAdate,
            reminderEndYear = 0,
            reminderTime = reminderTime,
            reminderId = 0
        )
    }

    enum class ReminderPreSuffix(val title: String) {
        EVERY_DAY("Every day in "),
        EVERY_MONTH(" of every month"),
        ST("st "),
        ND("nd "),
        RD("rd "),
        TH("th "),
        OF("of ")
    }

}