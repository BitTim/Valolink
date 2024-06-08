package dev.bittim.valolink.main.ui.util

import kotlin.math.floor

fun getProgressDecimal(
    collected: Int,
    total: Int,
): Float {
    return collected.toFloat() / total.toFloat()
}

fun getProgressPercent(
    collected: Int,
    total: Int,
): Int {
    return floor(
        getProgressDecimal(
            collected,
            total
        ) * 100
    ).toInt()
}