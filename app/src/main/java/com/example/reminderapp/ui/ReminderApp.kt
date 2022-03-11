package com.example.reminderapp.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.reminderapp.ReminderAppState
import com.example.reminderapp.data.entity.Category
import com.example.reminderapp.data.entity.Reminder
import com.example.reminderapp.rememberReminderAppState
import com.example.reminderapp.ui.home.Home
import com.example.reminderapp.ui.home.categoryReminder.EditCategoryReminder
import com.example.reminderapp.ui.login.Login
import com.example.reminderapp.ui.reminder.Reminder

@Composable
fun ReminderApp(
    appState: ReminderAppState = rememberReminderAppState()
) {
    NavHost(
        navController = appState.navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            Login(navController = appState.navController)
        }
        composable(route = "home") {
            Home(navController = appState.navController)
        }
        composable(route = "reminder") {
            Reminder(onBackPress = appState::navigateBack, navController = appState.navController)
        }
        composable(route = "edit") {

            val reminder =
                appState.navController.previousBackStackEntry?.savedStateHandle?.get<Reminder>("reminder")

            val category =
                appState.navController.previousBackStackEntry?.savedStateHandle?.get<Category>("category")

            if (reminder != null && category != null)
            {
                EditCategoryReminder(navController = appState.navController, reminder, category)
            }
        }
    }
}

