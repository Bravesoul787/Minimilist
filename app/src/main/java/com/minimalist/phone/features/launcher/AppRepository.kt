package com.minimalist.phone.features.launcher

import android.content.Context
import android.content.Intent
import com.minimalist.phone.data.local.dao.AppDao
import com.minimalist.phone.data.local.entities.AppEntity
import com.minimalist.phone.domain.models.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class AppRepository(
    private val context: Context,
    private val appDao: AppDao
) {

    suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val apps = packageManager.queryIntentActivities(intent, 0)

        val currentInstalledApps = apps.mapNotNull { resolveInfo ->
            val packageName = resolveInfo.activityInfo.packageName
            // Skip the launcher itself
            if (packageName == context.packageName) return@mapNotNull null

            val name = resolveInfo.loadLabel(packageManager).toString()
            AppInfo(name = name, packageName = packageName)
        }.sortedBy { it.name.lowercase() }

        // Sync with local database
        val existingAppsFlow = appDao.getAllApps()
        val existingApps = existingAppsFlow.firstOrNull() ?: emptyList()

        val currentPackages = currentInstalledApps.map { it.packageName }.toSet()
        val existingPackages = existingApps.map { it.packageName }.toSet()

        val newAppsToInsert = currentInstalledApps.filter { !existingPackages.contains(it.packageName) }

        if (newAppsToInsert.isNotEmpty()) {
            val entities = newAppsToInsert.map {
                AppEntity(id = it.packageName, packageName = it.packageName, appName = it.name)
            }
            appDao.insertApps(entities)
        }

        currentInstalledApps
    }
}
