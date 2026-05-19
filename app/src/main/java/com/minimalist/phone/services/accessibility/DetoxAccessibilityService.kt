package com.minimalist.phone.services.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import com.minimalist.phone.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import android.content.Intent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import com.minimalist.phone.features.detox.DetoxOverlayActivity

class DetoxAccessibilityService : AccessibilityService() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var appDatabase: AppDatabase
    private var lastInterceptedPackage: String? = null
    private var lastInterceptTime: Long = 0

    override fun onCreate() {
        super.onCreate()
        appDatabase = AppDatabase.getDatabase(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // It detects when a window state changes (e.g. an app opens)
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return

            // Avoid looping if the overlay itself triggers the event, or if we just intercepted it
            if (packageName == this.packageName) return

            val currentTime = System.currentTimeMillis()
            if (packageName == lastInterceptedPackage && (currentTime - lastInterceptTime) < 10000) {
                // If we intercepted this package less than 10 seconds ago, let it pass (user clicked 'Continue')
                return
            }

            // Query database to see if the app is marked as distracting
            scope.launch {
                val apps = appDatabase.appDao().getAllApps().firstOrNull() ?: emptyList()
                val isDistracting = apps.find { it.packageName == packageName }?.isDistracting == true

                if (isDistracting || packageName == "com.google.android.youtube") {
                    lastInterceptedPackage = packageName
                    lastInterceptTime = currentTime

                    launch(Dispatchers.Main) {
                        val overlayIntent = Intent(this@DetoxAccessibilityService, DetoxOverlayActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                        startActivity(overlayIntent)
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        // Required method, called when the system wants to interrupt the feedback
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
