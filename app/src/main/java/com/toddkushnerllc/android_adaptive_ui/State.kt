package com.toddkushnerllc.android_adaptive_ui

import android.content.res.Configuration
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

data class State(
    val configuration: Configuration,
    val density: Density,
    var clicked: Int = 0,
    var pointerEventState: PointerEventState = PointerEventState.START,
    var buttonPadding: Dp = 0.dp,
    var previousPosition: Offset = Offset.Zero,
    var showDialog: Boolean = false,
    var buttonGapIndex: Int = 0,
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
    var offsetY: Float = 0f,
    var boxHeightDp: Dp = 0.dp,
    var boxHeightPx: Float = 0f,
    var boxWidthDp: Dp = 0.dp,
    var boxWidthPx: Float = 0f,
    var first: Boolean = true
) {
    fun getButtonGapPercentage() = ButtonParameters.buttonGapPercentage[buttonGapIndex]
    fun getButtonHeightDp() = ButtonParameters.buttonHeightsDp[buttonSizeIndex]
    fun getButtonHeightPx() = ButtonParameters.buttonHeightsPx[buttonSizeIndex]
    fun getButtonWidthDp() = ButtonParameters.buttonWidthsDp[buttonSizeIndex]
    fun getButtonWidthPx() = ButtonParameters.buttonWidthsPx[buttonSizeIndex]
    fun getScreenButtonColumn() = ButtonParameters.screenButtonColumns[buttonSizeIndex]
    fun getScreenButtonRow() = ButtonParameters.screenButtonRows[buttonSizeIndex]
    fun getButtonRoundedSize() = ButtonParameters.buttonRoundedSizes[buttonSizeIndex]
    fun getButtonTextSize() = ButtonParameters.buttonTextSizes[buttonSizeIndex]
    fun init(configuration: Configuration, density: Density) {
        ButtonParameters.init(density)
        screenHeightDp = configuration.screenHeightDp.dp
        screenHeightPx = with(density) { screenHeightDp.toPx() }
        screenWidthDp = configuration.screenWidthDp.dp
        screenWidthPx = with(density) { screenWidthDp.toPx() }
        if (first) {
            offsetX = screenWidthPx / 2 - getButtonWidthPx() / 2
            //offsetY = screenHeightPx / 2 - getButtonHeightPx() / 2
            offsetY = 675 - getButtonHeightPx() / 2
            first = false
        }
    }

    // common
    val setButtonSizeIndex: (Int) -> Unit =
        { newButtonSizeIndex ->
            buttonSizeIndex = newButtonSizeIndex
            offsetX = min(
                offsetX,
                boxWidthPx - getButtonWidthPx()
            )
            offsetY = min(
                offsetY,
                boxHeightPx - getButtonHeightPx()
            )
        }
    val setPointerEventState: (PointerEventState) -> Unit =
        { newPointerEventState -> pointerEventState = newPointerEventState }

    // other
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
                boxWidthPx - getButtonWidthPx()
            )
            offsetY += deltaY
            offsetY = max(offsetY, 0f)
            offsetY = min(
                offsetY,
                boxHeightPx - getButtonHeightPx()
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
}
