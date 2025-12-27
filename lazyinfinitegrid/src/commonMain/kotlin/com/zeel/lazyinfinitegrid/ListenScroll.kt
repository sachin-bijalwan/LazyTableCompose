package com.zeel.lazyinfinitegrid

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

/**
 * Composable for listening to scroll events
 */
@Composable
fun ListenScroll(
    currentScroll: State<Float>,
    childState: LazyListState,
    childStateScroll: MutableState<Float> = mutableStateOf(0f)
) {
    ListenScroll(
        currentScroll,
        childState,
        {
            childStateScroll.value
        }
    ) {
        childStateScroll.value = it
    }
}

@Composable
fun ListenScroll(
    currentScroll: State<Float>,
    childState: LazyListState,
    childStateScroll: () -> Float,
    childStateScrollUpdate: (Float) -> Unit
) {
    LaunchedEffect(currentScroll.value) {
        val scrollRequired = currentScroll.value - (childStateScroll.invoke())
        childStateScrollUpdate.invoke(currentScroll.value)
        val consumed = childState.dispatchRawDelta(scrollRequired)
        if (consumed != scrollRequired) {
            childStateScrollUpdate.invoke(childStateScroll.invoke() - (scrollRequired - consumed))
        }
    }
}
