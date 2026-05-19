package com.minimalist.phone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.minimalist.phone.data.local.entities.UsageLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageLogDao {
    @Query("SELECT * FROM usage_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<UsageLogEntity>>

    @Insert
    suspend fun insertLog(log: UsageLogEntity)
}
