package com.oi.hata.common.reminder.data.local

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class Converters {

    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun toOffSetDateTime(value: String?): OffsetDateTime? {
        return value?.let { formatter.parse(value, OffsetDateTime::from) }
    }

    @TypeConverter
    fun fromOffSetDateTime(value: OffsetDateTime?): String? {
        return value?.format(formatter)
    }
}