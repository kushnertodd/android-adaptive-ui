package com.toddkushnerllc.android_adaptive_ui

import android.util.Log
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType

object PointerEvents {
    fun log(message: String) = Log.d("LogPointerEvents", message)
    val onBoxPointerEvent: (
        PointerEvent,
        State,
        stateChanged: (State) -> Unit,
        changeCounter: () -> Unit
    ) -> Unit =
        { event,
          state,
          stateChanged,
          changeCounter
            ->
            log("box    ${event.type}, ${state.getPointerEventState()}, (${event.changes.first().position.x},${event.changes.first().position.y}), ${event.changes.first().pressure}, ${event.changes.first().uptimeMillis}")
            when (event.type) {
                PointerEventType.Press -> {
                    if (state.buttonMoving) {
                        state.setFirstPosition(event.changes.first().position)
                    }
                    when (state.getPointerEventState()) {
                        PointerEventState.START -> {
                            state.setPointerEventState(PointerEventState.BOX_PRESS)
                        }

                        PointerEventState.BOX_PRESS -> {}

                        PointerEventState.BUTTON_PRESS -> {
                            state.setPointerEventState(PointerEventState.BUTTON_BOX_PRESS)
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state ${state.getPointerEventState()}")
                            state.setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Move -> {
                    if (state.buttonMoving) {
                        state.setChangePosition(event.changes.first())
                    }
                    when (state.getPointerEventState()) {
                        PointerEventState.BOX_PRESS -> {
                            state.setPointerEventState(PointerEventState.BOX_MOVE)
                        }

                        PointerEventState.BOX_MOVE -> {
                        }

                        PointerEventState.BUTTON_MOVE -> {
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state ${state.getPointerEventState()}")
                            state.setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Release -> {
                    if (state.buttonMoving) {
                        state.setFinalPosition()
                        state.setButtonMoving(false)
                    }
                    when (state.getPointerEventState()) {
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
                            //if (state.setButtonRelease(event.changes.first().uptimeMillis)) {
                            //if (state.buttonSizeIndex > (ButtonParameters.buttonSizeIndexMax / 2)) {
                            //    state.setShowDialog()
                            //} else {
                            //  if (state.getButtonId() > 3)
                            //      state.decrementButtonSize()
                            //  else {
                            // TODO: kludge to get screen to reconstitute
                            //state.incrementButtonSize()
                            //state.decrementButtonSize()
                            state.launchDeskClock(
                                state.getButtonId(),
//                                arrayOf("kushnertodd@gmail.com"),
//                                "from adaptive UI",
                                state
                            )
                            //}
                        }

                        else -> {
                            log("unexpected box event type ${event.type} in state ${state.getPointerEventState()}")
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
        String,
        PointerEvent,
        State,
        stateChanged: (State) -> Unit
    ) -> Unit =
        {
                buttonNumber,
                label,
                event,
                state,
                stateChanged,
            ->
            log("button ${buttonNumber}, label ${label}, ${event.type}, ${state.getPointerEventState()}, (${event.changes.first().position.x},${event.changes.first().position.y}), pressure ${event.changes.first().pressure}, uptime ${event.changes.first().uptimeMillis}                               ")
            state.setButtonId(buttonNumber)
            when (event.type) {
                PointerEventType.Press -> {
                    state.setButtonMoving(true)
                    when (state.getPointerEventState()) {
                        PointerEventState.START -> {
                            state.setPointerEventState(PointerEventState.BUTTON_PRESS)
                            state.setButtonPress(event.changes.first().uptimeMillis)
                        }

                        else -> {
                            log("unexpected button $buttonNumber event type ${event.type} in state ${state.getPointerEventState()}")
                            state.setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Move -> {
                    when (state.getPointerEventState()) {
                        PointerEventState.BUTTON_BOX_PRESS -> {
                            state.setPointerEventState(PointerEventState.BUTTON_MOVE)
                        }

                        PointerEventState.BUTTON_MOVE -> {
                        }

                        else -> {
                            log("unexpected button $buttonNumber event type ${event.type} in state ${state.getPointerEventState()}")
                            state.setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                PointerEventType.Release -> {
                    when (state.getPointerEventState()) {
                        PointerEventState.BUTTON_BOX_PRESS -> {
                            state.setPointerEventState(PointerEventState.BUTTON_RELEASE)
                        }

                        PointerEventState.BUTTON_MOVE -> {
                            state.setPointerEventState(PointerEventState.BUTTON_RELEASE)
                        }

                        else -> {
                            log("unexpected button $buttonNumber event type ${event.type} in state ${state.getPointerEventState()}")
                            state.setPointerEventState(PointerEventState.START)
                        }
                    }
                }

                else ->
                    log("unexpected button $buttonNumber event type ${event.type}")
            }
        }
}
