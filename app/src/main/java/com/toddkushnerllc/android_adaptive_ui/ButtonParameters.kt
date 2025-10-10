package com.toddkushnerllc.android_adaptive_ui

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object ButtonParameters {

    val buttonGapPctIndexMax = 3
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
        8.sp,
        11.sp,
        13.sp,
        22.sp,
        32.sp,
    )

    fun init(density: Density) {
        for (buttonWidthPx in buttonWidthsPx) buttonWidthsDp +=
            with(density) { buttonWidthPx.toDp() }
        for (buttonHeightPx in buttonHeightsPx) buttonHeightsDp +=
            with(density) { buttonHeightPx.toDp() }
    }

    /*
    solve for column case

    c = button-columns
    w = button-width
    gp = gap-percentage
    gw = gap-width = w * gp
    sw = screen-width

    sw = c * w + (c + 1) * gw

    buttonWidthGapPctToColumns() (w, gp) ->
        sw = c * w + (c + 1) * gw
           = c * w + (c + 1) * w * gp
           = c * w + c * w * gp + w * gp
           = c * (w + w * gp) + w * gp
           = c * w + (gp + 1) + w * gp
        sw - w * gp = c * w * (gp + 1)
        c = (sw - w * gp) / (w * (gp + 1))
    columnsGapPctToButtonWidthDp()
        sw = c * w + (c + 1) * gw
        sw = c * w + (c + 1) * w * gp
           = w * (c + (c + 1) * gp)
        w = sw / ((c + (c + 1) * gp))
    columnsButtonWidthDpToGapPct()
        sw = c * w + (c + 1) * gw
        sw = c * w + (c + 1) * w * gp
        sw - c * w = (c + 1) * w * gp
        gp = (sw - c * w) / ((c + 1) * w)
     */

    // c = (sw - w * gp) / (w * (gp + 1))
    fun buttonWidthGapPctToColumns(
        density: Density,
        screenWidthDp: Dp,
        buttonWidthDp: Dp,
        gapPercentage: Float
    ): Int {
        val screenWidthPx = with(density) { screenWidthDp.roundToPx() }
        val buttonWidthPx = with(density) { buttonWidthDp.roundToPx() }
        return ((screenWidthPx - buttonWidthPx * gapPercentage) /
                (buttonWidthPx * (gapPercentage + 1.0f))).toInt()
    }

    //  c = (sh - h * gp) / (h * (gp + 1))
    fun buttonHeightGapPctToRows(
        density: Density,
        screenHeightDp: Dp,
        buttonHeightDp: Dp,
        gapPercentage: Float
    ): Int {
        val screenHeightPx = with(density) { screenHeightDp.toPx() }
        val buttonHeightPx = with(density) { buttonHeightDp.toPx() }
        return ((screenHeightPx - buttonHeightPx * gapPercentage) /
                (buttonHeightPx * (gapPercentage + 1.0f))).toInt()
    }

    // w = sw / ((c + (c + 1) * gp))
    fun columnsGapPctToButtonWidthDp(
        density: Density,
        screenWidthDp: Dp,
        buttonColumns: Int,
        gapPercentage: Float
    ): Dp {
        val screenWidthPx = with(density) { screenWidthDp.toPx() }
        return with(density) {
            (screenWidthPx / (buttonColumns + (buttonColumns + 1) * gapPercentage)).toDp()
        }
    }

    // h = sh / ((c + (c + 1) * gp))
    fun rowsGapPctToButtonHeightDp(
        density: Density,
        screenHeightDp: Dp,
        buttonRows: Int,
        gapPercentage: Float
    ): Dp {
        val screenHeightPx = with(density) { screenHeightDp.toPx() }
        return with(density) {
            (screenHeightPx / (buttonRows + (buttonRows + 1) * gapPercentage)).toDp()
        }
    }

    //  gp = (sw - c * w) / ((c + 1) * w)
    fun columnsButtonWidthDpToGapPct(
        density: Density,
        screenWidthDp: Dp,
        buttonWidthDp: Dp,
        buttonColumns: Int
    ): Float {
        val screenWidthPx = with(density) { screenWidthDp.toPx() }
        val buttonWidthPx = with(density) { buttonWidthDp.toPx() }
        return with(density) {
            ((screenWidthPx - buttonColumns * buttonWidthPx) / ((buttonColumns + 1) * buttonWidthPx))
        }
    }

    //  gp = (sh - c * h) / ((c + 1) * h)
    fun rowsButtonHeightDpToGapPct(
        density: Density,
        screenHeightDp: Dp,
        buttonHeightDp: Dp,
        buttonRows: Int
    ): Float {
        val screenHeightPx = with(density) { screenHeightDp.toPx() }
        val buttonHeightPx = with(density) { buttonHeightDp.toPx() }
        return with(density) {
            ((screenHeightPx - buttonRows * screenHeightPx) / ((buttonRows + 1) * screenHeightPx))
        }
    }
}