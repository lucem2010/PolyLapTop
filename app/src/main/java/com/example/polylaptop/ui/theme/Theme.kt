package com.example.polylaptop.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import viewmodel.UserViewModel


@Composable
fun PolyLapTopTheme(
    viewModel: UserViewModel,
    content: @Composable () -> Unit
) {
    val isDarkTheme by viewModel.isDarkTheme.observeAsState(false)
    val colors = if (isDarkTheme) {
        darkColors(
            primary = Color(0xFFBB86FC),
            secondary = Color(0xFF03DAC6),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            onPrimary = Color.Black,
            onSecondary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        )
    } else {
        lightColors(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC6),
            background = Color(0xFFFFFFFF),
            surface = Color(0xFFFAFAFA),
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.Black,
            onSurface = Color.Black
        )
    }

    MaterialTheme(
        colors = colors, // Sử dụng colors thay vì colorScheme
        typography = Typography, // Tùy chỉnh Typography nếu cần
        shapes = Shapes, // Định nghĩa Shapes nếu cần
        content = content
    )

}