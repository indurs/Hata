package com.oi.hata.common.reminder.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class DateTransition(
    cornerRadius: State<Dp>,
    selectedAlpha: State<Float>,
    colorAlpha: State<Color>
) {
    val cornerRadius by cornerRadius
    val selectedAlpha by selectedAlpha
    val colorAlpha by colorAlpha
}
enum class SelectionState { Unselected, Selected }

@Composable
fun dateTransition(date:Int, selectedDates: List<Int>): DateTransition {
    val transition = updateTransition(
        targetState = if (date in selectedDates) SelectionState.Selected else SelectionState.Unselected,
        label = ""
    )
    val corerRadius = transition.animateDp { state ->
        when (state) {
            SelectionState.Unselected -> 4.dp
            SelectionState.Selected -> 24.dp
        }
    }
    val selectedAlpha = transition.animateFloat { state ->
        when (state) {
            SelectionState.Unselected -> 0f
            SelectionState.Selected -> 0.8f
        }
    }

    val colorAlpha = transition.animateColor { state ->
        when(state){
            SelectionState.Unselected -> Color.Blue
            SelectionState.Selected -> Color.White
        }
    }

    return remember(transition) {
        DateTransition(corerRadius, selectedAlpha,colorAlpha)
    }
}

class MonthTransition(
    cornerRadius: State<Dp>,
    selectedAlpha: State<Float>,
    colorAlpha: State<Color>
){
    val selectedAlpha by selectedAlpha
    val colorAlpha by colorAlpha
    val cornerRadius by cornerRadius
}

@Composable
fun monthTransition(month: String,selectedMonths: List<String>): MonthTransition {
    val transition = updateTransition(
        targetState = if(month in selectedMonths) SelectionState.Selected else SelectionState.Unselected,
        label = ""
    )
    val selectedAlpha = transition.animateFloat { state ->
        when (state) {
            SelectionState.Unselected -> 0f
            SelectionState.Selected -> 0.8f
        }
    }

    val colorAlpha = transition.animateColor { state ->
        when(state){
            SelectionState.Unselected -> MaterialTheme.colors.surface.copy(alpha = 0.50f)
            SelectionState.Selected -> Color.White
        }
    }

    val corerRadius = transition.animateDp { state ->
        when (state) {
            SelectionState.Unselected -> 60.dp
            SelectionState.Selected -> 5.dp
        }
    }

    return remember(transition) { MonthTransition(selectedAlpha = selectedAlpha,colorAlpha = colorAlpha, cornerRadius = corerRadius) }
}

class WeekTransition(
    cornerRadius: State<Dp>,
    selectedAlpha: State<Float>,
) {
    val cornerRadius by cornerRadius
    val selectedAlpha by selectedAlpha
}

@Composable
fun weekTransition(week:String, selectedWeeks: List<String>): WeekTransition {
    val transition = updateTransition(
        targetState = if (week in selectedWeeks) SelectionState.Selected else SelectionState.Unselected,
        label = ""
    )
    val corerRadius = transition.animateDp { state ->
        when (state) {
            SelectionState.Unselected -> 8.dp
            SelectionState.Selected -> 24.dp
        }
    }
    val selectedAlpha = transition.animateFloat { state ->
        when (state) {
            SelectionState.Unselected -> 0f
            SelectionState.Selected -> 0.8f
        }
    }

    return remember(transition) {
        WeekTransition(corerRadius, selectedAlpha)
    }
}

