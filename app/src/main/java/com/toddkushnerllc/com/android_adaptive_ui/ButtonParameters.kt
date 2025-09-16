package com.toddkushnerllc.com.android_adaptive_ui

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object ButtonParameters {
    val buttonSizeIndexMax = 5

    var buttonWidthsDp = emptyArray<Dp>()
    val buttonWidthsPx = arrayOf(
        //102.7f,
        //117.1f,
        136.2f,
        162.7f,
        202.1f,
        266.7f,
        391.8f
    )
    var buttonHeightsDp = emptyArray<Dp>()
    val buttonHeightsPx = arrayOf(
        //102.7f,
        //117.1f,
        136.2f,
        162.7f,
        202.1f,
        266.7f,
        391.8f
    )
    var buttonColumnGapsDp = emptyArray<Dp>()
    val buttonColumnGapsPx = arrayOf(
        96.0f,
        138.5f,
        127.5f,
        108.8f,
        90.0f,
        160.0f,
        136.7f
    )
    var buttonRowGapsDp = emptyArray<Dp>()
    val buttonRowGapsPx = arrayOf(
        125.0f,
        162.0f,
        150.0f,
        130.0f,
        187.5f,
        157.5f,
        131.3f
    )
    val screenButtonColumns = arrayOf(
        4,
        3,
        3,
        3,
        3,
        2,
        2
    )
    val screenButtonRows = arrayOf(
        5,
        4,
        4,
        4,
        3,
        3,
        3
    )
    val buttonTextSizes = arrayOf( // 0.75
        //6.sp,
        //8.sp,
        11.sp,
        14.sp,
        19.sp,
        26.sp,
        34.sp
    )
    val buttonRoundedSizes = arrayOf( // 0.75
        //6.sp,
        //8.sp,
        9.dp,
        12.dp,
        16.dp,
        21.dp,
        28.dp
    )

    fun init(density: Density) {
        for (buttonWidthPx in buttonWidthsPx) buttonWidthsDp +=
            with(density) { buttonWidthPx.toDp() }
        for (buttonHeightPx in buttonHeightsPx) buttonHeightsDp +=
            with(density) { buttonHeightPx.toDp() }
        for (buttonColumnGapPx in buttonColumnGapsPx) buttonColumnGapsDp +=
            with(density) { buttonColumnGapPx.toDp() }
        for (buttonRowGapPx in buttonRowGapsPx) buttonRowGapsDp +=
            with(density) { buttonRowGapPx.toDp() }
    }

    var boxHeightDp = 0.dp
    var boxHeightPx = 0f
    var boxWidthDp = 0.dp
    var boxWidthPx = 0f

    var buttonBoxHeightDp = 0.dp
    var buttonBoxHeightPx = 0f
    var buttonBoxWidthDp = 0.dp
    var buttonBoxWidthPx = 0f
    var buttonHeightDp = 0.dp
    var buttonHeightPx = 0f
    var buttonWidthDp = 0.dp
    var buttonWidthPx = 0f

    fun initButtonSizeIndex(density: Density, buttonSizeIndex: Int) {
        buttonWidthDp = buttonWidthsDp[buttonSizeIndex]
        buttonHeightDp = buttonHeightsDp[buttonSizeIndex]
        buttonWidthPx = with(density) { buttonWidthDp.toPx() }
        buttonHeightPx = with(density) { buttonHeightDp.toPx() }
        /*
                buttonWidthPx= buttonWidths[buttonSizeIndex]
                buttonHeightPx=buttonHeights[buttonSizeIndex]
        */
    }

    var buttonColumns = 0
    var buttonRows = 0
    var gapPercentage = 0.25f

    fun setButtonColumns(screenWidthPx: Float) {
        buttonColumns =
            ((screenWidthPx - buttonWidthPx * gapPercentage) / (buttonWidthPx * (gapPercentage + 1))).toInt()
    }

    fun setButtonRows(screenWidthPx: Float, screenHeightPx: Float) {
        buttonRows =
            ((screenHeightPx - buttonHeightPx * gapPercentage) / (buttonHeightPx * (gapPercentage + 1))).toInt()
    }

    fun setButtonWidth(screenWidthPx: Float) {
        buttonWidthPx = ((screenWidthPx / (buttonColumns * (1 + gapPercentage) + gapPercentage)))
    }

    fun setButtonHeight(screenHeightPx: Float) {
        buttonHeightPx = screenHeightPx / (buttonRows * (1 + gapPercentage) + gapPercentage)
    }
}

