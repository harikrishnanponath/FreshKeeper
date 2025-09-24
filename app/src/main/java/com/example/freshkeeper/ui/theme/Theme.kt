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

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black,

    secondary = Color(0xFF03DAC6),  // Teal for secondary highlights
    onSecondary = Color.Black,

    background = Color.Black,       // Always black background
    onBackground = Color.White,

    surface = Color(0xFF121212),    // Dark gray for cards/surfaces
    onSurface = Color.White,

    error = Color(0xFFCF6679),
    onError = Color.Black
)


private val FreshKeeperLightColorScheme = lightColorScheme(
    primary = Color(0xFF00897B),   // Deep teal accent
    onPrimary = Color.White,
    secondary = Color(0xFF26C6DA), // Bright cyan
    onSecondary = Color.White,
    tertiary = Color(0xFF4DB6AC),  // Muted teal
    onTertiary = Color.White,
    background = Color(0xFFFDFDFD),
    onBackground = Color(0xFF121212),
    surface = Color(0xFFFFFFFF),   // Card background
    onSurface = Color(0xFF121212),
)

@Composable
fun FreshKeeperTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = false // false = white icons, true = dark icons

    SideEffect {
        systemUiController.setStatusBarColor(Color.Black, darkIcons = false)

    }
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> FreshKeeperLightColorScheme
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}