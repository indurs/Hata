package com.oi.hata.common.reminder.data.local

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class Converters {

    val formatter = DateTimeFormatter.ISO_DATE

    @TypeConverter
    fun toOffSetDateTime(value:String?): LocalDate?{
        return value?.let { formatter.parse(value,LocalDate::from) }
    }

    @TypeConverter
    fun fromOffSetDateTime(value: LocalDate?): String?{
        return value?.format(formatter)
    }
}