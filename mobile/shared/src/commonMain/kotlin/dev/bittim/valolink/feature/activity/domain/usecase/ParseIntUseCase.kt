/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ParseIntUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   16.06.26, 03:47
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.Result

class ParseIntUseCase {
    enum class IntParseError {
        EMPTY,
        INVALID,
        NEGATIVE
    }

    operator fun invoke(rawValue: String?, allowNegative: Boolean = true): Result<Int, IntParseError> {
        if (rawValue.isNullOrEmpty()) return Result.Err(IntParseError.EMPTY)

        return try {
            val score = rawValue.toInt()
            if (score < 0 && !allowNegative) return Result.Err(IntParseError.NEGATIVE)
            Result.Ok(score)
        } catch (_: NumberFormatException) {
            Result.Err(IntParseError.INVALID)
        }
    }
}