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

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.oi.hata.ui.theme.*



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



@Composable
fun HataTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = hatadarkcolors, typography = hataTypography, shapes = HataShapes) {
        content()
    }
}

val HataShapes = Shapes(
    small = RoundedCornerShape(percent = 50),
    medium = RoundedCornerShape(size = 16.dp),
    large = RoundedCornerShape(topStart = 8.dp,topEnd = 8.dp,bottomStart = 8.dp,bottomEnd = 8.dp),
)
