package com.arturoproject.triviaapp.loginui

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.arturoproject.triviaapp.data.SettingsRepository



class LoginViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    private val _email = MutableLiveData<String>("")
    val email: LiveData<String> = _email

    private val _loginEnable = MutableLiveData<Boolean>(false)
    val loginEnable: LiveData<Boolean> = _loginEnable

    fun onLoginChanged(email: String) {
        _email.value = email
        _loginEnable.value = isValidEmail(email)
    }

    suspend fun onLoginSelected() {
        delay(1000)
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isNotBlank()
    }
    val isDarkMode: Flow<Boolean> = settingsRepository.isDarkMode
    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(enabled)
        }
    }


}
