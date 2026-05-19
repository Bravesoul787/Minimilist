package com.minimalist.phone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.minimalist.phone.data.local.entities.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY start_time DESC")
    fun getAllSessions(): Flow<List<FocusSessionEntity>>

    @Insert
    suspend fun insertSession(session: FocusSessionEntity)
}
