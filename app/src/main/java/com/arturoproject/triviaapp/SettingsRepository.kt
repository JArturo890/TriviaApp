package com.arturoproject.triviaapp.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Keeping dataStore to remember if the dark is enable even if you close it.
private val Context.dataStore by preferencesDataStore("settings")

class SettingsRepository(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    // Check if it is enable
    val isDarkMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY] ?: false
    }

    // SAVE DARK
    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }
}
