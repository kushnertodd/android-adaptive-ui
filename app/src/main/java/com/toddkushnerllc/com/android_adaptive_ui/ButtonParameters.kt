package com.toddkushnerllc.com.android_adaptive_ui

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object ButtonParameters {
    val buttonSizeIndexMax = 6
    var buttonWidthsDp = emptyArray<Dp>()
    val buttonWidthsPx = arrayOf(
        120f,
        135f,
        150f,
        175f,
        200f,
        240f,
        275f
    )
    var buttonHeightsDp = emptyArray<Dp>()
    val buttonHeightsPx = arrayOf(
        120f,
        135f,
        150f,
        175f,
        200f,
        240f,
        275f
    )
    val buttonTextSizes = arrayOf(
        8.sp,
        10.sp,
        12.sp,
        14.sp,
        16.sp,
        18.sp,
        20.sp
    )

    fun init(density: Density) {
        for (buttonWidthPx in buttonWidthsPx) buttonWidthsDp +=
            with(density) { buttonWidthPx.toDp() }
        for (buttonHeightPx in buttonHeightsPx) buttonHeightsDp +=
            with(density) { buttonHeightPx.toDp() }
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

