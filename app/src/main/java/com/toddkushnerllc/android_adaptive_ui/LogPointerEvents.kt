package com.toddkushnerllc.android_adaptive_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toddkushnerllc.android_adaptive_ui.PointerEvents.log
import kotlin.math.roundToInt


@Composable
fun LogPointerEvents(
    filter: PointerEventType? = null
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    var state by remember { mutableStateOf(State(configuration, density)) }
    //var noClicks by remember { mutableStateOf(0) }
    val stateChanged: () -> Unit = {
        state.dirty = false
        state = state.copy(clicked = ++state.noClicks)
    }

    //val context = LocalContext.current // Get the current context

    state.init(configuration, density)
    fun formatDecimals(number: Float, decimals: Int) = String.format("%.${decimals}f", number)

    Column(
        modifier = Modifier.fillMaxSize(), // Makes the Column take the full width
        horizontalAlignment = Alignment.CenterHorizontally // Centers children horizontally
    ) {
        //var changed by remember { mutableStateOf(0) }
        //var changed by remember { mutableStateOf(false) }
        Text("Adaptive UI", textAlign = TextAlign.Center, fontSize = 48.sp)
        Text("screen size ${state.screen.width.dp} x ${state.screen.height.dp}", fontSize = 12.sp)
        Text(
            "screen size ${state.screen.width.px}.px x ${state.screen.height.px}.px",
            fontSize = 12.sp
        )
        Text(
            "button size ${state.getButtonWidthDp()} x ${state.getButtonHeightDp()}",
            fontSize = 12.sp
        )
        Text(
            "button size ${state.getButtonWidthPx()}.px x ${state.getButtonHeightPx()}.px",
            fontSize = 12.sp
        )
        Text(
            "offsetX ${formatDecimals(state.boxOffset.x, 1)}.px " +
                    "offsetY ${
                        formatDecimals(state.boxOffset.y, 1)
                    }.px",
            fontSize = 12.sp
        )
        if (!state.showDialog) {
            Column() {
                log("reconstuting column")
                //var changed1 by remember { mutableStateOf(0) }
                //var changed1 by remember { mutableStateOf(false) }
                log("box index ${state.buttonSizeIndex} (${state.getButtonWidthDp()}, ${state.getButtonHeightDp()}) ")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .onGloballyPositioned { coordinates ->
                            state.box = Dimensions(
                                Extent.pxToExtent(density, coordinates.size.width.toFloat()),
                                Extent.pxToExtent(density, coordinates.size.height.toFloat()),
                            )
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
                                        if (state.dirty) {
                                            stateChanged()
                                            //changed++
                                            //changed = !changed
                                        }
                                    }
                                }
                            }
                        }
                ) {
                    val buttonWidth =
                        with(density) { state.getButtonWidthDp().toPx().roundToInt() }
                    val buttonheight =
                        with(density) { state.getButtonHeightDp().toPx().roundToInt() }
                    val offsetBox1X = state.boxOffset.x.roundToInt()
                    val offsetBox1Y = state.boxOffset.y.roundToInt()

                    val offsetBox2X = state.boxOffset.x.roundToInt() + buttonWidth + 20
                    val offsetBox2Y = state.boxOffset.y.roundToInt()

                    val offsetBox3X = state.boxOffset.x.roundToInt()
                    val offsetBox3Y = state.boxOffset.y.roundToInt() + buttonheight + 20

                    val offsetBox4X = state.boxOffset.x.roundToInt() + buttonheight + 20
                    val offsetBox4Y = state.boxOffset.y.roundToInt() + buttonheight + 20

                    ButtonBox(
                        1, state, filter, "click me 1",
                        offsetBox1X,
                        offsetBox1Y,
                        stateChanged
                    )
                    ButtonBox(
                        2, state, filter, "click me 2",
                        offsetBox2X,
                        offsetBox2Y,
                        stateChanged
                    )
                    ButtonBox(
                        3, state, filter, "click me 3",
                        offsetBox3X,
                        offsetBox3Y,
                        stateChanged
                    )
                    ButtonBox(
                        4, state, filter, "click me 4",
                        offsetBox4X,
                        offsetBox4Y,
                        stateChanged
                    )
                }
                Row() {
                    MaximizeButton(state.maximizeButton,stateChanged)
                    MinimizeButton(state.minimizeButton,stateChanged)
                    IncrementButton(state.incrementButton,stateChanged)
                    DecrementButton(state.decrementButton,stateChanged)
                    ExpandButton(state.incrementButton, stateChanged)
                    CompressButton(state.decrementButton,stateChanged)
                }
            }
        } else {
            ConfirmButtonTapDialog(state.onConfirm, state.onDismiss)
        }
    }
}


