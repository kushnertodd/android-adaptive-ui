package com.toddkushnerllc.com.android_adaptive_ui

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

enum class PointerEventState {
    START,
    BOX_PRESS,
    BOX_RELEASE,
    BOX_TAP,
    BUTTON_PRESS,
    BUTTON_BOX_PRESS,
    BUTTON_BOX_RELEASE,
    BUTTON_RELEASE,
    BUTTON_TAP
}

data class State(
    val configuration: Configuration, val density: Density,
    var clicked: Int = 0,
    var pointerEventState: PointerEventState = PointerEventState.START,
    var buttonPadding: Dp = 0.dp,
    var previousPosition: Offset = Offset.Zero,
    var showDialog: Boolean = false,
    var buttonMoving: Boolean = false,
    var buttonPressMillis: Long = 0L,
    val buttonTapThresholdMillis: Int = 250,
    var buttonSizeIndex: Int = 0,
    var gapPercentage: Float = 0.25f,
    var screenHeightDp: Dp = 0.dp,
    var screenHeightPx: Float = 0f,
    var screenWidthDp: Dp = 0.dp,
    var screenWidthPx: Float = 0f,
    var offsetX: Float = 0f,
    var offsetY: Float = 0f
) {
    fun init(configuration: Configuration, density: Density) {
        screenHeightDp = configuration.screenHeightDp.dp
        screenHeightPx = with(density) { screenHeightDp.toPx() }
        screenWidthDp = configuration.screenWidthDp.dp
        screenWidthPx = with(density) { screenWidthDp.toPx() }
        offsetX = screenWidthPx / 2 + ButtonParameters.buttonBoxWidthPx / 2
        offsetY = screenHeightPx / 2 + ButtonParameters.buttonBoxHeightPx / 2
    }

    // common
    val setButtonSizeIndex: (Int) -> Unit =
        { newButtonSizeIndex ->
            buttonSizeIndex = newButtonSizeIndex
            ButtonParameters.initButtonSizeIndex(density, buttonSizeIndex)
            offsetX = min(
                offsetX,
                ButtonParameters.boxWidthPx - ButtonParameters.buttonWidthPx
            )
            offsetY = min(
                offsetY,
                ButtonParameters.boxHeightPx - ButtonParameters.buttonHeightPx
            )
        }
    val setPointerEventState: (PointerEventState) -> Unit =
        { newPointerEventState -> pointerEventState = newPointerEventState }

    // other
    val decrementButtonSize: () -> Unit = {
//        if (buttonSizeIndex > 1)
//            setButtonSizeIndex(buttonSizeIndex - 2)
        if (buttonSizeIndex > 0)
            setButtonSizeIndex(buttonSizeIndex - 1)
    }
    val decrementButton: () -> Unit = {
        decrementButtonSize()
    }
    val incrementButtonSize: () -> Unit = {
//        if (buttonSizeIndex < ButtonParameters.buttonSizeIndexMax - 1)
//            setButtonSizeIndex(buttonSizeIndex + 2)
        if (buttonSizeIndex < ButtonParameters.buttonSizeIndexMax - 1)
            setButtonSizeIndex(buttonSizeIndex + 1)
    }
    val incrementButton: () -> Unit = {
        incrementButtonSize()
    }
    val maximizeButton: () -> Unit = {
        setButtonSizeIndex(ButtonParameters.buttonSizeIndexMax)
    }
    val minimizeButton: () -> Unit = {
        setButtonSizeIndex(0)
    }
    val onConfirm: () -> Unit = {
        decrementButton()
        showDialog = false
        setPointerEventState(PointerEventState.START)
    }
    val onDismiss: () -> Unit = {
        incrementButton()
        showDialog = false
        setPointerEventState(PointerEventState.START)
    }
    val setButtonMoving: (Boolean) -> Unit = { newButtonMoving ->
        buttonMoving = newButtonMoving
    }
    val setButtonPress: (Long) -> Unit =
        { newButtonPressMillis ->
            buttonPressMillis = newButtonPressMillis
        }
    val setButtonRelease: (Long) -> Boolean =
        { buttonReleaseMillis ->
            if (buttonReleaseMillis - buttonPressMillis < buttonTapThresholdMillis) {
                buttonPressMillis = 0
                true
            } else false
        }
    val setChangePosition: (PointerInputChange) -> Unit =
        { change ->
            // Calculate the difference from the previous position
            val deltaX = change.position.x - previousPosition.x
            val deltaY = change.position.y - previousPosition.y

            // Update offsetX and offsetY based on the drag amount (or delta from previous)
            offsetX += deltaX
            offsetX = max(offsetX, 0f)
            offsetX = min(
                offsetX,
                ButtonParameters.boxWidthPx - ButtonParameters.buttonWidthPx
            )
            offsetY += deltaY
            offsetY = max(offsetY, 0f)
            offsetY = min(
                offsetY,
                ButtonParameters.boxHeightPx - ButtonParameters.buttonHeightPx
            )
            // Update previous position for the next onDrag call
            previousPosition = change.position
            change.consume()
        }
    val setFinalPosition: () -> Unit =
        {
            previousPosition = Offset.Zero
        }
    val setFirstPosition: (Offset) -> Unit =
        { startOffset ->
            previousPosition = startOffset
        }
    val setShowDialog: () -> Unit =
        { showDialog = true }
    val testButtonMoving: () -> Boolean =
        { buttonMoving }

}

