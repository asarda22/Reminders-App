package com.example.reminderapp.ui.reminder

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.reminderapp.Graph
import com.example.reminderapp.data.entity.Category
import com.example.reminderapp.data.entity.Reminder
import com.example.reminderapp.data.repository.CategoryRepository
import com.example.reminderapp.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class ReminderViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
    private val categoryRepository: CategoryRepository = Graph.categoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ReminderViewState())

    val state: StateFlow<ReminderViewState>
        get() = _state

    suspend fun saveReminder(reminder: Reminder): Long {
        return reminderRepository.addReminder(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        return reminderRepository.deleteReminder(reminder)
    }

    suspend fun updateReminder(reminder:Reminder){
        Log.i("log",reminder.reminderMessage)
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

data class ReminderViewState(
    val categories: List<Category> = emptyList()
)