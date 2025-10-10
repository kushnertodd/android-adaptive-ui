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
import kotlin.math.min

@Composable
fun LogPointerEvents(
    filter: PointerEventType? = null
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    var noClicks by remember { mutableStateOf(0) }

    var pointerEventState by remember { mutableStateOf(PointerEventState.START) }
    var buttonSizeIndex by remember { mutableStateOf(0) }
    var state by remember {
        mutableStateOf(
            State(
                configuration,
                density,
                pointerEventState,
                buttonSizeIndex
            )
        )
    }

    fun getButtonHeightDp() = ButtonParameters.buttonHeightsDp[buttonSizeIndex]
    fun getButtonHeightPx() = ButtonParameters.buttonHeightsPx[buttonSizeIndex]
    fun getButtonWidthDp() = ButtonParameters.buttonWidthsDp[buttonSizeIndex]
    fun getButtonWidthPx() = ButtonParameters.buttonWidthsPx[buttonSizeIndex]
    fun getScreenButtonColumn() = ButtonParameters.screenButtonColumns[buttonSizeIndex]
    fun getScreenButtonRow() = ButtonParameters.screenButtonRows[buttonSizeIndex]
    fun getButtonRoundedSize() = ButtonParameters.buttonRoundedSizes[buttonSizeIndex]
    fun getButtonTextSize() = ButtonParameters.buttonTextSizes[buttonSizeIndex]

    val setButtonSizeIndex: (Int) -> Unit =
        { newButtonSizeIndex ->
            buttonSizeIndex = newButtonSizeIndex
            state.boxOffset.x = min(
                state.boxOffset.x,
                state.box.width.px - getButtonWidthPx()
            )
            state.boxOffset.y = min(
                state.boxOffset.y,
                state.box.height.px - getButtonHeightPx()
            )
            state.dirty = true
        }
    val setPointerEventState: (PointerEventState) -> Unit =
        { newPointerEventState ->
            pointerEventState = newPointerEventState
            state.dirty = true
        }
    val decrementButtonSize: () -> Unit = {
        if (buttonSizeIndex > 0)
            setButtonSizeIndex(buttonSizeIndex - 1)

    }
    val decrementButton: () -> Unit = {
        decrementButtonSize()
    }

    val incrementButtonSize: () -> Unit = {
        if (buttonSizeIndex < ButtonParameters.buttonSizeIndexMax - 1)
            setButtonSizeIndex(buttonSizeIndex + 1)
    }
    val incrementButton: () -> Unit = {
        incrementButtonSize()
    }
    val maximizeButton: () -> Unit = {
        setButtonSizeIndex(ButtonParameters.buttonSizeIndexMax - 1)
    }
    val minimizeButton: () -> Unit = {
        setButtonSizeIndex(0)
    }
    val onConfirm: () -> Unit = {
        decrementButton()
        state.showDialog = false
        setPointerEventState(PointerEventState.START)
    }
    val onDismiss: () -> Unit = {
        incrementButton()
        state.showDialog = false
        setPointerEventState(PointerEventState.START)
    }


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
            "button size ${getButtonWidthDp()} x ${getButtonHeightDp()}",
            fontSize = 12.sp
        )
        Text(
            "button size ${getButtonWidthPx()}.px x ${getButtonHeightPx()}.px",
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
                log("reconstituting column")
                log("box button index ${buttonSizeIndex} gap index ${state.buttonGapPctIndex} (${getButtonWidthDp()}, ${getButtonHeightDp()}) ")
                MainBox(
                    density, state, filter,
                    pointerEventState,
                    buttonSizeIndex,
                    stateChanged,
                    decrementButtonSize,
                    incrementButtonSize,
                    setButtonSizeIndex,
                    setPointerEventState
                )
                Row() {
                    MaximizeButton(state, maximizeButton, stateChanged)
                    MinimizeButton(state, minimizeButton, stateChanged)
                    IncrementButton(state, incrementButton, stateChanged)
                    DecrementButton(state, decrementButton, stateChanged)
                    ExpandButton(state, incrementButton, stateChanged)
                    CompressButton(state, decrementButton, stateChanged)
                }
            }
        } else {
            ConfirmButtonTapDialog(onConfirm, onDismiss)
        }
    }
}


