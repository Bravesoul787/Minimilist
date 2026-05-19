package com.minimalist.phone.features.focus

import com.minimalist.phone.data.local.dao.FocusSessionDao
import com.minimalist.phone.data.local.entities.FocusSessionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FocusRepository(private val focusSessionDao: FocusSessionDao) {
    fun getAllSessions(): Flow<List<FocusSessionEntity>> = focusSessionDao.getAllSessions()

    suspend fun insertSession(session: FocusSessionEntity) = withContext(Dispatchers.IO) {
        focusSessionDao.insertSession(session)
    }
}
