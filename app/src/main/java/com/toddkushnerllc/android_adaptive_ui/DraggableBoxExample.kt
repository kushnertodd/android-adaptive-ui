import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/*
In this example:
•	A Box composable is created and its position is controlled by offsetX and offsetY states using the offset modifier.
•	The pointerInput modifier is attached to the Box, allowing it to receive raw pointer events.
•	Inside the pointerInput block, a custom detectDragGestures function (or the built-in one from Compose) is used to process drag events.
•	The onDrag lambda receives a PointerInputChange object (change) and the dragAmount (an Offset representing the change in position).
•	change.consume() is called to indicate that this event has been handled, preventing other modifiers or parent composables from processing it further.
•	The offsetX and offsetY states are updated based on the dragAmount, effectively moving the Box on the screen.

//

@Composable
fun DraggableBoxExample() {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Column(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .background(Color.Blue)
                .pointerInput(Unit) {
                    detectDragGestures { change: PointerInputChange, dragAmount: Offset ->
                        // Consume the change to prevent other modifiers from handling it
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
        )
    }
}

// Helper function for detecting drag gestures
// This is a simplified version; Compose provides built-in detectDragGestures
// which is generally preferred for common use cases.
suspend fun androidx.compose.ui.input.pointer.PointerInputScope.detectDragGestures(
    onDrag: (PointerInputChange, Offset) -> Unit
) {
    awaitPointerEventScope {
        var down: PointerInputChange? = null
        while (true) {
            val event = awaitPointerEvent()
            val change = event.changes.firstOrNull()

            if (change != null) {
                when (event.type) {
                    androidx.compose.ui.input.pointer.PointerEventType.Press -> {
                        down = change
                    }
                    androidx.compose.ui.input.pointer.PointerEventType.Move -> {
                        if (down != null && change.pressed) {
                            val dragAmount = change.position - down.position
                            onDrag(change, dragAmount)
                            down = change // Update down for continuous drag
                        }
                    }
                    androidx.compose.ui.input.pointer.PointerEventType.Release -> {
                        down = null
                    }
                }
            }
        }
    }
}
*/
