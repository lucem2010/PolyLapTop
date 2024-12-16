package com.example.polylaptop.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import viewmodel.UserViewModel

@Composable
fun PolyLapTopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    viewModel: UserViewModel,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val colors = if (isDarkTheme) {
        darkColors(
            primary = Color(0xFFBB86FC),
            secondary = Color(0xFF03DAC6),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color.White,
            onSurface = Color.White
        )
    } else {
        lightColors(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC6),
            background = Color(0xFFFFFFFF),
            surface = Color(0xFFFAFAFA),
            onPrimary = Color.Black,
            onSecondary = Color.Black,
            onBackground = Color.Black,
            onSurface = Color.Black
        )
    }

    // Retrieve the current view
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            setUpEdgeToEdge(view, isDarkTheme)
        }
    }

    // Apply MaterialTheme with colors
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

private fun setUpEdgeToEdge(view: View, darkTheme: Boolean) {
    val window = (view.context as Activity).window
    WindowCompat.setDecorFitsSystemWindows(window, false)

    // Set transparent status bar and navigation bar colors
    window.statusBarColor = Color.Transparent.toArgb()
    window.navigationBarColor = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> Color.Transparent.toArgb()
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> Color(0xFF, 0xFF, 0xFF, 0x63).toArgb()
        else -> Color(0x00, 0x00, 0x00, 0x50).toArgb()
    }

    // Adjust status bar and navigation bar icon color based on theme
    val controller = WindowCompat.getInsetsController(window, view)
    controller?.let {
        it.isAppearanceLightStatusBars = !darkTheme // Light icons for dark background
        it.isAppearanceLightNavigationBars = !darkTheme // Light icons for dark background
    }
}