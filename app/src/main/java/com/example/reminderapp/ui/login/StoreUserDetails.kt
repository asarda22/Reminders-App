package com.example.reminderapp.ui.login

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//This file is fo handling datastore for login

class StoreUserDetails(private val context: Context) {
    companion object {

        private val Context.dataStoree: DataStore<Preferences> by preferencesDataStore("userDetails")
        val USER_NAME_KEY = stringPreferencesKey("username")
        val PASSWORD_KEY = stringPreferencesKey("password")
        val ISLOGGEDIN_KEY = stringPreferencesKey("ifLoggedIn")
    }

    // get the saved username
    val getUserName: Flow<String> = context.dataStoree.data
        .map {
                preferences ->
            preferences[USER_NAME_KEY] ?: "myuser"
        }

    val getPassword: Flow<String> = context.dataStoree.data
        .map{
                preferences ->
            preferences[PASSWORD_KEY] ?: "mypassword"
        }

    val getIsLoggedIn: Flow<Any> = context.dataStoree.data
        .map{
                preferences ->
            preferences[ISLOGGEDIN_KEY] ?: "false"
        }

    //save username to datastore
    suspend fun saveUserName(name : String){
        context.dataStoree.edit {
                preferences ->
            preferences[USER_NAME_KEY] = name
        }

    }

    suspend fun savePassword(pwd : String) {
        context.dataStoree.edit { preferences ->
            preferences[PASSWORD_KEY] = pwd
        }
    }

    suspend fun saveIsLoggedIn(isLoggedIn : String) {
        context.dataStoree.edit { preferences ->
            preferences[ISLOGGEDIN_KEY] = isLoggedIn
        }
    }

}