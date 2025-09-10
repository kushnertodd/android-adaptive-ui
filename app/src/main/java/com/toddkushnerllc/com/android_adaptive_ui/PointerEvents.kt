package com.toddkushnerllc.com.android_adaptive_ui

import android.util.Log
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object PointerEvents {


    /*
    •	The pointerInput modifier. You pass it one or more keys. When the value of one of those keys changes, the modifier content lambda is re-executed. The sample passes an optional filter to the composable. If the value of that filter changes, the pointer event handler should be re-executed to make sure the right events are logged.
    •	awaitPointerEventScope creates a coroutine scope that can be used to wait for pointer events.
    •	awaitPointerEvent suspends the coroutine until a next pointer event occurs.

    */

    /*
        @Composable
        fun LogPointerEvents(
            buttonWidth: Dp, buttonHeight: Dp, buttonPadding: Dp, filter: PointerEventType? = null
        ) {
            var log by remember { mutableStateOf("") }
            var pointerEventState by remember { mutableStateOf(PointerEventState.START) }
            var buttonPadding by remember { mutableStateOf(16.dp) }
            var buttonSizeIndex by remember { mutableStateOf(0) }
    */
    val buttonSizeIndexMax = 14
    val buttonWidths = arrayOf(
        150.dp,
        165.dp,
        180.dp,
        195.dp,
        210.dp,
        225.dp,
        240.dp,
        255.dp,
        270.dp,
        285.dp,
        300.dp,
        315.dp,
        330.dp,
        345.dp,
        360.dp
    )
    val buttonHeights = arrayOf(
        120.dp,
        132.dp,
        144.dp,
        156.dp,
        168.dp,
        180.dp,
        192.dp,
        204.dp,
        216.dp,
        228.dp,
        240.dp,
        252.dp,
        264.dp,
        276.dp,
        288.dp
    )
    val buttonTextSizes = arrayOf(
        15.sp,
        18.sp,
        21.sp,
        24.sp,
        27.sp,
        30.sp,
        33.sp,
        36.sp,
        39.sp,
        42.sp,
        45.sp,
        48.sp,
        51.sp,
        54.sp,
        57.sp
    )

    fun log(message: String) = Log.d("LogPointerEvents", message)

    val onBoxPointerEvent: (PointerEvent, PointerEventState, Int, Int, setPointerEventState: (PointerEventState) -> Unit, setButtonSizeIndex: (Int) -> Unit) -> Unit =
        { event, pointerEventState, buttonSizeIndex, buttonSizeIndexMax, setPointerEventState, setButtonSizeIndex ->
            // Process the PointerEvent here
            log("box ${event.type}, ${event.changes.first().position}, ${event.changes.first().pressure}, ${event.changes.first().uptimeMillis}                               ")
            when (event.type) {
                PointerEventType.Press -> {
                    when (pointerEventState) {
                        PointerEventState.START -> {
                            setPointerEventState(PointerEventState.BOX_PRESS)
                        }

                        PointerEventState.BUTTON_PRESS -> {
                            setPointerEventState(PointerEventState.BUTTON_BOX_PRESS)
                        }

                        PointerEventState.BOX_PRESS -> {
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state $pointerEventState")
                            setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Release -> {
                    when (pointerEventState) {
                        PointerEventState.BOX_PRESS -> {
                            // pointerEventState = PointerEventState.BOX_TAP // PointerEventState.BOX_RELEASE
                            setPointerEventState(PointerEventState.START)
                            if (buttonSizeIndex > 0)
                                setButtonSizeIndex(buttonSizeIndex - 1)
                        }

                        PointerEventState.BUTTON_RELEASE -> {
                            //pointerEventState = PointerEventState.BUTTON_TAP // PointerEventState.BUTTON_BOX_RELEASE
                            setPointerEventState(PointerEventState.START)
                            if (buttonSizeIndex < buttonSizeIndexMax)
                                setButtonSizeIndex(buttonSizeIndex + 1)
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state $pointerEventState")
                            setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Move -> {}

                else ->
                    log("unexpected box event type ${event.type}")
            }

        }

    val onButtonPointerEvent: (PointerEvent, PointerEventState, Int, setPointerEventState: (PointerEventState) -> Unit, setButtonSizeIndex: (Int) -> Unit) -> Unit =
        { event, pointerEventState, buttonSizeIndex, setPointerEventState, setButtonSizeIndex ->
            // Process the PointerEvent here
            //println("Pointer event occurred at: ${event.changes.first().position}")
            log("button ${event.type}, ${event.changes.first().position}, ${event.changes.first().pressure}, ${event.changes.first().uptimeMillis}                               ")
            when (event.type) {
                PointerEventType.Press -> {
                    when (pointerEventState) {
                        PointerEventState.START -> {
                            setPointerEventState(PointerEventState.BUTTON_PRESS)
                        }

                        PointerEventState.BUTTON_PRESS -> {
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state $pointerEventState")
                            setPointerEventState(PointerEventState.BUTTON_PRESS)
                        }
                    }
                }

                PointerEventType.Release -> {
                    when (pointerEventState) {
                        PointerEventState.BUTTON_BOX_PRESS -> {
                            setPointerEventState(PointerEventState.BUTTON_RELEASE)
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state $pointerEventState")
                            setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Move -> {}

                else ->
                    log("unexpected button event type ${event.type}")
            }

        }
}
