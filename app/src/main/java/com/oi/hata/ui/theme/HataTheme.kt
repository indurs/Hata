/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.oi.hata.ui.theme.*
import hataColors

val crane_caption = Color.DarkGray
val crane_divider_color = Color.LightGray
private val hata_yello = Color(0xFFf8e767)
private val hata_white = Color.White
private val hata_yello_600 = Color(0xFFffffff)
private val hata_yello_700 = Color(0xFF505252)
private val hata_yello_800 = Color(0xFFfcfbf4)

private val blue_800 = Color(0xFF166ed8)
private val blue_700 = Color(0xFF1680ea)


//Month colors

val jan = Color(0xFF024eee)
val feb = Color(0xFFc6129f)
val mar = Color(0xFF008744)
val apr = Color(0xFF875200)
val may = Color(0xFF003087)
val jun = Color(0xFF873600)
val jul = Color(0xFF8f6a1a)
val aug = Color(0xFF0b6545)
val sep = Color(0xFF1363c9)
val oct = Color(0xFF7911aa)
val nov = Color(0xFF447019)
val dec = Color(0xFF194470)


val hataColors = darkColors(
    primary = hata_yello_700,
    onPrimary = Color.Black,
    secondary = hata_yello,
    surface = hata_yello_800,
    onSurface = hata_yello,
    primaryVariant = hata_yello_600
)

val calendarColors = lightColors(
    primary = green200,
    primaryVariant = green100,
    surface = Color.Black,

    secondary = purple500,
    onSurface = green50
)

@Composable
fun HataTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = hatascreencolors, typography = hataTypography, shapes = HataShapes) {
        content()
    }
}

@Composable
fun HataCalendarTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = calendarColors, typography = hataTypography, shapes = HataShapes) {
        content()
    }
}

val HataShapes = Shapes(
    small = RoundedCornerShape(percent = 50),
    medium = RoundedCornerShape(size = 16.dp),
    large = RoundedCornerShape(topStart = 8.dp,topEnd = 8.dp,bottomStart = 8.dp,bottomEnd = 8.dp)
)
