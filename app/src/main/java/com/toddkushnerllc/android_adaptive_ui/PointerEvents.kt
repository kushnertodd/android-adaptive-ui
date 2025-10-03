package com.toddkushnerllc.android_adaptive_ui

import android.util.Log
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType

object PointerEvents {
    fun log(message: String) = Log.d("LogPointerEvents", message)
    val onBoxPointerEvent: (
        PointerEvent,
        State
    ) -> Unit =
        { event,
          state
            ->
            log("box    ${event.type}, ${state.pointerEventState}, ${event.changes.first().position}, ${event.changes.first().pressure}, ${event.changes.first().uptimeMillis}")
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

                        else -> {
                            log("unexpected box event type ${event.type} in state ${state.pointerEventState}")
                            state.setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Move -> {
                    if (state.buttonMoving) {
                        state.setChangePosition(event.changes.first())
                    }
                    when (state.pointerEventState) {
                        PointerEventState.BOX_PRESS -> {
                            state.setPointerEventState(PointerEventState.BOX_MOVE)
                        }

                        PointerEventState.BOX_MOVE -> {
                        }

                        PointerEventState.BUTTON_MOVE -> {
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state ${state.pointerEventState}")
                            state.setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Release -> {
                    if (state.buttonMoving) {
                        state.setFinalPosition()
                        state.setButtonMoving(false)
                    }
                    when (state.pointerEventState) {
                        PointerEventState.BOX_PRESS -> {
                            state.setPointerEventState(PointerEventState.START)
                            state.incrementButtonSize()
                        }

                        PointerEventState.BOX_MOVE -> {
                            state.setPointerEventState(PointerEventState.START)
                            state.incrementButtonSize()
                        }

                        PointerEventState.BUTTON_RELEASE -> {
                            state.setPointerEventState(PointerEventState.START)
                            if (state.setButtonRelease(event.changes.first().uptimeMillis)) {
                                if (state.buttonSizeIndex > (ButtonParameters.buttonSizeIndexMax / 2)) {
                                    state.setShowDialog()
                                } else {
                                    //state.decrementButtonSize()
                                }
                            }
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state ${state.pointerEventState}")
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
        Int,
        PointerEvent,
        State
    ) -> Unit =
        { buttonNumber, event, state
            ->
            log("button ${buttonNumber}, ${event.type}, ${state.pointerEventState}, ${event.changes.first().position}, pressure ${event.changes.first().pressure}, uptime ${event.changes.first().uptimeMillis}                               ")
            when (event.type) {
                PointerEventType.Press -> {
                    state.setButtonMoving(true)
                    when (state.pointerEventState) {
                        PointerEventState.START -> {
                            state.setPointerEventState(PointerEventState.BUTTON_PRESS)
                            state.setButtonPress(event.changes.first().uptimeMillis)
                        }

                        else -> {
                            log("unexpected button ${buttonNumber} event type ${event.type} in state ${state.pointerEventState}")
                            state.setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Move -> {
                    when (state.pointerEventState) {
                        PointerEventState.BUTTON_BOX_PRESS -> {
                            state.setPointerEventState(PointerEventState.BUTTON_MOVE)
                        }

                        PointerEventState.BUTTON_MOVE -> {
                        }

                        else -> {
                            log("unexpected button ${buttonNumber} event type ${event.type} in state ${state.pointerEventState}")
                            state.setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Release -> {
                    when (state.pointerEventState) {
                        PointerEventState.BUTTON_BOX_PRESS -> {
                            state.setPointerEventState(PointerEventState.BUTTON_RELEASE)
                        }

                        PointerEventState.BUTTON_MOVE -> {
                            state.setPointerEventState(PointerEventState.BUTTON_RELEASE)
                        }

                        else -> {
                            log("unexpected button ${buttonNumber} event type ${event.type} in state ${state.pointerEventState}")
                            state.setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                else ->
                    log("unexpected button ${buttonNumber} event type ${event.type}")
            }
        }
}
