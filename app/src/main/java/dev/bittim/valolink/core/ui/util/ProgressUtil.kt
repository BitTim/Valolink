/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProgressUtil.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   13.04.25, 17:30
 */

package dev.bittim.valolink.core.ui.util

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
    if (total == 0) return 100

    return floor(getProgressDecimal(collected, total) * 100).toInt()
}
