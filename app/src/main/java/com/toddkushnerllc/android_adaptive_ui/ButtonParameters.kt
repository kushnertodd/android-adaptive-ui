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

    /*
    solve for column case

    bc = button-columns
    bw = button-width
    gp = gap-percentage
    gw = gap-width
    sw = screen-width

        bh = bw
        gw = gh = bw * gp
    buttonWidthGapPctToColumns() (bw, gp) ->
        sw = bc * bw + (bc + 1) * gp
           = bc * bw + (bc + 1) * bw * gp
           = bc * bw + bc * bw * gp + bw * gp
           = bc * (bw + bw * gp) + bw * gp
           = bc * bw + (gp + 1) + bw * gp
        sw - bw * gp = bc * bw * (gp + 1)
        bc = (sw - bw * gp) / (bw * (gp + 1))
        bc = (sw - bw * gp) / (bw * (gp + 1))
    columnsGapPctToButtonWidthDp()
        sw = bc * bw + (bc + 1) * gp
        sw - (bc + 1) * gp = bc * bw
        bw = (sw - (bc + 1) * gp) / bc
     */
    
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

    fun columnsGapPctToButtonWidthDp(
        density: Density,
        screenWidthDp: Dp,
        buttonColumns: Int,
        gapPercentage: Float
    ): Dp {
        val screenWidthPx = with(density) { screenWidthDp.toPx() }
        return with(density) {
            ((screenWidthPx - (buttonColumns + 1) * gapPercentage) / buttonColumns).toDp()
        }
    }

    fun rowsGapPctToButtonHeightDp(
        density: Density,
        screenHeightDp: Dp,
        buttonRows: Int,
        gapPercentage: Float
    ): Dp {
        val screenHeightPx = with(density) { screenHeightDp.toPx() }
        return with(density) {
            ((screenHeightPx - (buttonRows + 1) * gapPercentage) / buttonRows).toDp()
        }
    }
}