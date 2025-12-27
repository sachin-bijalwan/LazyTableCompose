package com.zeel.lazyinfinitegrid

import androidx.compose.runtime.FloatState
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import kotlin.math.abs

/**
 * Class designed to listen to scroll events in grid and based on those events update
 * the currentYScroll and currentXScroll's states. Note that this class will consume all scroll
 * events and will give scroll events only when maxVerticalScroll or maxHorizontalScoll has been
 * exceeded
 *
 * @param currentYScroll
 * @param currentXScroll
 * @param maxVerticalScrollThatCanHappen - max value of Y scroll that can happen
 * @param maxHorizontalScrollThatCanHappen - max value of X scroll that can happen
 */
class TableNestedScrollConnection(
    private val maxVerticalScrollThatCanHappen: Float,
    private val maxHorizontalScrollThatCanHappen: Float
) : NestedScrollConnection {
    val currentYScroll: FloatState
        get() = _currentYScroll
    val currentXScroll: FloatState
        get() = _currentXScroll
    private val _currentYScroll: MutableFloatState by lazy {
        mutableFloatStateOf(0f)
    }
    private val _currentXScroll: MutableFloatState by lazy {
        mutableFloatStateOf(0f)
    }
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val newVerticalScroll =
            (_currentYScroll.floatValue + (-1 * available.y)).coerceAtMost(
                maxVerticalScrollThatCanHappen
            )
                .coerceAtLeast(0f)
        val newHorizontalScroll =
            (_currentXScroll.floatValue + (-1 * available.x)).coerceAtMost(
                maxHorizontalScrollThatCanHappen
            )
                .coerceAtLeast(0f)

        /**
         * Figure out in which direction scroll is happening and update scroll based on that
         */
        if (abs(available.y) > 0f && abs(available.x) < abs(available.y)
            && newVerticalScroll != _currentYScroll.floatValue
        ) {
            _currentYScroll.floatValue = newVerticalScroll
            return Offset(available.x, available.y)
        } else if (abs(available.x) > 0f &&
            abs(available.y) < abs(available.x) &&
            newHorizontalScroll != _currentXScroll.floatValue
        ) {

            _currentXScroll.floatValue = newHorizontalScroll
            return Offset(available.x, available.y)
        }

        return super.onPreScroll(available, source)
    }
}