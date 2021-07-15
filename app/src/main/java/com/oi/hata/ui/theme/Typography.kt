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

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.oi.hata.R

private val light = Font(R.font.montserrat_light,FontWeight.W300)
private val regular = Font(R.font.montserrat_regular, FontWeight.W400)
private val medium = Font(R.font.montserrat_medium, FontWeight.W500)
private val semibold = Font(R.font.montserrat_semibold, FontWeight.W600)

private val Montserrat = FontFamily(
    Font(R.font.montserrat_light, FontWeight.Light),
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_semibold, FontWeight.SemiBold)
)

private val RobotoCondensed = FontFamily(
    Font(R.font.robotocondensed_regular),
    Font(R.font.robotocondensed_light, FontWeight.Light),
    Font(R.font.robotocondensed_bold, FontWeight.Bold)
)

private val hatafontFamily = FontFamily(fonts = listOf(light, regular, medium, semibold))

val captionTextStyle = TextStyle(
    fontFamily = RobotoCondensed,
    fontWeight = FontWeight.W400,
    fontSize = 16.sp
)

val hataTypography = Typography(
    h1 = TextStyle(
        fontFamily = hatafontFamily,
        fontWeight = FontWeight.W300,
        fontSize = 96.sp
    ),
    h2 = TextStyle(
        fontFamily = hatafontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 60.sp
    ),
    h3 = TextStyle(
        fontFamily = hatafontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 48.sp
    ),
    h4 = TextStyle(
        fontFamily = hatafontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 34.sp
    ),
    h5 = TextStyle(
        fontFamily = hatafontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        fontFamily = hatafontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 20.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = hatafontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = hatafontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = hatafontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = Montserrat,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    button = TextStyle(
        fontFamily = hatafontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = hatafontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        fontFamily = hatafontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp
    )
)
