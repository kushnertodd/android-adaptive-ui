package com.toddkushnerllc.android_adaptive_ui

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object ButtonParameters {

    val buttonGapIndexMax = 5
    val buttonGapPercentage = arrayOf(
        10.0f,
        20.0f,
        30.0f
    )
    val buttonSizeIndexMax = 5
    var buttonHeightsDp = emptyArray<Dp>()
    val buttonHeightsPx = arrayOf(
        136.2f,
        162.7f,
        202.1f,
        266.7f,
        391.8f
    )
    var buttonWidthsDp = emptyArray<Dp>()
    val buttonWidthsPx = arrayOf(
        136.2f,
        162.7f,
        202.1f,
        266.7f,
        391.8f
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
    val buttonRoundedSizes = arrayOf(
        9.dp,
        12.dp,
        16.dp,
        21.dp,
        28.dp
    )
    val buttonTextSizes = arrayOf(
        11.sp,
        14.sp,
        19.sp,
        26.sp,
        34.sp
    )

    fun init(density: Density) {
        for (buttonWidthPx in buttonWidthsPx) buttonWidthsDp +=
            with(density) { buttonWidthPx.toDp() }
        for (buttonHeightPx in buttonHeightsPx) buttonHeightsDp +=
            with(density) { buttonHeightPx.toDp() }
    }

    fun buttonWidthDpToColumns(screenWidthDp: Dp, buttonWidthDp: Dp, gapPercentage: Float): Int =
        (screenWidthDp / (buttonWidthDp * (1 + 2 * gapPercentage))).toInt()

    fun columnsToButtonWidthDp(screenWidthDp: Dp, buttonColumns: Int, gapPercentage: Float) =
        screenWidthDp / (buttonColumns * (1 + 2 * gapPercentage))

    fun buttonHeightDpToRows(screenHeightDp: Dp, buttonHeightDp: Dp, gapPercentage: Float): Int =
        (screenHeightDp / (buttonHeightDp * (1 + 2 * gapPercentage))).toInt()

    fun columnsToButtonHeightDp(screenHeightDp: Dp, buttonRows: Int, gapPercentage: Float) =
        screenHeightDp / (buttonRows * (1 + 2 * gapPercentage))

}