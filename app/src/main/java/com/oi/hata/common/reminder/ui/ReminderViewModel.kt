package com.oi.hata.common.reminder.ui

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.oi.hata.common.reminder.data.local.datasource.HataReminderDatasource
import com.oi.hata.data.HataDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(val hataReminderDatasource: HataReminderDatasource): ViewModel() {

    var months by mutableStateOf<List<String>>(emptyList())
    var dates by mutableStateOf<List<Int>>(emptyList())
    var weeks by mutableStateOf<List<String>>(emptyList())
    var weeknums by mutableStateOf<List<Int>>(emptyList())

    var reminder by mutableStateOf("")

    var choosenType: ChoosenType? = null


    fun onMonthSelected(month: String){
       // Log.d("onMonthSelected", month)
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

    fun buildReminder(){

        if(!months.isEmpty() && dates.isEmpty() && weeks.isEmpty())
            choosenType = ChoosenType.MONTH
        if(months.isEmpty() && !dates.isEmpty() && weeks.isEmpty())
            choosenType = ChoosenType.DATE
        if(months.isEmpty() && dates.isEmpty() && !weeks.isEmpty())
            choosenType = ChoosenType.WEEK
        if(months.isEmpty() && dates.isEmpty() && !weeks.isEmpty() && !weeknums.isEmpty())
            choosenType = ChoosenType.WEEKNUM
        if(!months.isEmpty()){
            if(!dates.isEmpty()){
                choosenType = ChoosenType.MONTHDATE
            }
            else if(!weeks.isEmpty()){
                if(!weeknums.isEmpty()){
                    choosenType = ChoosenType.MONTHWEEKNUM
                }else{
                    choosenType = ChoosenType.MONTHWEEK
                }
            }
        }
        if (choosenType != null) {
            reminder = buildReminderStr()
        }else{
            reminder = ""
        }

    }


   suspend fun saveReminder(){
       hataReminderDatasource.insertReminder(reminderTxt = "hello",alarmScreenVal = reminder,reminderTime = "",
                                                reminderDueDate = "",reminderDueMonth = "",reminderDueYear = "",
                                                whenSelectType = choosenType!!.name,
                                                reminderMonths = months,
                                                reminderDates = dates,
                                                reminderWeeks = weeks,reminderWeekNum = weeknums)
    }


    fun buildReminderStr(): String{
        var reminder = ""
        when(choosenType) {
            ChoosenType.DATE -> {
                reminder = dates.map {
                    buildNumSuffix(it)
                }.joinToString(separator = ",", postfix = ReminderPreSuffix.EVERY_MONTH.title)

            }
            ChoosenType.MONTH -> {
                reminder = months.joinToString(separator = ",", prefix = ReminderPreSuffix.EVERY_DAY.title )
            }
            ChoosenType.WEEK -> {
                reminder = weeks.joinToString(
                    separator = ",",
                    postfix = ReminderPreSuffix.EVERY_MONTH.title
                )
            }
            ChoosenType.WEEKNUM -> {
                reminder = weeknums.map {
                    buildNumSuffix(it)
                }.joinToString(separator = ",",
                    postfix =  " " + weeks.joinToString(separator = ",") + ReminderPreSuffix.EVERY_MONTH.title)
            }
            ChoosenType.MONTHDATE -> {
                reminder = dates.map {
                    buildNumSuffix(it)
                }.joinToString(
                    separator = " , ",
                    postfix = ReminderPreSuffix.OF.title + " " + months.joinToString(separator = ",")
                )
            }
            ChoosenType.MONTHWEEK -> {
                reminder = weeks.joinToString(
                    separator = " , ",
                    postfix = " " + ReminderPreSuffix.OF.title + " " + months.joinToString(separator = ",")
                )
            }
            ChoosenType.MONTHWEEKNUM -> {
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

    enum class ChoosenType { MONTH, DATE, WEEK, WEEKNUM, MONTHDATE, MONTHWEEK, MONTHWEEKNUM}

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