package com.minimalist.phone.services.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast

class DetoxAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // This is a simple proof of concept for V1.
        // It detects when a window state changes (e.g. an app opens)
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString()

            // In a real app, this would query a database of "distracting" apps
            // Here, we hardcode an example like YouTube for demonstration
            if (packageName == "com.google.android.youtube") {
                Toast.makeText(this, "Take a breath. Is this intentional?", Toast.LENGTH_LONG).show()
                // The actual logic would launch an overlay Activity here
                // to show the reflection screen and countdown timer.
            }
        }
    }

    override fun onInterrupt() {
        // Required method, called when the system wants to interrupt the feedback
    }
}
