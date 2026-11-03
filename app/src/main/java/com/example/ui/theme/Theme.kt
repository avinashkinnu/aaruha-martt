package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = AaruhaGreenLight,
  onPrimary = Color.Black,
  primaryContainer = AaruhaGreenDark,
  onPrimaryContainer = Color.White,
  secondary = AaruhaYellowAccent,
  onSecondary = Color.Black,
  secondaryContainer = Color(0xFF422006),
  onSecondaryContainer = AaruhaYellowBright,
  background = SurfaceDark,
  onBackground = TextPrimaryDark,
  surface = SurfaceDark,
  onSurface = TextPrimaryDark,
  surfaceVariant = SurfaceCardDark,
  onSurfaceVariant = TextSecondaryDark,
  outline = BorderSubtleDark
)

private val LightColorScheme = lightColorScheme(
  primary = AaruhaGreenPrimary,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFDCFCE7),
  onPrimaryContainer = AaruhaGreenDark,
  secondary = AaruhaYellowAccent,
  onSecondary = Color.Black,
  secondaryContainer = AaruhaYellowContainer,
  onSecondaryContainer = Color(0xFF78350F),
  background = SurfaceLight,
  onBackground = TextPrimaryLight,
  surface = SurfaceCardLight,
  onSurface = TextPrimaryLight,
  surfaceVariant = Color(0xFFF3F4F6),
  onSurfaceVariant = TextSecondaryLight,
  outline = BorderSubtleLight
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep branded emerald & yellow for Aaruha brand consistency
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

