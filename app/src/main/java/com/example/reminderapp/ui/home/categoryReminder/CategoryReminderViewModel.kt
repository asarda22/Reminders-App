package com.example.reminderapp.ui.home.categoryReminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.data.entity.Reminder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class CategoryReminderViewModel : ViewModel() {
    private val _state = MutableStateFlow(CategoryReminderViewState())

    val state: StateFlow<CategoryReminderViewState>
        get() = _state

    init {
        val list = mutableListOf<Reminder>()
        for (x in 1..5) {
            list.add(
                Reminder(
                    reminderId = x.toLong(),
                    reminderTitle = "Study reminder $x",
                    reminderCategory = "Study",
                    reminderDate = Date()
                )
            )
        }
        for (x in 6..10) {
            list.add(
                Reminder(
                    reminderId = x.toLong(),
                    reminderTitle = "Study reminder $x",
                    reminderCategory = "Study",
                    reminderDate = Date(122,Calendar.MONTH-1,
                        Calendar.DAY_OF_MONTH+2)
                )
            )
        }
        for (x in 11..15) {
            list.add(
                Reminder(
                    reminderId = x.toLong(),
                    reminderTitle = "Study reminder $x",
                    reminderCategory = "Study",
                    reminderDate = Date(122,Calendar.MONTH-1,
                        Calendar.DAY_OF_MONTH + 3)
                )
            )
        }

        viewModelScope.launch {
            _state.value = CategoryReminderViewState(
                reminders = list
            )
        }
    }
}

data class CategoryReminderViewState(
    val reminders: List<Reminder> = emptyList()
)