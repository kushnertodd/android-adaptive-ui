package com.toddkushnerllc.android_adaptive_ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt




@Composable
fun LogPointerEvents(
    filter: PointerEventType? = null
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    var state by remember { mutableStateOf(State(configuration, density)) }
    var noClicks by remember { mutableStateOf(0) }
    val stateChanged: () -> Unit = {
        state = state.copy(clicked = ++noClicks)
    }
    //val context = LocalContext.current // Get the current context

    state.init(configuration, density)
    fun formatDecimals(number: Float, decimals: Int) = String.format("%.${decimals}f", number)

    Column(
        modifier = Modifier.fillMaxSize(), // Makes the Column take the full width
        horizontalAlignment = Alignment.CenterHorizontally // Centers children horizontally
    ) {
        Text("Adaptive UI", textAlign = TextAlign.Center, fontSize = 48.sp)
        Text("screen size ${state.screenWidthDp} x ${state.screenHeightDp}", fontSize = 12.sp)
        Text("screen size ${state.screenWidthPx}.px x ${state.screenHeightPx}.px", fontSize = 12.sp)
        //Text("box size ${state.boxWidthDp} x ${state.boxHeightDp}", fontSize = 12.sp)
        //Text("box size ${state.boxWidthPx}.px x ${state.boxHeightPx}.px", fontSize = 12.sp)
        Text(
            "button size ${state.getButtonWidthDp()} x ${state.getButtonHeightDp()}",
            fontSize = 12.sp
        )
        Text(
            "button size ${state.getButtonWidthPx()}.px x ${state.getButtonHeightPx()}.px",
            fontSize = 12.sp
        )
        Text(
            "offsetX ${formatDecimals(state.offsetX, 1)}.px " +
                    "offsetY ${
                        formatDecimals(state.offsetY, 1)
                    }.px",
            fontSize = 12.sp
        )
        if (!state.showDialog) {
            Column() {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .onGloballyPositioned { coordinates ->
                            // Convert pixels to DP using LocalDensity
                            state.boxWidthDp =
                                with(density) { coordinates.size.width.toDp() }
                            state.boxHeightDp =
                                with(density) { coordinates.size.height.toDp() }
                            state.boxWidthPx =
                                with(density) { state.boxWidthDp.toPx() }
                            state.boxHeightPx =
                                with(density) { state.boxHeightDp.toPx() }
                        }
                        .pointerInput(filter) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    // handle pointer event
                                    if (filter == null || event.type == filter) {
                                        PointerEvents.onBoxPointerEvent(
                                            event,
                                            state,
                                            stateChanged
                                        )
                                    }
                                }
                            }
                        }
                ) {
                    // The Button composable placed inside the Box
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    state.offsetX.roundToInt(),
                                    state.offsetY.roundToInt()
                                )
                            }
                            .size(
                                state.getButtonWidthDp(),
                                state.getButtonHeightDp()
                            )
                            //.align(Alignment.Center) // Center the button within the Box
                            .clip(RoundedCornerShape(ButtonParameters.buttonRoundedSizes[state.buttonSizeIndex]))//28.dp)) // Apply rounded corners
                            .background(MaterialTheme.colorScheme.primary)
                            .pointerInput(filter) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        // handle pointer event
                                        if (filter == null || event.type == filter) {
                                            PointerEvents.onButtonPointerEvent(
                                                event,
                                                state,
                                                stateChanged
                                            )
                                        }
                                    }
                                }
                            }
                    ) {
                        Text(
                            text = "Click Me",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = ButtonParameters.buttonTextSizes[state.buttonSizeIndex]
                        )
                    }
                }
                Row() {
                    MaximizeButton(state.maximizeButton, stateChanged)
                    MinimizeButton(state.minimizeButton, stateChanged)
                    IncrementButton(state.incrementButton, stateChanged)
                    DecrementButton(state.decrementButton, stateChanged)
                    ExpandButton(state.incrementButton, stateChanged)
                    CompressButton(state.decrementButton, stateChanged)
                }
            }
        } else {
            ConfirmButtonTapDialog(state.onConfirm, state.onDismiss, stateChanged)
        }
    }
}


