package com.toddkushnerllc.android_adaptive_ui

import android.content.res.Configuration
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

data class Extent(val dp: Dp = 0.dp, val px: Float = 0f) {
    companion object {
        fun dpToExtent(density: Density, dp: Dp) =
            Extent(dp, with(density) { dp.toPx() })

        fun pxToExtent(density: Density, px: Float) =
            Extent(with(density) { px.toDp() }, px)
    }
}

data class Dimensions(val width: Extent, val height: Extent)

data class BoxOffset(var x: Float = 0f, var y: Float = 0f)

data class State(
    val configuration: Configuration,
    val density: Density,
    var noClicks: Int = 0,
    //var clicked: Int = 0,
    var pointerEventState: PointerEventState = PointerEventState.START,
    var buttonPadding: Dp = 0.dp,
    var previousPosition: Offset = Offset.Zero,
    var showDialog: Boolean = false,
    var buttonGapPctIndex: Int = 0,
    var buttonMoving: Boolean = false,
    var buttonPressMillis: Long = 0L,
    val buttonTapThresholdMillis: Int = 250,
    var buttonSizeIndex: Int = 0,
    var dirty: Boolean = false,
    var gapPercentage: Float = 0.25f,
    val screen: Dimensions = Dimensions(
        Extent.dpToExtent(density, configuration.screenWidthDp.dp),
        Extent.dpToExtent(density, configuration.screenHeightDp.dp)
    ),
    var boxOffset: BoxOffset = BoxOffset(),
    var box: Dimensions = Dimensions(
        Extent(),
        Extent()
    ),
    var first: Boolean = true
) {
    fun getButtonGapPercentage() = ButtonParameters.buttonGapPercentage[buttonGapPctIndex]
    fun getButtonHeightDp() = ButtonParameters.buttonHeightsDp[buttonSizeIndex]
    fun getButtonHeightPx() = ButtonParameters.buttonHeightsPx[buttonSizeIndex]
    fun getButtonWidthDp() = ButtonParameters.buttonWidthsDp[buttonSizeIndex]
    fun getButtonWidthPx() = ButtonParameters.buttonWidthsPx[buttonSizeIndex]
    fun getScreenButtonColumn() = ButtonParameters.screenButtonColumns[buttonSizeIndex]
    fun getScreenButtonRow() = ButtonParameters.screenButtonRows[buttonSizeIndex]
    fun getButtonRoundedSize() = ButtonParameters.buttonRoundedSizes[buttonSizeIndex]
    fun getButtonTextSize() = ButtonParameters.buttonTextSizes[buttonSizeIndex]
    fun extentFromDp(dp: Dp) = Extent(dp, with(density) { dp.toPx() })
    fun extentFromPx(px: Float) = Extent(with(density) { px.toDp() }, px)

    //fun init(configuration: Configuration, density: Density) {
    init {
        ButtonParameters.init(density)
        if (first) {
            boxOffset.x = screen.width.px / 2 - getButtonWidthPx() / 2
            boxOffset.y = 675 - getButtonHeightPx() / 2
            first = false
        }
    }

    // common
    val setButtonSizeIndex: (Int) -> Unit =
        { newButtonSizeIndex ->
            buttonSizeIndex = newButtonSizeIndex
            boxOffset.x = min(
                boxOffset.x,
                box.width.px - getButtonWidthPx()
            )
            boxOffset.y = min(
                boxOffset.y,
                box.height.px - getButtonHeightPx()
            )
            dirty = true
        }
    val setGapPctIndex: (Int) -> Unit =
        { newButtonGapPctIndex ->
            buttonGapPctIndex = newButtonGapPctIndex
            dirty = true
        }
    val setPointerEventState: (PointerEventState) -> Unit =
        { newPointerEventState ->
            pointerEventState = newPointerEventState
            dirty = true
        }

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
        dirty = true
    }
    val setButtonPress: (Long) -> Unit =
        { newButtonPressMillis ->
            buttonPressMillis = newButtonPressMillis
            dirty = true
        }
    val setButtonRelease: (Long) -> Boolean =
        { buttonReleaseMillis ->
            if (buttonReleaseMillis - buttonPressMillis < buttonTapThresholdMillis) {
                buttonPressMillis = 0
                dirty = true
                true
            } else false
        }
    val setChangePosition: (PointerInputChange) -> Unit =
        { change ->
            // Calculate the difference from the previous position
            val deltaX = change.position.x - previousPosition.x
            val deltaY = change.position.y - previousPosition.y


            // Update boxOffset.x and offsetY based on the drag amount (or delta from previous)
            boxOffset.x += deltaX
            boxOffset.x = max(boxOffset.x, 0f)
            boxOffset.x = min(
                boxOffset.x,
                box.width.px - getButtonWidthPx()
            )
            boxOffset.y += deltaY
            boxOffset.y = max(boxOffset.y, 0f)
            boxOffset.y = min(
                boxOffset.y,
                box.height.px - getButtonHeightPx()
            )
            // Update previous position for the next onDrag call
            previousPosition = change.position
            change.consume()
            dirty = true
        }
    val setFinalPosition: () -> Unit =
        {
            previousPosition = Offset.Zero
            dirty = true
        }
    val setFirstPosition: (Offset) -> Unit =
        { startOffset ->
            previousPosition = startOffset
            dirty = true
        }
    val setShowDialog: () -> Unit =
        {
            showDialog = true
            dirty = true
        }
}
