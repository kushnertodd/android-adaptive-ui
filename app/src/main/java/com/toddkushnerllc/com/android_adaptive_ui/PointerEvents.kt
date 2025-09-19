package com.toddkushnerllc.com.android_adaptive_ui

import android.util.Log
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType

object PointerEvents {

    fun log(message: String) = Log.d("LogPointerEvents", message)

    val onBoxPointerEvent: (
        PointerEvent,
        // PointerEventState,
        // Int,
        State,
        stateChanged: () -> Unit
        /*
                setPointerEventState: (PointerEventState) -> Unit,
                decrementButtonSize: () -> Unit,
                incrementButtonSize: () -> Unit,
                setShowDialog: () -> Unit,
                setFirstPosition: (Offset) -> Unit,
                setChangePosition: (PointerInputChange) -> Unit,
                setFinalPosition: () -> Unit,
                setButtonMoving: (Boolean) -> Unit,
                testButtonMoving: () -> Boolean,
                setButtonRelease: (Long) -> Boolean
        */
    ) -> Unit =
        { event,
            //  pointerEventState,
//          buttonSizeIndex,
          state,
          stateChanged
            /*          setPointerEventState
                      decrementButtonSize,
                      incrementButtonSize,
                      setShowDialog,
                      setFirstPosition,
                      setChangePosition,
                      setFinalPosition,
                      setButtonMoving,
                      testButtonMoving,
                      setButtonRelease */
            ->
            // Process the PointerEvent here
            log("box ${event.type}, ${event.changes.first().position}, ${event.changes.first().pressure}, ${event.changes.first().uptimeMillis}")
            when (event.type) {
                PointerEventType.Press -> {
                    if (state.testButtonMoving())
                        state.setFirstPosition(event.changes.first().position)
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
                    if (state.testButtonMoving())
                        state.setChangePosition(event.changes.first())
                }

                PointerEventType.Release -> {
                    if (state.testButtonMoving()) {
                        state.setFinalPosition()
                        state.setButtonMoving(false)
                    }
                    when (state.pointerEventState) {
                        PointerEventState.BOX_PRESS -> {
                            // pointerEventState = PointerEventState.BOX_TAP // PointerEventState.BOX_RELEASE
                            state.setPointerEventState(PointerEventState.START)
                            state.incrementButtonSize()
                        }

                        PointerEventState.BUTTON_RELEASE -> {
                            //setPointerEventState(PointerEventState.BUTTON_BOX_RELEASE)
                            //setPointerEventState(PointerEventState.BUTTON_TAP)
                            state.setPointerEventState(PointerEventState.START)
                            if (state.setButtonRelease(event.changes.first().uptimeMillis)) {
                                if (state.buttonSizeIndex > (ButtonParameters.buttonSizeIndexMax / 2))
                                    state.setShowDialog()
                                else {
                                    state.decrementButtonSize()
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
            stateChanged()
        }

    val onButtonPointerEvent: (
        PointerEvent,
        //PointerEventState,
        //Int,
        State,
        stateChanged: () -> Unit
        /*
                setPointerEventState: (PointerEventState) -> Unit,
                setButtonMoving: (Boolean) -> Unit,
                setButtonPress: (Long) -> Unit
        */
    ) -> Unit =
        { event,
            // pointerEventState,
            //buttonSizeIndex,
          state,
          stateChanged
            /*          setPointerEventState,
                      setButtonMoving,
                      setButtonPress */
            ->
            // Process the PointerEvent here
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
            stateChanged()
        }
}
