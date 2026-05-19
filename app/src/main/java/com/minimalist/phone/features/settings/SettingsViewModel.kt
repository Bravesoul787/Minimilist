package com.minimalist.phone.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    val currentTheme: StateFlow<String> = settingsRepository.getThemeSetting()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "nothing"
        )

    fun setTheme(theme: String) {
        viewModelScope.launch {
            settingsRepository.updateThemeSetting(theme)
        }
    }
}
