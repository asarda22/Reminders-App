package com.example.reminderapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reminderapp.data.entity.Category
import com.example.reminderapp.data.entity.Reminder

@Database(
    entities = [Category::class, Reminder::class],
    version = 5,
    exportSchema = false
)
abstract class ReminderAppDatabase : RoomDatabase() {
    abstract fun categoryDao() : CategoryDao

    abstract fun reminderDao() : ReminderDao
}