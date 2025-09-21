package com.example.cdhomes.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
  primary = Purple40,
  onPrimary = White,
  primaryContainer = Purple80,
  onPrimaryContainer = Black,
  secondary = PurpleGrey40,
  onSecondary = White,
  secondaryContainer = Purple80,
  onSecondaryContainer = Black,
  tertiary = Pink40,
  onTertiary = White,
  background = LightGray,
  onBackground = Black,
  surface = White,
  onSurface = Black,
  surfaceVariant = Purple80,
  onSurfaceVariant = Black,
)

private val DarkColors = darkColorScheme(
  primary = Purple80,
  onPrimary = Black,
  primaryContainer = Purple40,
  onPrimaryContainer = White,
  secondary = PurpleGrey40,
  onSecondary = Black,
  secondaryContainer = Purple40,
  onSecondaryContainer = White,
  tertiary = Pink40,
  onTertiary = Black,
  background = Black,
  onBackground = White,
  surface = Black,
  onSurface = White,
  surfaceVariant = Purple40,
  onSurfaceVariant = White,
)

@Composable
fun CDHomesTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colors = if (darkTheme) DarkColors else LightColors

  MaterialTheme(
    colorScheme = colors,
    typography = CDHomesTypography,
    shapes = CDHomesShapes,
    content = content
  )
}
