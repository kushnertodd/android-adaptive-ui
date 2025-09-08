package com.toddkushnerllc.com.android_pointer_demo

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

/*
https://developer.android.com/jetpack/compose/touch-input/pointer-input
https://developer.android.com/jetpack/compose/touch-input/pointer-input/tap-and-press
https://developer.android.com/jetpack/compose/touch-input/pointer-input/scroll
https://developer.android.com/jetpack/compose/touch-input/pointer-input/drag-swipe-fling
https://developer.android.com/jetpack/compose/touch-input/pointer-input/multi-touch
https://developer.android.com/reference/kotlin/androidx/compose/foundation/gestures/package-summary
*/
val TAG = "DrawingApp"
fun LogGesture(label: String, action: String, start: Offset?, end: Offset?) {
    Log.d(TAG, "$label $action from $start to $end")
}

@Composable
fun DrawingApp() {
    // val action: MutableState<Boolean> = remember { mutableStateOf(false) }
    var press: Offset? = null
    var lastDrag: Offset? = null
    val drawModifier = Modifier
        .padding(8.dp)
        .shadow(1.dp)
        .fillMaxWidth()
        .background(Color.White)
        // https://developer.android.com/reference/kotlin/androidx/compose/foundation/gestures/package-summary#(androidx.compose.ui.input.pointer.PointerInputScope).detectDragGestures(kotlin.Function1,kotlin.Function0,kotlin.Function0,kotlin.Function2)
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { Log.d(TAG, "detectDragGestures onDragStart $it") },
                onDragEnd = {
                    Log.d(TAG, "detectDragGestures onDragEnd")
                    LogGesture("detectDragGestures", "drag", press, lastDrag)
                },
                onDragCancel = { Log.d(TAG, "detectDragGestures onDragCancel") },
                onDrag = { change, dragAmount ->
                    lastDrag = change.position
                    Log.d(
                        TAG,
                        "detectDragGestures onDrag change.position ${change.position}"
                    )
                }
            )
        }
        .pointerInput(Unit) {
            detectVerticalDragGestures(
                onDragStart = { Log.d(TAG, "detectVerticalDragGestures onDragStart $it") },
                onDragEnd = {
                    Log.d(TAG, "detectVerticalDragGestures onDragEnd")
                    LogGesture("detectVerticalDragGestures", "vertical drag", press, lastDrag)
                },
                onDragCancel = { Log.d(TAG, "detectVerticalDragGestures onDragCancel") },
                onVerticalDrag = { change, dragAmount ->
                    lastDrag = change.position
                    Log.d(
                        TAG,
                        "detectVerticalDragGestures onVerticalDrag change.position ${change.position}"
                    )
                }
            )
        }
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragStart = { Log.d(TAG, "detectHorizontalDragGestures onDragStart $it") },
                onDragEnd = {
                    Log.d(TAG, "detectHorizontalDragGestures onDragEnd")
                    LogGesture("detectHorizontalDragGestures", "horizontal drag", press, lastDrag)
                },
                onDragCancel = { Log.d(TAG, "detectHorizontalDragGestures onDragCancel") },
                onHorizontalDrag = { change, dragAmount ->
                    lastDrag = change.position
                    Log.d(
                        TAG,
                        "detectHorizontalDragGestures onHorizontalDrag change.position ${change.position}"
                    )
                }
            )
        }
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDragStart = {
                    lastDrag = it
                    Log.d(TAG, "detectDragGesturesAfterLongPress onDragStart $it")
                },
                onDragEnd = {
                    Log.d(TAG, "detectDragGesturesAfterLongPress onDragEnd")
                },
                onDragCancel = {
                    Log.d(TAG, "detectDragGesturesAfterLongPress onDragCancel")
                    LogGesture("detectDragGesturesAfterLongPress", "long press", press, lastDrag)
                },
                onDrag = { change, dragAmount ->
                    Log.d(
                        TAG,
                        "detectDragGesturesAfterLongPress change.position ${change.position}"
                    )
                }
            )
        }
        .pointerInput(Unit) {
            // https://developer.android.com/reference/kotlin/androidx/compose/foundation/gestures/package-summary#(androidx.compose.ui.input.pointer.PointerInputScope).detectTapGestures(kotlin.Function1,kotlin.Function1,kotlin.coroutines.SuspendFunction2,kotlin.Function1)
            detectTapGestures(
                onTap = {
                    Log.d(TAG, "detectTapGestures onTap $it")
                    LogGesture("detectTapGestures", "tap", press, it)
                },
                onDoubleTap = {
                    Log.d(TAG, "detectTapGestures onDoubleTap $it")
                    LogGesture("detectTapGestures", "double tap", press, it)
                },
                onLongPress = {
                    Log.d(TAG, "detectTapGestures onLongPress $it")
                },
                onPress = {
                    press = it
                    Log.d(TAG, "detectTapGestures onPress $it")
                }
            )
        }
/*
        .pointerInput(Unit) {
            // https://developer.android.com/reference/kotlin/androidx/compose/foundation/gestures/package-summary#(androidx.compose.ui.input.pointer.PointerInputScope).detectTapGestures(kotlin.Function1,kotlin.Function1,kotlin.coroutines.SuspendFunction2,kotlin.Function1)
            detectTransformGestures(
                onGesture = { centroid, pan, gestureZoom, gestureRotate ->
                    Log.d(
                        TAG,
                        "detectTransformGestures centroid $centroid pan $pan gestureZoom $gestureZoom gestureRotate $gestureRotate"
                    )
                },
            )
        }
*/
    Canvas(modifier = drawModifier) {
//        action.value?.let {
//            action.value = false
//            //val canvas = drawContext.canvas.nativeCanvas
//        }
    }
}


