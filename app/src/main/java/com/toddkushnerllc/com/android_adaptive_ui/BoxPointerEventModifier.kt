package com.toddkushnerllc.com.android_adaptive_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.xr.compose.testing.toDp
/*

@Composable
fun Modifier.boxPointerEventModifier(filter: PointerEventType?): Modifier  = this
.fillMaxWidth()
.height(600.dp)
.background(MaterialTheme.colorScheme.secondaryContainer)
.onGloballyPositioned { coordinates ->
    // Convert pixels to DP using LocalDensity
    val density = null
    ButtonParameters.boxWidthDp =
        with(density) { coordinates.size.width.toDp() }
    ButtonParameters.boxHeightDp =
        with(density) { coordinates.size.height.toDp() }
    ButtonParameters.boxWidthPx =
        with(density) { ButtonParameters.boxWidthDp.toPx() }
    ButtonParameters.boxHeightPx =
        with(density) { ButtonParameters.boxHeightDp.toPx() }
}
.pointerInput(filter) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            // handle pointer event
            if (filter == null || event.type == filter) {
                PointerEvents.onBoxPointerEvent(
                    event,
                    pointerEventState,
                    buttonSizeIndex,
                    setPointerEventState,
                    decrementButtonSize,
                    incrementButtonSize,
                    setShowDialog,
                    setFirstPosition,
                    setChangePosition,
                    setFinalPosition,
                    setButtonMoving,
                    testButtonMoving,
                    setButtonRelease
                )
            }
        }
    }
}
*/
