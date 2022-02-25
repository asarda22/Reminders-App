package com.example.reminderapp.data.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "reminders",
    indices = [
        Index("id",unique = true),
        Index("reminder_category_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["reminder_category_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Reminder(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")var reminderId: Long=0,
    @ColumnInfo(name = "reminder_message")val reminderMessage: String,
    @ColumnInfo(name = "reminder_date") val reminderDate: String,
    @ColumnInfo(name= "reminder_category_id") val reminderCategoryId: Long,
    @ColumnInfo(name = "location_x") val location_x : Double?,
    @ColumnInfo(name = "loaction_y") val location_y : Double?,
    @ColumnInfo(name = "reminder_time") val reminderTime: String?
): Parcelable
