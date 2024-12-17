/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ProgressUtil.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

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
    if (total == 0) return 100

    return floor(getProgressDecimal(collected, total) * 100).toInt()
}