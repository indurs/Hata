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

val jan = Color(0xFF4e7ffc)
val feb = Color(0xFFfe8cef)
val mar = Color(0xFF7fff82)
val apr = Color(0xFFe4f81b)
val may = Color(0xFFff96b1)
val jun = Color(0xFF4efcea)
val jul = Color(0xFF9fa3fd)
val aug = Color(0xFF4ef6fc)
val sep = Color(0xFFf6fc4e)
val oct = Color(0xFFe2c2fd)
val nov = Color(0xFFfac19c)
val dec = Color(0xFFe7f5ec)



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
val colors = darkColors(
    primary = Green500,
    surface = DarkBlue900,
    onSurface = Color.White,
    background = DarkBlue900,
    onBackground = Color.White
)

@Composable
fun HataTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = hatascreencolors, typography = hataTypography, shapes = HataShapes) {
        content()
    }
}

@Composable
fun HataCalendarTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = colors, typography = hataTypography, shapes = HataShapes) {
        content()
    }
}

val HataShapes = Shapes(
    small = RoundedCornerShape(percent = 50),
    medium = RoundedCornerShape(size = 16.dp),
    large = RoundedCornerShape(topStart = 8.dp,topEnd = 8.dp,bottomStart = 8.dp,bottomEnd = 8.dp),
)
