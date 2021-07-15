package com.oi.hata.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

val purple200 = Color(0xFFBB86FC)
val purple500 = Color(0xFF6200EE)
val purple700 = Color(0xFF3700B3)
val teal200 = Color(0xFF03DAC5)

//Calendar

val green200 = Color(0xFF7dcbc9)
val green100 = Color(0xFFb1dfde)
val green50 = Color(0xFFe0f2f2)


@Composable
fun Colors.compositedOnSurface(alpha: Float): Color {
    return onSurface.copy(alpha = alpha).compositeOver(surface)
}

val Yellow800 = Color(0xFFF29F05)
val Red300 = Color(0xFFEA6D7E)

val JetcasterColors = darkColors(
    primary = Yellow800,
    onPrimary = Color.Black,
    primaryVariant = Yellow800,
    secondary = Yellow800,
    onSecondary = Color.Black,
    error = Red300,
    onError = Color.Black
)



val Green500 = Color(0xFF1EB980)

val DarkBlue900 = Color(0xFF26282F) // TODO: Confirm literal name

val DarkSurface = Color(0xff242316)

val hatascreencolors = darkColors(
    primary = Green500,
    primaryVariant = Yellow800,
    secondary = Yellow800,
    surface = DarkSurface,
    onSurface = Yellow800,
    background = DarkBlue900,
    onBackground = Yellow800
)
