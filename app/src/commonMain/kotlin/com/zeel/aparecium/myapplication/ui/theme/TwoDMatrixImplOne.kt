package com.zeel.aparecium.myapplication.ui.theme

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextDecorationLineStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.zeel.lazyinfinitegrid.LazyTable



@Composable
fun ExcelSheet(data: ImmutableList) {
    BoxWithConstraints {
        val height = 80.dp
        val width = 100.dp
        val screenHeight = constraints.maxHeight.toFloat()
        val screenWidth = constraints.maxWidth.toFloat()
        val heightInPixels = with(LocalDensity.current) {
            height.toPx()
        }
        val widthInPixels = with(LocalDensity.current) {
            width.toPx()
        }
        val maxVerticalScrollThatCanHappen by remember(screenHeight) {
            mutableStateOf(heightInPixels * (data.data.size+1) - screenHeight)
        }

        val maxHorizontalScrollThatCanHappen by remember(screenWidth) {
            mutableStateOf(widthInPixels * (data.data[0].size+1) - screenWidth)
        }

        /**
         * r: row, c: col
         * 0..r
         * .
         * .
         * c
         * outer loop is c and inner loop is r
         *
         * first index is col then row
         */
        val rowHeader: @Composable (Int) -> Unit = {
            Text(
                "Row$it",
                Modifier
                    .height(height)
                    .width(width)
            )
        }
        val columnHeader: @Composable (Int) -> Unit = {
            Text(
                "Col${it-1}",
                Modifier
                    .height(height)
                    .width(width)
            )
        }
        LazyTable(
            colCount = data.data.size,
            rowCount = data.data[0].size,
            heightInPixels = heightInPixels,
            maxVerticalScrollThatCanHappen = maxVerticalScrollThatCanHappen,
            maxHorizontalScrollThatCanHappen = maxHorizontalScrollThatCanHappen,
            rowHeader = rowHeader,
            columnHeader = columnHeader
        ) { row, col ->
            Text(
                data.data[col][row],
                Modifier
                    .height(height)
                    .width(width),
            )
        }
    }
}

@Composable
fun Test() {
    val data = MutableList(50) { row ->
        MutableList(20) {
            "row$row col$it"
        }
    }
    ExcelSheet(ImmutableList(data))
}

@Immutable
data class ImmutableList(val data: List<List<String>>)
