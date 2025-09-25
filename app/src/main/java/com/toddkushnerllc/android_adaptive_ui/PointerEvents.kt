package com.toddkushnerllc.android_adaptive_ui

import android.util.Log
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType

object PointerEvents {
    fun log(message: String) = Log.d("LogPointerEvents", message)
    val onBoxPointerEvent: (
        PointerEvent,
        State,
        stateChanged: () -> Unit
    ) -> Unit =
        { event,
          state,
          stateChanged
            ->
            log("box ${event.type}, ${event.changes.first().position}, ${event.changes.first().pressure}, ${event.changes.first().uptimeMillis}")
            when (event.type) {
                PointerEventType.Press -> {
                    if (state.buttonMoving) {
                        state.setFirstPosition(event.changes.first().position)
                    }
                    when (state.pointerEventState) {
                        PointerEventState.START -> {
                            state.setPointerEventState(PointerEventState.BOX_PRESS)
                        }

                        PointerEventState.BUTTON_PRESS -> {
                            state.setPointerEventState(PointerEventState.BUTTON_BOX_PRESS)
                        }

                        PointerEventState.BUTTON_RELEASE -> {
                            //setPointerEventState(PointerEventState.BUTTON_BOX_PRESS)
                        }

                        PointerEventState.BOX_PRESS -> {
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state $state.pointerEventState")
                            state.setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Move -> {
                    if (state.buttonMoving) {
                        state.setChangePosition(event.changes.first())
                        stateChanged()
                    }
                }

                PointerEventType.Release -> {
                    if (state.buttonMoving) {
                        state.setFinalPosition()
                        state.setButtonMoving(false)
                    }
                    when (state.pointerEventState) {
                        PointerEventState.BOX_PRESS -> {
                            // pointerEventState = PointerEventState.BOX_TAP // PointerEventState.BOX_RELEASE
                            state.setPointerEventState(PointerEventState.START)
                            state.incrementButtonSize()
                            stateChanged()
                        }

                        PointerEventState.BUTTON_RELEASE -> {
                            //setPointerEventState(PointerEventState.BUTTON_BOX_RELEASE)
                            //setPointerEventState(PointerEventState.BUTTON_TAP)
                            state.setPointerEventState(PointerEventState.START)
                            if (state.setButtonRelease(event.changes.first().uptimeMillis)) {
                                if (state.buttonSizeIndex > (ButtonParameters.buttonSizeIndexMax / 2)) {
                                    state.setShowDialog()
                                    stateChanged()
                                } else {
                                    state.decrementButtonSize()
                                    stateChanged()
                                }
                            }
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state $state.pointerEventState")
                            state.setPointerEventState(PointerEventState.START)
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
        State,
        stateChanged: () -> Unit
    ) -> Unit =
        { event,
          state,
          stateChanged
            ->
            log("button ${event.type}, ${event.changes.first().position}, ${event.changes.first().pressure}, ${event.changes.first().uptimeMillis}                               ")
            when (event.type) {
                PointerEventType.Press -> {
                    state.setButtonMoving(true)
                    when (state.pointerEventState) {
                        PointerEventState.START -> {
                            state.setPointerEventState(PointerEventState.BUTTON_PRESS)
                            state.setButtonPress(event.changes.first().uptimeMillis)
                        }

                        PointerEventState.BUTTON_PRESS -> {
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state $state.pointerEventState")
                            state.setPointerEventState(PointerEventState.BUTTON_PRESS)
                        }
                    }
                }

                PointerEventType.Release -> {
                    when (state.pointerEventState) {
                        PointerEventState.BUTTON_BOX_PRESS -> {
                            state.setPointerEventState(PointerEventState.BUTTON_RELEASE)
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state $state.pointerEventState")
                            state.setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Move -> {}
                else ->
                    log("unexpected button event type ${event.type}")
            }
        }
}
