package com.example.reminderapp

import android.content.Context
import androidx.room.Room
import com.example.reminderapp.data.repository.CategoryRepository
import com.example.reminderapp.data.repository.ReminderRepository
import com.example.reminderapp.data.room.ReminderAppDatabase

object Graph {

    lateinit var database : ReminderAppDatabase

    lateinit var appContext: Context

    val categoryRepository by lazy{
        CategoryRepository(
            categoryDao = database.categoryDao()
        )
    }

    val reminderRepository by lazy{
        ReminderRepository(
            reminderDao = database.reminderDao()
        )
    }

    fun provide(context: Context){
        appContext = context
        database = Room.databaseBuilder(context, ReminderAppDatabase::class.java,"reminders.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}