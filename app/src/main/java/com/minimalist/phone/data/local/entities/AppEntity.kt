package com.minimalist.phone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apps")
data class AppEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "package_name") val packageName: String,
    @ColumnInfo(name = "app_name") val appName: String,
    @ColumnInfo(name = "is_hidden") val isHidden: Boolean = false,
    @ColumnInfo(name = "is_distracting") val isDistracting: Boolean = false,
    @ColumnInfo(name = "usage_time") val usageTime: Long = 0,
    @ColumnInfo(name = "launch_count") val launchCount: Int = 0
)
