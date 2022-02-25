package com.example.reminderapp.util

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.reminderapp.ui.reminder.createReminderNotification
import java.lang.Exception

class NotificationWorker(
    context: Context,
    userParameters: WorkerParameters
) : Worker(context, userParameters) {

    override fun doWork(): Result {
        return try {
           /* for (i in 0..10) {
                Log.i("NotificationWorker", "Counted $i")
            }*/
            val opMsg = inputData.getString("msg")
            createReminderNotification(opMsg.toString())
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

}