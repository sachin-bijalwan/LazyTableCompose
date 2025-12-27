package com.zeel.lazyinfinitegrid

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

/**
 * A table with support for adding sticky headers and only
 * rendering views within viewport
 */
@Composable
fun LazyTable(
    colCount: Int,
    rowCount: Int,
    heightInPixels: Float,
    maxVerticalScrollThatCanHappen: Float,
    maxHorizontalScrollThatCanHappen: Float,
    modifier: Modifier = Modifier,
    rowHeader: @Composable ((Int) -> Unit)? = null,
    columnHeader: @Composable ((Int) -> Unit)? = null,
    itemComposable: @Composable (Int, Int) -> Unit,
) {
    /**
     * It might be the case that all LazyListState are present together in
     * viewport. So we're storing scroll pixels in this array
     * and then later using it to calculate how much more scroll needs to take place
     */
    val columnScrollMap = remember {
        mutableStateMapOf<Int, Float>()
    }

    /**
     * This connection receives all scroll events from children
     */
    val nestedScrollConnection =
        remember(maxVerticalScrollThatCanHappen, maxHorizontalScrollThatCanHappen) {
            TableNestedScrollConnection(
                maxVerticalScrollThatCanHappen,
                maxHorizontalScrollThatCanHappen
            )
        }
    Column(modifier.nestedScroll(nestedScrollConnection)) {
        if (columnHeader!=null) {
            /**
             * header row and it's listeners
             */
            val headerRowState = rememberLazyListState()
            ListenScroll(
                nestedScrollConnection.currentXScroll, headerRowState,
            )
            LazyRow(state = headerRowState) {
                items(rowCount + 1) {
                    columnHeader.invoke(it)
                }
            }
        }
        /**
         * Rest of UI
         */
        Row {
            if (rowHeader!=null) {
                /**
                 * Header column
                 */
                val headerColumnState = rememberLazyListState()
                ListenScroll(
                    nestedScrollConnection.currentYScroll, headerColumnState,
                )
                LazyColumn(state = headerColumnState) {
                    items(colCount) {
                        rowHeader.invoke(it)
                    }
                }
            }

            /**
             * Grid data
             */
            val rowState = rememberLazyListState()
            ListenScroll(
                nestedScrollConnection.currentXScroll,
                rowState
            )
            LazyRow(
                modifier = Modifier.fillMaxHeight(),
                state = rowState
            ) {
                items(rowCount) { row ->
                    val columnScrollState = remember {
                        columnScrollMap[row] = nestedScrollConnection.currentYScroll.floatValue
                        val scroll = nestedScrollConnection.currentYScroll.floatValue
                        LazyListState(
                            (scroll / heightInPixels).toInt(),
                            (scroll % heightInPixels).toInt()
                        )
                    }
                    LazyColumn(state = columnScrollState) {
                        items(colCount) { col: Int ->
                            itemComposable.invoke(row, col)
                        }
                    }
                    ListenScroll(nestedScrollConnection.currentYScroll,
                        columnScrollState,
                        { columnScrollMap[row] ?: 0f }
                    ) {
                        columnScrollMap[row] = it
                    }
                }
            }
        }
    }
}