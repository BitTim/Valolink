/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       CalcProgressDecimalUseCase.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   17.12.24, 20:40
 */

package dev.bittim.valolink.core.domain.usecase.progress

import javax.inject.Inject

class CalcProgressDecimalUseCase @Inject constructor() {
    operator fun invoke(
        collected: Int,
        total: Int,
    ): Float {
        if (total == 0) return 1f
        return collected.toFloat() / total.toFloat()
    }
}