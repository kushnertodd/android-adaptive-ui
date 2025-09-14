package com.toddkushnerllc.com.android_adaptive_ui

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputChange

object PointerEvents {

    fun log(message: String) = Log.d("LogPointerEvents", message)

    val onBoxPointerEvent: (
        PointerEvent,
        PointerEventState,
        Int,
        setPointerEventState: (PointerEventState) -> Unit,
        decrementButtonSize: () -> Unit,
        incrementButtonSize: () -> Unit,
        setShowDialog: () -> Unit,
        setFirstPosition: (Offset) -> Unit,
        setChangePosition: (PointerInputChange) -> Unit,
        setFinalPosition: () -> Unit,
        setIgnoreBoxEvent: (Boolean) -> Unit, // TODO: unnecessary
        testIgnoreBoxEvent: () -> Boolean, // TODO: unnecessary
        setButtonMoving: (Boolean) -> Unit,
        testButtonMoving: () -> Boolean,
        setButtonRelease: (Long) -> Boolean
    ) -> Unit =
        { event,
          pointerEventState,
          buttonSizeIndex,
          setPointerEventState,
          decrementButtonSize,
          incrementButtonSize,
          setShowDialog,
          setFirstPosition,
          setChangePosition,
          setFinalPosition,
          setIgnoreBoxEvent,  // TODO: unnecessary
          testIgnoreBoxEvent,  // TODO: unnecessary
          setButtonMoving,
          testButtonMoving,
          setButtonRelease ->
            // Process the PointerEvent here
            log("box ${event.type}, ${event.changes.first().position}, ${event.changes.first().pressure}, ${event.changes.first().uptimeMillis}")
            when (event.type) {
                PointerEventType.Press -> {
                    if (testButtonMoving())
                        setFirstPosition(event.changes.first().position)
                    when (pointerEventState) {
                        PointerEventState.START -> {
                            setPointerEventState(PointerEventState.BOX_PRESS)
                        }

                        PointerEventState.BUTTON_PRESS -> {
                            setPointerEventState(PointerEventState.BUTTON_BOX_PRESS)
                        }

                        PointerEventState.BUTTON_RELEASE -> {
                            //setPointerEventState(PointerEventState.BUTTON_BOX_PRESS)
                        }

                        PointerEventState.BOX_PRESS -> {
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state $pointerEventState")
                            setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Move -> {
                    if (testButtonMoving())
                        setChangePosition(event.changes.first())
                }

                PointerEventType.Release -> {
                    if (testButtonMoving()) {
                        setFinalPosition()
                        setButtonMoving(false)
                    }
                    when (pointerEventState) {
                        PointerEventState.BOX_PRESS -> {
                            // pointerEventState = PointerEventState.BOX_TAP // PointerEventState.BOX_RELEASE
                            setPointerEventState(PointerEventState.START)
                            // if (!testIgnoreBoxEvent()) { // TODO: unnecessary
                            incrementButtonSize()
                            // } else setIgnoreBoxEvent(false) // TODO: unnecessary
                        }

                        PointerEventState.BUTTON_RELEASE -> {
                            //setPointerEventState(PointerEventState.BUTTON_BOX_RELEASE)
                            //setPointerEventState(PointerEventState.BUTTON_TAP)
                            setPointerEventState(PointerEventState.START)
                            if (setButtonRelease(event.changes.first().uptimeMillis)) {
                                if (buttonSizeIndex > (ButtonParameters.buttonSizeIndexMax / 2))
                                    setShowDialog()
                                else {
                                    decrementButtonSize()
                                }
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

    val onButtonPointerEvent: (
        PointerEvent,
        PointerEventState,
        Int,
        setPointerEventState: (PointerEventState) -> Unit,
        setButtonMoving: (Boolean) -> Unit,
        setButtonPress: (Long) -> Unit
    ) -> Unit =
        { event,
          pointerEventState,
          buttonSizeIndex,
          setPointerEventState,
          setButtonMoving,
          setButtonPress ->
            // Process the PointerEvent here
            log("button ${event.type}, ${event.changes.first().position}, ${event.changes.first().pressure}, ${event.changes.first().uptimeMillis}                               ")
            when (event.type) {
                PointerEventType.Press -> {
                    setButtonMoving(true)
                    when (pointerEventState) {
                        PointerEventState.START -> {
                            setPointerEventState(PointerEventState.BUTTON_PRESS)
                            setButtonPress(event.changes.first().uptimeMillis)
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
