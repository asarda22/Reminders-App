package com.example.reminderapp.ui.reminder

import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import com.example.reminderapp.Graph
import com.example.reminderapp.data.entity.Category
import com.example.reminderapp.data.entity.Reminder
import com.example.reminderapp.data.repository.CategoryRepository
import com.example.reminderapp.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import androidx.work.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import com.example.reminderapp.R
import com.example.reminderapp.util.NotificationWorker
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ReminderViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
    private val categoryRepository: CategoryRepository = Graph.categoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ReminderViewState())

    val state: StateFlow<ReminderViewState>
        get() = _state

    suspend fun saveReminder(reminder: Reminder): Long {
        Log.i("t",reminder.reminderTime.toString())
        if(reminder.reminderTime.toString().isNotEmpty()) {
            setOneTimeNotification(reminder)
        }
        return reminderRepository.addReminder(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        return reminderRepository.deleteReminder(reminder)
    }

    suspend fun updateReminder(reminder:Reminder){
        Log.i("log",reminder.reminderMessage)
        if(reminder.reminderTime.toString().isNotEmpty()) {
            setOneTimeNotification(reminder)
        }
        return reminderRepository.updateReminder(reminder)
    }

    init {
        viewModelScope.launch {
            categoryRepository.categories().collect { categories ->
                _state.value = ReminderViewState(categories)
            }
        }
    }

}

private fun setOneTimeNotification(reminder: Reminder) {
    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val remMsg = Data.Builder()
    remMsg.putString("msg",reminder.reminderMessage)

   // Log.i("dt",reminder.reminderDate)
    //Log.i("tm",reminder.reminderTime.toString())
    var sdf : SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
    var rDt : Date = sdf.parse(reminder.reminderDate + " "+ reminder.reminderTime)
    var rMls  = rDt.time
    //Log.i("rmls",rMls.toString())

    val c = Calendar.getInstance()
    //Log.i("cmls",c.timeInMillis.toString())


    val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(rMls - c.timeInMillis, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .setInputData(remMsg.build())
        .build()

    workManager.enqueue(notificationWorker)

    //Monitoring for state of work
    /*workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                createReminderNotification(message)
            }
            /*else {
                createErrorNotification()
            }*/
        }*/
}

fun createReminderNotification(reminderMsg: String) {
    val notificationId = 2
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("It's time to")
        .setContentText("${reminderMsg}")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    with(NotificationManagerCompat.from(Graph.appContext)) {
        notify(notificationId, builder.build())
    }
}

data class ReminderViewState(
    val categories: List<Category> = emptyList()
)