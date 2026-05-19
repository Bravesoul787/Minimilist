package com.minimalist.phone.features.launcher

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.minimalist.phone.domain.models.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {

    suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val apps = packageManager.queryIntentActivities(intent, 0)

        apps.mapNotNull { resolveInfo ->
            val packageName = resolveInfo.activityInfo.packageName
            // Skip the launcher itself
            if (packageName == context.packageName) return@mapNotNull null

            val name = resolveInfo.loadLabel(packageManager).toString()
            AppInfo(name = name, packageName = packageName)
        }.sortedBy { it.name.lowercase() }
    }
}
