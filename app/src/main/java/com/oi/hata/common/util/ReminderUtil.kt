package com.oi.hata.common.util

import java.util.*
import java.util.HashMap

import java.util.ArrayList


object ReminderUtil {

    var monthsStr = mapOf(1 to "Jan", 2 to "Feb", 3 to "Mar", 4 to "Apr", 5 to "May", 6 to "Jun", 7 to "Jul", 8 to "Aug", 9 to "Sep", 10 to "Oct", 11 to "Nov", 12 to "Dec")


    fun getMonthCalendarScreen(month: Int): ArrayList<ArrayList<Int>>
    {

        var calendar = GregorianCalendar()
        var date = calendar.get(Calendar.DAY_OF_MONTH)

        var year = calendar.get(Calendar.YEAR)


        var d = 0
        val datesByDay: ArrayList<ArrayList<Int>> = ArrayList<ArrayList<Int>>()
        datesByDay.add(ArrayList())
        datesByDay.add(ArrayList())
        datesByDay.add(ArrayList())
        datesByDay.add(ArrayList())
        datesByDay.add(ArrayList())
        datesByDay.add(ArrayList())
        datesByDay.add(ArrayList())

        if(month == 2 && isLeapYear(year))
            days[month] = 29

        var weekDay = 0
        var weekName = ""
        for(i in 1 .. days[month]){
            weekDay = weekDay(month,i,year)
            if(i == 1){
                if(weekDay > 0){
                    for(j in 0 until weekDay){
                        datesByDay[j].add(0)
                    }
                }
            }
            println(WEEKNAMES.values()[weekDay].toString() +" "+WEEKNAMES.values()[weekDay].ordinal)
            datesByDay[WEEKNAMES.values()[weekDay].ordinal]!!.add(i)
        }
        return datesByDay
    }

    fun getMonthCalendar(month: Int): HashMap<String, ArrayList<Int>> {

        var calendar = GregorianCalendar()
        var date = calendar.get(Calendar.DAY_OF_MONTH)

        var year = calendar.get(Calendar.YEAR)


        var d = 0
        val datesByDay: HashMap<String, ArrayList<Int>> = HashMap<String,ArrayList<Int>>()
        datesByDay.put(WEEKNAMES.Sun.name, ArrayList())
        datesByDay.put(WEEKNAMES.Mon.name, ArrayList())
        datesByDay.put(WEEKNAMES.Tue.name, ArrayList())
        datesByDay.put(WEEKNAMES.Wed.name, ArrayList())
        datesByDay.put(WEEKNAMES.Thu.name, ArrayList())
        datesByDay.put(WEEKNAMES.Fri.name, ArrayList())
        datesByDay.put(WEEKNAMES.Sat.name, ArrayList())

        if(month == 2 && isLeapYear(year))
            days[month] = 29

        var weekDay = 0
        var weekName = ""
        for(i in 1 .. days[month]){
            weekDay = weekDay(month,i,year)

            datesByDay[WEEKNAMES.values()[weekDay].name]!!.add(i)
        }
        return datesByDay
    }

    fun weekDay(month: Int, date: Int, year: Int): Int {
        val y: Int = year - (14 - month) / 12
        val x = y + y / 4 - y / 100 + y / 400
        val m = month + 12 * ((14 - month) / 12) - 2
        return (date + x + 31 * m / 12) % 7
    }

    fun isLeapYear(year: Int): Boolean {
        if  ((year % 4 == 0) && (year % 100 != 0)) return true;
        if  (year % 400 == 0) return true;
        return false
    }

    enum class WEEKNAMES{ Sun, Mon, Tue, Wed, Thu, Fri, Sat }

    var days = intArrayOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    enum class WhenSelectType {  MONTH, DATE, WEEK, WEEKNUM, MONTHDATE, MONTHWEEK, MONTHWEEKNUM,NONE }


    val PICKADATE = "Pick a Date"
    val EVERYDAY = "EveryDay"
    val TODAY = "Today"
    val TOMORROW = "Tomorrow"
    val CUSTOM = "Custom"
    val DUEDATE = "Due Date"
    val NONE = "None"
    val TIME = "Time"

    enum class AM_PM{AM, PM}


}