/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ParseIntUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   28.06.26, 12:23
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.Result
import kotlin.math.absoluteValue

class ParseIntUseCase {
    enum class IntParseError {
        EMPTY,
        INVALID,
        NEGATIVE,
        TOO_MANY_DIGITS
    }

    /**
     * Parses an optional string into an integer with configurable sign and digit constraints.
     *
     * @param rawValue The string to parse.
     * @param allowNegative Whether negative values are accepted.
     * @param maxDigits The maximum number of digits allowed in the absolute value, or `null` for no limit.
     * @return A successful result containing the parsed integer, or an error describing why parsing failed.
     */
    operator fun invoke(rawValue: String?, allowNegative: Boolean = true, maxDigits: Int? = null): Result<Int, IntParseError> {
        if (rawValue.isNullOrEmpty()) return Result.Err(IntParseError.EMPTY)

        return try {
            val score = rawValue.toInt()
            if (score < 0 && !allowNegative) return Result.Err(IntParseError.NEGATIVE)
            if (maxDigits != null && score.toLong().absoluteValue.toString().length > maxDigits) {
                return Result.Err(IntParseError.TOO_MANY_DIGITS)
            }
            Result.Ok(score)
        } catch (_: NumberFormatException) {
            Result.Err(IntParseError.INVALID)
        }
    }
}