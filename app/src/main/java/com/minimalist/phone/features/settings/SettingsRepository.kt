package com.minimalist.phone.features.settings

import com.minimalist.phone.data.local.dao.SettingsDao
import com.minimalist.phone.data.local.entities.SettingsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SettingsRepository(private val settingsDao: SettingsDao) {

    fun getThemeSetting(): Flow<String> {
        return settingsDao.getSettings().map { it?.theme ?: "nothing" } // Default to 'nothing' style
    }

    suspend fun updateThemeSetting(theme: String) = withContext(Dispatchers.IO) {
        val settings = settingsDao.getSettings().firstOrNull() ?: SettingsEntity()
        settingsDao.updateSettings(settings.copy(theme = theme))
    }
}
