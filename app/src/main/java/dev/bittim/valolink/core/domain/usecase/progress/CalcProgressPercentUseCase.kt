/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       CalcProgressPercentUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   17.12.24, 20:52
 */

package dev.bittim.valolink.core.domain.usecase.progress

import dev.bittim.valolink.main.ui.util.getProgressDecimal
import javax.inject.Inject
import kotlin.math.floor

class CalcProgressPercentUseCase @Inject constructor() {
    operator fun invoke(
        collected: Int,
        total: Int,
    ): Int {
        if (total == 0) return 100
        return floor(getProgressDecimal(collected, total) * 100).toInt()
    }
}