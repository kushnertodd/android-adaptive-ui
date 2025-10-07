package com.toddkushnerllc.android_adaptive_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.toddkushnerllc.android_adaptive_ui.PointerEvents.log


@Composable
fun LogPointerEvents(
    filter: PointerEventType? = null
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    var state by remember { mutableStateOf(State(configuration, density)) }
    var noClicks by remember { mutableStateOf(0) }
    val stateChanged: (State) -> Unit = { newState ->
        state.dirty = false
        state = newState.copy(noClicks = ++noClicks)
    }

    //val context = LocalContext.current // Get the current context

    //state.init(configuration, density)
    fun formatDecimals(number: Float, decimals: Int) = String.format("%.${decimals}f", number)

    Column(
        modifier = Modifier.fillMaxSize(), // Makes the Column take the full width
        horizontalAlignment = Alignment.CenterHorizontally // Centers children horizontally
    ) {
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
                log("box index ${state.buttonSizeIndex} (${state.getButtonWidthDp()}, ${state.getButtonHeightDp()}) ")
                MainBox(
                    density, state, filter,
                    stateChanged
                )
                Row() {
                    MaximizeButton(state, state.maximizeButton, stateChanged)
                    MinimizeButton(state, state.minimizeButton, stateChanged)
                    IncrementButton(state, state.incrementButton, stateChanged)
                    DecrementButton(state, state.decrementButton, stateChanged)
                    ExpandButton(state, state.incrementButton, stateChanged)
                    CompressButton(state, state.decrementButton, stateChanged)
                }
            }
        } else {
            ConfirmButtonTapDialog(state.onConfirm, state.onDismiss)
        }
    }
}


