package com.oi.hata.common.ui.reminder

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import apr
import aug
import dec
import feb
import jan
import jul
import jun
import mar
import may
import nov
import oct
import sep
import kotlin.math.max


@Composable
fun Months(){
    Surface(){
        /*Column() {
            Spacer(modifier = Modifier.height(32.dp))
            MonthHeader(modifier = Modifier.padding(32.dp))
            Spacer(modifier = Modifier.height(4.dp))
            MonthsSurface()
        }*/
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                LayoutContainer(modifier = Modifier.padding(16.dp))
                {
                    for (month in CalMonths.values().take(6)) {
                        Month(month.name, Color.White)
                    }
                }
            }
    }
}

@Composable
private fun MonthsSurface(){
    Column(modifier = Modifier.padding(bottom = 8.dp)) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (month in CalMonths.values().take(6)) {
                Month(month.name,Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (month in CalMonths.values().takeLast(6)) {
                Month(month.name,Color.White)
            }
        }
    }
}

@Composable
public fun Month(name: String, color: Color){

    var mthSurfaceModifier: Modifier = Modifier.size(width = CELL_SIZE, height = CELL_SIZE)

    val brush = Brush.horizontalGradient(
        listOf(Color.Red, Color.Green, Color.Blue),
        startX = 10.0f,
        endX = 20.0f
    )

    MonthSurface( color = color,brush,modifier = mthSurfaceModifier) {
        Text(text = name)
    }

}

@Composable
private fun MonthHeader(modifier: Modifier = Modifier){
    Row(modifier = modifier) {
        Text(text = "Months")
    }
}

@Composable
private fun MonthSurface(color: Color,
                         brush: Brush,
                         modifier: Modifier,
                         content: @Composable () -> Unit
){
    
    Surface(modifier = modifier, color = color, shape= CircleShape, border = BorderStroke(Dp.Hairline,brush)) {
        content()
    }
    
}


@Composable
private fun MonthLayout(modifier: Modifier = Modifier,
                        rows: Int,
                        content: @Composable () -> Unit){

    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // measure and position children given constraints logic here

        Log.d("constraints.maxHeight","height >>>>>>"+ constraints.maxHeight + " constraints.maxwidth "+constraints.maxWidth)

        val rowWidths = IntArray(rows) { 0 } // Keep track of the width of each row
        val rowHeights = IntArray(rows) { 0 } // Keep track of the height of each row

        // Don't constrain child views further, measure them with given constraints
        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints)

            // Track the width and max height of each row
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = max(rowHeights[row], placeable.height)

            placeable
        }

        // Grid's width is the widest row
        val width = rowWidths.maxOrNull()?.coerceIn(constraints.minWidth, constraints.maxWidth)
            ?: constraints.minWidth
        // Grid's height is the sum of each row
        val height = rowHeights.sum().coerceIn(constraints.minHeight, constraints.maxHeight)

        // y co-ord of each row
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i - 1] + rowHeights[i - 1]
        }
        layout(width, height) {
            // x co-ord we have placed up to, per row
            val rowX = IntArray(rows) { 0 }
            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.place(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }

    
    BoxWithConstraints() {
        
    }
}


@Composable
fun LayoutSample(content: @Composable () -> Unit){
    Layout(content) { measurables, constraints ->

        Log.d("Comstraints", "constraints.maxHeight" +constraints.maxHeight + " constraints.maxWidth "+constraints.maxWidth)
        // measurables contains one element corresponding to each of our layout children.
        // constraints are the constraints that our parent is currently measuring us with.
        val childConstraints = Constraints(
            minWidth = constraints.minWidth / 2,
            minHeight = constraints.minHeight / 2,
            maxWidth = if (constraints.hasBoundedWidth) {
                Log.d("constraints.hasBoundedWidth", " "+constraints.hasBoundedWidth)
                constraints.maxWidth / 2
            } else {
                Constraints.Infinity
            },
            maxHeight = if (constraints.hasBoundedHeight) {
                Log.d("constraints.hasBoundedHeight", " "+constraints.hasBoundedHeight)
                constraints.maxHeight / 2
            } else {
                Constraints.Infinity
            }
        )

        // We measure the children with half our constraints, to ensure we can be double
        // the size of the children.
        val placeables = measurables.map { it.measure(childConstraints) }
        val layoutWidth = (placeables.maxByOrNull { it.width }?.width ?: 0) * 2
        val layoutHeight = (placeables.maxByOrNull { it.height }?.height ?: 0) * 2
        // We call layout to set the size of the current layout and to provide the positioning
        // of the children. The children are placed relative to the current layout place.
        var yPosition = 0

        layout(layoutWidth, layoutHeight) {
             placeables.forEach {
                 Log.d("layout",">>>>>>>" + layoutHeight +"  "+layoutWidth)
                 Log.d("it", "height "+it.height +" width "+it.width)
                it.placeRelative(0, yPosition)
                 yPosition += it.height
            }
        }
    }

}


@Composable
fun LayoutContainer(modifier: Modifier=Modifier, content: @Composable () -> Unit){

    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        var rowWidths = mutableListOf<Int>()
        var rowWidth = 0
        var maxRowElements = 0
        var layoutHeight = 0
        var layoutWidth = 0
        var index = 1

        val placeables = measurables.map { measurable ->
            val placeable = measurable.measure(constraints)
            if((rowWidth + placeable.width) < constraints.maxWidth){
                rowWidth += placeable.width
            }else{
                rowWidths[index] = rowWidth
                ++index
                rowWidth = 0
                rowWidth += placeable.width
            }

            placeable
        }

        layoutHeight = placeables[0].height * index
        layoutWidth = rowWidths.maxOf { it }

        maxRowElements =  layoutWidth/placeables[0].width

        var xPosition = 0
        var yPosition = 0
        var rowElements = 0

        layout(layoutWidth,layoutHeight){
            placeables.forEach {
                if(rowElements > maxRowElements){
                    xPosition = 0
                    yPosition += it.height
                    rowElements = 0
                }
                it.placeRelative(xPosition,yPosition)
                xPosition = xPosition+it.width
                ++rowElements
            }
        }
    }
}


private val CELL_SIZE = 48.dp

enum class CalMonths {
    Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
}

private val monthColorMap = mapOf(CalMonths.Jan to jan,
    CalMonths.Feb to feb,
    CalMonths.Mar to mar,
    CalMonths.Apr to apr,
    CalMonths.May to may,
    CalMonths.Jun to jun,
    CalMonths.Jul to jul,
    CalMonths.Aug to aug,
    CalMonths.Sep to sep,
    CalMonths.Oct to oct,
    CalMonths.Nov to nov,
    CalMonths.Dec to dec
                            )


/*@Preview
@Composable
fun LayoutSampleTest(){
   Surface(){
       Column(modifier = Modifier.padding(bottom = 8.dp)) {
           LayoutContainer(modifier = Modifier.padding(16.dp))
           {
               for (month in CalMonths.values().take(6)) {
                   Month(month.name, Color.White)
               }
           }
       }
   }
}*/
