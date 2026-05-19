package com.minimalist.phone.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.minimalist.phone.data.local.dao.AppDao
import com.minimalist.phone.data.local.dao.FocusSessionDao
import com.minimalist.phone.data.local.dao.SettingsDao
import com.minimalist.phone.data.local.dao.UsageLogDao
import com.minimalist.phone.data.local.entities.AppEntity
import com.minimalist.phone.data.local.entities.FocusSessionEntity
import com.minimalist.phone.data.local.entities.SettingsEntity
import com.minimalist.phone.data.local.entities.UsageLogEntity

@Database(
    entities = [
        AppEntity::class,
        FocusSessionEntity::class,
        UsageLogEntity::class,
        SettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
    abstract fun focusSessionDao(): FocusSessionDao
    abstract fun usageLogDao(): UsageLogDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "minimalist_phone_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