@Composable
fun LogPointerEvents(
    /*buttonWidth: Dp, buttonHeight: Dp, buttonPadding: Dp,*/ filter: PointerEventType? = null
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
    ButtonParameters.init(density)
    fun formatDecimals(number: Float, decimals: Int) = String.format("%.${decimals}f", number)

    Column(
        modifier = Modifier.fillMaxSize(), // Makes the Column take the full width
        horizontalAlignment = Alignment.CenterHorizontally // Centers children horizontally
    ) {
        Text("Adaptive UI", textAlign = TextAlign.Center, fontSize = 48.sp)
        Text("screen size ${state.screenWidthPx} x ${state.screenHeightPx}", fontSize = 18.sp)
        Text(
            "box size ${ButtonParameters.boxWidthPx} x ${ButtonParameters.boxHeightPx}",
            fontSize = 18.sp
        )
        Text(
            "button box size ${ButtonParameters.buttonBoxWidthPx} x ${ButtonParameters.buttonBoxHeightPx}",
            fontSize = 18.sp
        )
        Text(
            "offsetX ${formatDecimals(state.offsetX, 1)} offsetY ${
                formatDecimals(
                    state.offsetY,
                    1
                )
            }",
            fontSize = 18.sp
        )
        if (!state.showDialog) {
            Column() {
                ButtonParameters.initButtonSizeIndex(density, state.buttonSizeIndex)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .onGloballyPositioned { coordinates ->
                            // Convert pixels to DP using LocalDensity
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
                                ButtonParameters.buttonWidthDp,
                                ButtonParameters.buttonHeightDp
                            )
                            //.align(Alignment.Center) // Center the button within the Box
                            .clip(RoundedCornerShape(ButtonParameters.buttonRoundedSizes[state.buttonSizeIndex]))//28.dp)) // Apply rounded corners
                            .background(MaterialTheme.colorScheme.primary)
                            .onGloballyPositioned { coordinates ->
                                // Convert pixels to DP using LocalDensity
                                ButtonParameters.buttonBoxWidthDp =
                                    with(density) { coordinates.size.width.toDp() }
                                ButtonParameters.buttonBoxHeightDp =
                                    with(density) { coordinates.size.height.toDp() }
                                ButtonParameters.buttonBoxWidthPx =
                                    with(density) { ButtonParameters.buttonBoxWidthDp.toPx() }
                                ButtonParameters.buttonBoxHeightPx =
                                    with(density) { ButtonParameters.buttonBoxHeightDp.toPx() }
                            }
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


