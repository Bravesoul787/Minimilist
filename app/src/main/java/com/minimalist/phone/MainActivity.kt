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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
}
