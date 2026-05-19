package com.minimalist.phone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minimalist.phone.core.theme.MinimalistPhoneTheme
import com.minimalist.phone.data.local.AppDatabase
import com.minimalist.phone.features.launcher.AppRepository
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.minimalist.phone.features.focus.FocusRepository
import com.minimalist.phone.features.focus.FocusScreen
import com.minimalist.phone.features.focus.FocusViewModel
import com.minimalist.phone.features.launcher.HomeScreen
import com.minimalist.phone.features.launcher.LauncherViewModel
import androidx.core.view.WindowCompat
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.minimalist.phone.features.lockscreen.LockScreenActivity

class MainActivity : ComponentActivity() {
    private lateinit var screenOffReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Register receiver for screen off to show lock screen
        screenOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_SCREEN_OFF) {
                    val lockIntent = Intent(this@MainActivity, LockScreenActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    startActivity(lockIntent)
                }
            }
        }
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        registerReceiver(screenOffReceiver, filter)

        val appDatabase = AppDatabase.getDatabase(applicationContext)
        val appRepository = AppRepository(applicationContext, appDatabase.appDao())
        val focusRepository = FocusRepository(appDatabase.focusSessionDao())

        setContent {
            MinimalistPhoneTheme {
                val navController = rememberNavController()
                val launcherViewModel: LauncherViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return LauncherViewModel(appRepository) as T
                        }
                    }
                )
                val focusViewModel: FocusViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return FocusViewModel(focusRepository) as T
                        }
                    }
                )

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            viewModel = launcherViewModel,
                            onNavigateToFocus = { navController.navigate("focus") }
                        )
                    }
                    composable("focus") {
                        FocusScreen(viewModel = focusViewModel)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenOffReceiver)
    }
}
