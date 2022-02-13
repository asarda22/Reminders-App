package com.example.reminderapp.data.repository

import android.util.Log
import com.example.reminderapp.data.entity.Reminder
import com.example.reminderapp.data.room.ReminderDao
import com.example.reminderapp.data.room.ReminderToCategory
import kotlinx.coroutines.flow.Flow

class ReminderRepository(
    private val reminderDao : ReminderDao
) {
    //fetch reminders with their respective categories
    fun remindersInCategory(categoryId: Long) : Flow<List<ReminderToCategory>> {
        return reminderDao.remindersFromCategory(categoryId)
    }

    //Add new reminder
    suspend fun addReminder(reminder: Reminder):Long {
        return when (val local = reminderDao.reminder(reminder.reminderId)) {
            null -> reminderDao.insert(reminder)
            else -> local.reminderId
        }
    }

    //Update the reminder
    suspend fun updateReminder(reminder:Reminder) {
        when (val ri = reminderDao.reminder(reminder.reminderId)) {
            null -> Log.i("id",reminder.reminderId.toString())
            else -> reminderDao.update(reminder)
        }
    }

    suspend fun deleteReminder(reminder : Reminder) = reminderDao.delete(reminder)
}