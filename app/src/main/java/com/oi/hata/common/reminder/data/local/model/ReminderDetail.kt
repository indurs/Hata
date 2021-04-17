package com.oi.hata.common.reminder.data.local.model

import androidx.room.*

@Entity(
    tableName = "reminder_dateil",
    foreignKeys = [
    ForeignKey(
        entity = ReminderMaster::class,
        parentColumns = ["reminder_id"],
        childColumns = ["reminderdetail_reminder_id"],
        onDelete = ForeignKey.CASCADE
    )
],
    indices = [Index(value = ["reminderdetail_reminder_id"])]
)
data class ReminderDetail(
    @ColumnInfo(name = "reminder_detail_id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "dates_select") var datesSelect: String,
    @ColumnInfo(name = "months_select") var monthsSelect: String,
    @ColumnInfo(name = "weeks_select") var weeksSelect: String,
    @ColumnInfo(name = "week_nums_select") var weekNumsSelect: String,
    @ColumnInfo(name = "everyday_select") var everydaySelect: Boolean,
)
