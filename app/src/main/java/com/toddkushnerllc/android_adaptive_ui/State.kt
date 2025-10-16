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
    val getButtonSizeIndex: () -> Int,
    val setButtonSizeIndex: (Int) -> Unit,
    val getPointerEventState: () -> PointerEventState,
    val setPointerEventState: (PointerEventState) -> Unit,
    val getBoxOffset: () -> BoxOffset,
    val setBoxOffset: (BoxOffset) -> Unit,
    val getBox: () -> Dimensions,
    val setBox: (Dimensions) -> Unit,
    val getButtonId: () -> Int,
    val setButtonId: (Int) -> Unit,
    val getCounter: () -> Int,
    val setCounter: (Int) -> Unit,
    val launchDeskClock: (Int, /*Array<String>, String,*/ State) -> Unit,
    var noClicks: Int = 0,
    var buttonPadding: Dp = 0.dp,
    var previousPosition: Offset = Offset.Zero,
    var showDialog: Boolean = false,
    var buttonGapPctIndex: Int = 0,
    var buttonMoving: Boolean = false,
    var buttonPressMillis: Long = 0L,
    val buttonTapThresholdMillis: Int = 250,
    var dirty: Boolean = false,
    var gapPercentage: Float = 0.25f,
    var screenCols: Int = 0,
    var screenRows: Int = 0,
    val screen: Dimensions = Dimensions(
        Extent.dpToExtent(density, configuration.screenWidthDp.dp),
        Extent.dpToExtent(density, configuration.screenHeightDp.dp - 250.dp)
    ),
    var apps: Apps = Apps(),
    var first: Boolean = true
) {
    fun recalculateOffsets() {
        val buttonWidthDp = ButtonParameters.buttonWidthsDp[getButtonSizeIndex()]
        val buttonWidthPx = ButtonParameters.buttonWidthsPx[getButtonSizeIndex()]
        val buttonHeightDp = ButtonParameters.buttonHeightsDp[getButtonSizeIndex()]
        val buttonHeightPx = ButtonParameters.buttonHeightsPx[getButtonSizeIndex()]
        screenCols = ButtonParameters.buttonWidthGapPctToColumns(
            density,
            configuration.screenWidthDp.dp,
            buttonWidthDp,
            gapPercentage
        )
        screenRows = ButtonParameters.buttonHeightGapPctToRows(
            density,
            screen.height.dp,
            buttonHeightDp,
            gapPercentage
        )
        val gapPct = ButtonParameters.columnsButtonWidthDpToGapPct(
            density,
            screen.width.dp,
            buttonWidthDp,
            screenCols
        )
        val buttonSizeIndex = getButtonSizeIndex() // for debugging
        val boxOffset = getBoxOffset()
        boxOffset.x = gapPct * buttonWidthPx
        boxOffset.y = buttonHeightPx / 2
        setBoxOffset(boxOffset)
    }

    init {
        ButtonParameters.init(density)
        if (first) {
            recalculateOffsets()
            first = false
        }
    }

    // common
    val setGapPctIndex: (Int) -> Unit =
        { newButtonGapPctIndex ->
            buttonGapPctIndex = newButtonGapPctIndex
            dirty = true
        }

    // other
    val decrementButtonSize: () -> Unit = {
        if (getButtonSizeIndex() > 0) {
            setButtonSizeIndex(getButtonSizeIndex() - 1)
            recalculateOffsets()
        }
    }
    val decrementButton: () -> Unit = {
        decrementButtonSize()
    }

    val incrementButtonSize: () -> Unit = {
        if (getButtonSizeIndex() < ButtonParameters.buttonSizeIndexMax - 1) {
            setButtonSizeIndex(getButtonSizeIndex() + 1)
            recalculateOffsets()
        }
    }
    val incrementButton: () -> Unit = {
        incrementButtonSize()
    }
    val maximizeButton: () -> Unit = {
        setButtonSizeIndex(ButtonParameters.buttonSizeIndexMax - 1)
        recalculateOffsets()
    }
    val minimizeButton: () -> Unit = {
        setButtonSizeIndex(0)
        recalculateOffsets()
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
            var boxOffset = getBoxOffset()
            var box = getBox()
            val buttonWidthPx = ButtonParameters.buttonWidthsPx[getButtonSizeIndex()]
            val buttonHeightPx = ButtonParameters.buttonHeightsPx[getButtonSizeIndex()]
            boxOffset.x += deltaX
            boxOffset.x = max(boxOffset.x, 0f)
            boxOffset.x = min(
                boxOffset.x,
                box.width.px - buttonWidthPx
            )
            boxOffset.y += deltaY
            boxOffset.y = max(boxOffset.y, 0f)
            boxOffset.y = min(
                boxOffset.y,
                box.height.px - buttonHeightPx
            )
            setBoxOffset(boxOffset)
            setBox(box)
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
