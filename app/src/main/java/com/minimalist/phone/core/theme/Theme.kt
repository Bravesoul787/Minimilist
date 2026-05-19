package com.minimalist.phone.core.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DefaultDarkColorScheme = darkColorScheme(
    primary = DefaultDarkText,
    secondary = DefaultDarkSecondary,
    tertiary = DefaultDarkSecondary,
    background = DefaultDarkBackground,
    surface = DefaultDarkBackground,
    onPrimary = DefaultDarkBackground,
    onSecondary = DefaultDarkBackground,
    onTertiary = DefaultDarkBackground,
    onBackground = DefaultDarkText,
    onSurface = DefaultDarkText,
    surfaceVariant = DefaultDarkDivider
)

private val DefaultLightColorScheme = lightColorScheme(
    primary = DefaultLightText,
    secondary = DefaultLightSecondary,
    tertiary = DefaultLightSecondary,
    background = DefaultLightBackground,
    surface = DefaultLightBackground,
    onPrimary = DefaultLightBackground,
    onSecondary = DefaultLightBackground,
    onTertiary = DefaultLightBackground,
    onBackground = DefaultLightText,
    onSurface = DefaultLightText,
    surfaceVariant = DefaultLightDivider
)

private val NothingDarkColorScheme = darkColorScheme(
    primary = NothingDarkText,
    secondary = NothingDarkSecondary,
    tertiary = NothingDarkSecondary,
    background = NothingDarkBackground,
    surface = NothingDarkBackground,
    onPrimary = NothingDarkBackground,
    onSecondary = NothingDarkBackground,
    onTertiary = NothingDarkBackground,
    onBackground = NothingDarkText,
    onSurface = NothingDarkText,
    surfaceVariant = NothingDarkDivider
)

private val NothingLightColorScheme = lightColorScheme(
    primary = NothingLightText,
    secondary = NothingLightSecondary,
    tertiary = NothingLightSecondary,
    background = NothingLightBackground,
    surface = NothingLightBackground,
    onPrimary = NothingLightBackground,
    onSecondary = NothingLightBackground,
    onTertiary = NothingLightBackground,
    onBackground = NothingLightText,
    onSurface = NothingLightText,
    surfaceVariant = NothingLightDivider
)

@Composable
fun MinimalistPhoneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    themeStyle: String = "nothing",
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        themeStyle == "nothing" -> if (darkTheme) NothingDarkColorScheme else NothingLightColorScheme
        else -> if (darkTheme) DefaultDarkColorScheme else DefaultLightColorScheme
    }

    val typography = if (themeStyle == "nothing") NothingTypography else DefaultTypography

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()

            val windowInsetsController = WindowCompat.getInsetsController(window, view)
            windowInsetsController.isAppearanceLightStatusBars = !darkTheme
            windowInsetsController.isAppearanceLightNavigationBars = !darkTheme
            windowInsetsController.systemBarsBehavior = androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            windowInsetsController.hide(androidx.core.view.WindowInsetsCompat.Type.systemBars())
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
