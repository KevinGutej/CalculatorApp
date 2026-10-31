package com.example.calculatorapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkPalette = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    secondaryContainer = DarkFuncContainer,
    onSecondaryContainer = DarkOnFuncContainer,
    tertiaryContainer = DarkSciContainer,
    onTertiaryContainer = DarkOnSciContainer,
    surfaceVariant = DarkNumContainer,
    onSurfaceVariant = DarkOnNumContainer,
    background = DarkScreenBackground
)

private val LightPalette = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    secondaryContainer = LightFuncContainer,
    onSecondaryContainer = LightOnFuncContainer,
    tertiaryContainer = LightSciContainer,
    onTertiaryContainer = LightOnSciContainer,
    surfaceVariant = LightNumContainer,
    onSurfaceVariant = LightOnNumContainer,
    background = LightScreenBackground
)

@Composable
fun CustomCalcTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColors: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = when {
        useDynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        useDarkTheme -> DarkPalette
        else -> LightPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}