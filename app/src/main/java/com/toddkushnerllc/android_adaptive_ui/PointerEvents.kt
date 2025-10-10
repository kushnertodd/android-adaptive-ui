package com.toddkushnerllc.android_adaptive_ui

import android.util.Log
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType

object PointerEvents {
    fun log(message: String) = Log.d("LogPointerEvents", message)
    val onBoxPointerEvent: (
        PointerEvent,
        State,
        PointerEventState,
        stateChanged: (State) -> Unit,
        setButtonSizeIndex: (Int) -> Unit,
        setPointerEventState: (PointerEventState) -> Unit,
        decrementButtonSize: () -> Unit,
        incrementButtonSize: () -> Unit
    ) -> Unit =
        { event,
          state,
          pointerEventState,
          stateChanged,
          setButtonSizeIndex,
          setPointerEventState,
          decrementButtonSize,
          incrementButtonSize
            ->
            log("box    ${event.type}, ${pointerEventState}, ${event.changes.first().position}, ${event.changes.first().pressure}, ${event.changes.first().uptimeMillis}")
            when (event.type) {
                PointerEventType.Press -> {
                    if (state.buttonMoving) {
                        state.setFirstPosition(event.changes.first().position)
                    }
                    when (pointerEventState) {
                        PointerEventState.START -> {
                            setPointerEventState(PointerEventState.BOX_PRESS)
                        }

                        PointerEventState.BOX_PRESS -> {}

                        PointerEventState.BUTTON_PRESS -> {
                            setPointerEventState(PointerEventState.BUTTON_BOX_PRESS)
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state ${pointerEventState}")
                            setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Move -> {
                    if (state.buttonMoving) {
                        state.setChangePosition(event.changes.first())
                    }
                    when (pointerEventState) {
                        PointerEventState.BOX_PRESS -> {
                            setPointerEventState(PointerEventState.BOX_MOVE)
                        }

                        PointerEventState.BOX_MOVE -> {
                        }

                        PointerEventState.BUTTON_MOVE -> {
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state ${pointerEventState}")
                            setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Release -> {
                    if (state.buttonMoving) {
                        state.setFinalPosition()
                        state.setButtonMoving(false)
                    }
                    when (pointerEventState) {
                        PointerEventState.BOX_PRESS -> {
                            setPointerEventState(PointerEventState.START)
                            incrementButtonSize()
                        }

                        PointerEventState.BOX_MOVE -> {
                            setPointerEventState(PointerEventState.START)
                            incrementButtonSize()
                        }

                        PointerEventState.BUTTON_RELEASE -> {
                            setPointerEventState(PointerEventState.START)
                            //if (state.setButtonRelease(event.changes.first().uptimeMillis)) {
                            //if (state.buttonSizeIndex > (ButtonParameters.buttonSizeIndexMax / 2)) {
                            //    state.setShowDialog()
                            //} else {
                            decrementButtonSize()
                            //}
                            //}
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state ${pointerEventState}")
                            setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Move -> {}
                else ->
                    log("unexpected box event type ${event.type}")
            }
            //if (state.dirty)
                stateChanged(state)
        }
    val onButtonPointerEvent: (
        Int,
        PointerEvent,
        State,
        PointerEventState,
        stateChanged: (State) -> Unit,
        setButtonSizeIndex: (Int) -> Unit,
        setPointerEventState: (PointerEventState) -> Unit
    ) -> Unit =
        { buttonNumber, event, state, pointerEventState,
          stateChanged,
          setButtonSizeIndex,
          setPointerEventState
            ->
            log("button ${buttonNumber}, ${event.type}, ${pointerEventState}, ${event.changes.first().position}, pressure ${event.changes.first().pressure}, uptime ${event.changes.first().uptimeMillis}                               ")
            when (event.type) {
                PointerEventType.Press -> {
                    state.setButtonMoving(true)
                    when (pointerEventState) {
                        PointerEventState.START -> {
                            setPointerEventState(PointerEventState.BUTTON_PRESS)
                            state.setButtonPress(event.changes.first().uptimeMillis)
                        }

                        else -> {
                            log("unexpected button ${buttonNumber} event type ${event.type} in state ${pointerEventState}")
                            setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Move -> {
                    when (pointerEventState) {
                        PointerEventState.BUTTON_BOX_PRESS -> {
                            setPointerEventState(PointerEventState.BUTTON_MOVE)
                        }

                        PointerEventState.BUTTON_MOVE -> {
                        }

                        else -> {
                            log("unexpected button ${buttonNumber} event type ${event.type} in state ${pointerEventState}")
                            setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Release -> {
                    when (pointerEventState) {
                        PointerEventState.BUTTON_BOX_PRESS -> {
                            setPointerEventState(PointerEventState.BUTTON_RELEASE)
                        }

                        PointerEventState.BUTTON_MOVE -> {
                            setPointerEventState(PointerEventState.BUTTON_RELEASE)
                        }

                        else -> {
                            log("unexpected button ${buttonNumber} event type ${event.type} in state ${pointerEventState}")
                            setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                else ->
                    log("unexpected button ${buttonNumber} event type ${event.type}")
            }
            //if (state.dirty)
                stateChanged(state)
        }
}
