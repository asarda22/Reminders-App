package com.example.reminderapp

import android.app.Application

class ReminderAppApplication : Application() {
    override fun onCreate(){
        super.onCreate()
        Graph.provide(this)
    }
}