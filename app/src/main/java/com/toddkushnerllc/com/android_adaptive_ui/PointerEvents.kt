package com.toddkushnerllc.com.android_adaptive_ui

import android.util.Log
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType

object PointerEvents {

    fun log(message: String) = Log.d("LogPointerEvents", message)
    var ignoreBoxEvent = false

    val onBoxPointerEvent: (PointerEvent, PointerEventState, Int, setPointerEventState: (PointerEventState) -> Unit, decrementButtonSize: () -> Unit, incrementButtonSize: () -> Unit, setShowDialog: () -> Unit) -> Unit =
        { event, pointerEventState, buttonSizeIndex, setPointerEventState, decrementButtonSize, incrementButtonSize, setShowDialog ->
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
                            if (!ignoreBoxEvent) {
                                incrementButtonSize()
                            } else ignoreBoxEvent = false
                        }

                        PointerEventState.BUTTON_RELEASE -> {
                            //setPointerEventState(PointerEventState.BUTTON_BOX_RELEASE)
                            //setPointerEventState(PointerEventState.BUTTON_TAP)
                            //setPointerEventState(PointerEventState.START)
                            if (buttonSizeIndex > (ButtonParameters.buttonSizeIndexMax / 2))
                                setShowDialog()
                            else {
                                decrementButtonSize()
                            }
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
