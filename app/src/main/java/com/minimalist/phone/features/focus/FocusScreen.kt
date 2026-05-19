package com.minimalist.phone.features.focus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun FocusScreen(viewModel: FocusViewModel, modifier: Modifier = Modifier) {
    val totalTime = 25 * 60
    var timeLeft by remember { mutableStateOf(totalTime) } // 25 minutes in seconds
    var isRunning by remember { mutableStateOf(false) }
    var sessionStartTime by remember { mutableStateOf(0L) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            if (sessionStartTime == 0L) {
                sessionStartTime = System.currentTimeMillis()
            }
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            isRunning = false

            // Session completed natively
            if (timeLeft == 0) {
                viewModel.saveSession(sessionStartTime, totalTime.toLong(), true)
                sessionStartTime = 0L
                timeLeft = totalTime // Reset for next session
            }
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val timeString = String.format("%02d:%02d", minutes, seconds)
    val progress = timeLeft.toFloat() / totalTime.toFloat()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Focus",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(240.dp),
                    color = if (isRunning) com.minimalist.phone.core.theme.NothingRed else MaterialTheme.colorScheme.onBackground,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeWidth = 4.dp
                )
                Text(
                    text = timeString,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = {
                    if (isRunning) {
                        // User paused/cancelled the session
                        val durationInSeconds = totalTime - timeLeft
                        viewModel.saveSession(sessionStartTime, durationInSeconds.toLong(), false)

                        // Reset session
                        sessionStartTime = 0L
                        timeLeft = totalTime
                    }
                    isRunning = !isRunning
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) com.minimalist.phone.core.theme.NothingRed else MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(if (isRunning) "Stop" else "Start Session")
            }
        }
    }
}
