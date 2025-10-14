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
    val launchDeskClock: (Int, Array<String>, String, State) -> Unit,
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
    var apps:Apps = Apps(),
    //var button_id: Int = 0,
    var first: Boolean = true
) {
    fun recalculateOffsets() {
        screenCols = ButtonParameters.buttonWidthGapPctToColumns(
            density,
            configuration.screenWidthDp.dp,
            ButtonParameters.buttonWidthsDp[getButtonSizeIndex()],
            gapPercentage
        )
        screenRows = ButtonParameters.buttonHeightGapPctToRows(
            density,
            screen.height.dp,
            ButtonParameters.buttonHeightsDp[getButtonSizeIndex()],
            gapPercentage
        )
        val gapPct = ButtonParameters.columnsButtonWidthDpToGapPct(
            density,
            screen.width.dp,
            ButtonParameters.buttonWidthsDp[getButtonSizeIndex()],
            screenCols
        )
        var boxOffset = getBoxOffset()
        val buttonWidthPx = ButtonParameters.buttonWidthsPx[getButtonSizeIndex()]
        boxOffset.x = gapPct * buttonWidthPx
        boxOffset.y = ButtonParameters.buttonHeightsPx[getButtonSizeIndex()] / 2
        setBoxOffset(boxOffset)
    }

    init {
        ButtonParameters.init(density)
        if (first) {
            recalculateOffsets()
            first = false
        }
        apps.addApp(
            App(
                0,
                "Chrome",
                "com.android.chrome",
                "com.google.android.apps.chrome.IntentDispatcher",
                true
            )
        )
        apps.addApp(
            App(
                1,
                "Maps",
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity",
                true
            )
        )
        apps.addApp(
            App(
                2,
                "Calculator",
                "com.google.android.calculator",
                "com.android.calculator2.Calculator",
                true
            )
        )
        apps.addApp(
            App(
                3,
                "Calendar",
                "com.google.android.calendar",
                "com.android.calendar.AllInOneActivity",
                true
            )
        )
        apps.addApp(
            App(
                4,
                "Camera",
                "com.google.android.GoogleCamera",
                "com.android.camera.CameraLauncher",
                true
            )
        )
        apps.addApp(
            App(
                5,
                "Clock",
                "com.google.android.deskclock",
                "com.android.deskclock.DeskClock",
                true
            )
        )
        apps.addApp(
            App(
                6,
                "Phone",
                "com.google.android.dialer",
                "com.android.dialer.main.impl.MainActivity",
                true
            )
        )
        apps.addApp(
            App(
                7,
                "Docs",
                "com.google.android.apps.docs.editors.docs",
                "com.google.android.apps.docs.app.NewMainProxyActivity",
                true
            )
        )
        apps.addApp(
            App(
                8,
                "Podcasts",
                "com.podcast.podcasts",
                "fm.castbox.ui.main.MainActivity",
                true
            )
        )
        apps.addApp(
            App(
                9,
                "Sheets",
                "com.google.android.apps.docs.editors.sheets",
                "com.google.android.apps.docs.app.NewMainProxyActivity",
                true
            )
        )
        apps.addApp(
            App(
                10,
                "Slides",
                "com.google.android.apps.docs.editors.slides",
                "com.google.android.apps.docs.app.NewMainProxyActivity",
                true
            )
        )
        apps.addApp(
            App(
                11,
                "Lens",
                "com.google.ar.lens",
                "com.google.vr.apps.ornament.app.lens.LensLauncherActivity",
                true
            )
        )
        /*
        // works, no return
        //                    12 -> component = ComponentName(
        //                        "com.google.android.apps.magazines",
        //                        "com.google.apps.dots.android.app.activity.CurrentsStartActivity"
        //                    )
        //                    13 -> component = ComponentName(
        //                        "com.google.android.contacts",
        //                        "com.android.contacts.activities.PeopleActivity"
        //                    )
        //                    14 -> component = ComponentName(
        //                        "com.google.android.contacts",
        //                        "com.android.contacts.activities.PeopleActivity"
        //                    )
        //                    15 -> component = ComponentName(
        //                        "com.google.android.apps.walletnfcrel",
        //                        "com.google.commerce.tapandpay.android.wallet.WalletActivity"
        //                    )
        */

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
            boxOffset.x += deltaX
            boxOffset.x = max(boxOffset.x, 0f)
            boxOffset.x = min(
                boxOffset.x,
                box.width.px - ButtonParameters.buttonWidthsPx[getButtonSizeIndex()]
            )
            boxOffset.y += deltaY
            boxOffset.y = max(boxOffset.y, 0f)
            boxOffset.y = min(
                boxOffset.y,
                box.height.px - ButtonParameters.buttonHeightsPx[getButtonSizeIndex()]
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
