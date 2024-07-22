package com.kionavani.todotask.ui.viewmodels

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

    fun changeTheme(themeState: ThemeState) {
        viewModelScope.launch {
            _selectedThemeState.value = themeState
        }
    }
}