package com.minimalist.phone.features.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minimalist.phone.data.local.entities.FocusSessionEntity
import kotlinx.coroutines.launch

class FocusViewModel(private val focusRepository: FocusRepository) : ViewModel() {

    fun saveSession(startTime: Long, duration: Long, completed: Boolean) {
        viewModelScope.launch {
            val session = FocusSessionEntity(
                startTime = startTime,
                endTime = startTime + duration * 1000,
                duration = duration,
                completed = completed
            )
            focusRepository.insertSession(session)
        }
    }
}
