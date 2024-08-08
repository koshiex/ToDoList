package com.kionavani.todotask.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kionavani.todotask.ui.ThemeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor() : ViewModel() {
    private val _selectedThemeState: MutableStateFlow<ThemeState> = MutableStateFlow(ThemeState.SYSTEM)
    val selectedThemeState: StateFlow<ThemeState> = _selectedThemeState.asStateFlow()

    fun changeThemeDropDown(themeState: ThemeState) {
        viewModelScope.launch {
            _selectedThemeState.value = themeState
        }
    }

    fun changeThemeBoolean(themeIsDark: Boolean?) {
        val newThemeState = when {
            themeIsDark == null -> ThemeState.SYSTEM
            themeIsDark -> ThemeState.DARK
            else -> ThemeState.LIGHT
        }

        if (_selectedThemeState.value != newThemeState) {
            _selectedThemeState.value = newThemeState
        }
    }
}