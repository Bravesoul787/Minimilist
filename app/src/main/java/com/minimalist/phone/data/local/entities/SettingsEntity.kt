package com.minimalist.phone.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "theme") val theme: String = "dark",
    @ColumnInfo(name = "font_scale") val fontScale: Float = 1.0f,
    @ColumnInfo(name = "focus_mode") val focusMode: String = "strict",
    @ColumnInfo(name = "notification_mode") val notificationMode: String = "minimal"
)
