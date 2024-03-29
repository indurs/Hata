package com.oi.hata.common.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.oi.hata.R
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.ui.theme.ButtonCircleShape

@ExperimentalMaterialApi
@Composable
fun CircleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = ButtonCircleShape,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable () -> Unit

    ){
        Surface(modifier = modifier
            .clip(shape)
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null
            ),
            onClick = {
                onClick()
            },
            enabled = enabled,
            role = Role.Button,
            interactionSource = interactionSource,
            indication = null,
            shape = shape,
            color = Color.White.copy(alpha = 0.50f).compositeOver(Color.Black),
            content = content
        )

}

@Composable
fun HataTaskSheetIconButton(
    onClick: () -> Unit,
    painter: Painter,
    contentDescription: String?,
){
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .statusBarsPadding()
            .height(24.dp)

    ) {
        Icon(
            painter = painter,
            tint = Color.White.copy(alpha = 0.80f).compositeOver(Color.Black),
            contentDescription = contentDescription

        )
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun HataTaskReminderOptionButton(
    color: Color,
    onReminderOptSelected: (String) -> Unit,
    reminderOptSelected: String,
    title:String,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },

){
    Surface(
        shape = MaterialTheme.shapes.small,
        color = color,
        onClick = { onReminderOptSelected(title) },
        role = Role.Button,
        interactionSource = interactionSource,
        indication = null,
        modifier = Modifier
            .padding(8.dp)
            ) {
        Row(horizontalArrangement = Arrangement.Center) {
            AnimatedVisibility(
                visible = reminderOptSelected.equals(title,true)
                ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 8.dp, bottom = 8.dp)
                        .size(12.dp),
                    color = color.copy(alpha = 0.20f).compositeOver(Color.White),
                    elevation = 2.dp
                ) {

                }
            }
            Text(
                text = title,
                color = Color.Black,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(top = 8.dp,end = 12.dp,start = 12.dp,bottom = 8.dp))
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun HataTaskReminderCustomButton(
    color: Color,
    onCustomReminderSelect: () -> Unit,
    reminderOptSelected: String,
    title:String,
    onCustomReminderInit: () -> Unit
    ){
    Surface(
        shape = MaterialTheme.shapes.small,
        color = color,
        onClick = {
            onCustomReminderInit()
            onCustomReminderSelect()
        },
        modifier = Modifier
            .padding(8.dp)
             ) {
        Row(horizontalArrangement = Arrangement.Center) {
            AnimatedVisibility(
                visible = reminderOptSelected.equals(title,true)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 8.dp, bottom = 8.dp)
                        .size(12.dp),
                    color = color.copy(alpha = 0.20f).compositeOver(Color.White),
                    elevation = 2.dp
                ) {

                }
            }
            Text(
                text = title,
                color = Color.Black,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(top = 8.dp,end = 12.dp,start = 12.dp,bottom = 8.dp))
        }
    }
}



@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun HataTaskReminderPickADate(
    color: Color,
    onReminderOptSelected: (String) -> Unit,
    reminderOptSelected: String,
    title:String,
    onPickaDate: (year: Int,month: Int, day: Int) -> Unit,
    ){
    Surface(
        shape = MaterialTheme.shapes.small,
        color = color,
        onClick = { onReminderOptSelected(title) },
        modifier = Modifier
            .padding(8.dp)
            ) {
        Row(horizontalArrangement = Arrangement.Center) {
            AnimatedVisibility(
                visible = reminderOptSelected.equals(title,true)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 8.dp, bottom = 8.dp)
                        .size(12.dp),
                    color = color.copy(alpha = 0.20f).compositeOver(Color.White),
                    elevation = 2.dp
                ) {

                }
            }
            Text(
                text = title,
                color = Color.Black,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(top = 8.dp,end = 12.dp,start = 12.dp,bottom = 8.dp))
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun HataTimeButton(
    timeSelected: Boolean,
    onTimeSelected: (Boolean) -> Unit,
    reminderTime: String
){
    Surface(
        shape = MaterialTheme.shapes.small,
        color = Color.Black.copy(alpha = 0.70f).compositeOver(Color.White),
        modifier = Modifier.padding(top = 16.dp,start=8.dp,bottom=16.dp),
        onClick = { if(timeSelected)
            onTimeSelected(false)
        else
            onTimeSelected(true) }
    )
    {
        Row(
            modifier = Modifier.padding(start = 8.dp,end = 8.dp,)) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_schedule_24),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(16.dp)
            )
            Text(text = reminderTime,
                modifier = Modifier.padding(4.dp),
                color = Color.White,style = MaterialTheme.typography.caption)
        }
    }
}

@Composable
fun Dot(
    color:Color,
    modifier: Modifier
){
    Surface(
        shape = MaterialTheme.shapes.small,
        modifier = modifier,
        color = color.copy(alpha = 0.20f).compositeOver(Color.White),
        elevation = 2.dp
    ) {

    }
}

@Composable
fun HataDivider(
    modifier: Modifier = Modifier,
    color: Color,
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = thickness,
        startIndent = startIndent
    )
}

@Composable
fun HataCloseButton(){

    IconButton(
        onClick = { },
        modifier = Modifier
            .statusBarsPadding()
            .size(36.dp)
            .background(
                color = Color.Black.copy(alpha = 0.10f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.Close,
            tint = Color.White.copy(alpha = 0.80f).compositeOver(Color.Black),
            contentDescription = stringResource(R.string.close)
        )
    }
}
