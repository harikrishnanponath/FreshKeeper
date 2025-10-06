package com.example.freshkeeper.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController


//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)

// 🌞 Light Theme
private val FreshKeeperLightColorScheme = lightColorScheme(
    primary = Color(0xFF388E3C),
    onPrimary = Color.White,
    secondary = Color(0xFFFFB300),
    onSecondary = Color.Black,
    tertiary = Color(0xFF81C784),
    onTertiary = Color.White,
    background = Color(0xFFF9FBF9),
    onBackground = Color(0xFF1B1B1B),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1E1E1E),
    error = Color(0xFFD32F2F),
    onError = Color.White
)

// 🌙 Dark Theme
private val FreshKeeperDarkColorScheme = darkColorScheme(
    primary = Color(0xFF388E3C),
    onPrimary = Color.Black,
    secondary = Color(0xFFFFCA28),
    onSecondary = Color.Black,
    tertiary = Color(0xFF388E3C),
    onTertiary = Color.White,
    background = Color(0xFF101312),
    onBackground = Color(0xFFE8F5E9),
    surface = Color(0xFF1B1F1D),
    onSurface = Color(0xFFE8F5E9),
    error = Color(0xFFEF5350),
    onError = Color.Black
)

@Composable
fun FreshKeeperTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> FreshKeeperDarkColorScheme
        else -> FreshKeeperLightColorScheme
    }

    // Update system bars (status/navigation)
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = colorScheme.primary,
            darkIcons = !darkTheme
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}