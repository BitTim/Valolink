/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ParseIntInput.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.09.26, 15:23
 */

package dev.bittim.valolink.feature.activity.domain.logic

import dev.bittim.valolink.core.domain.Outcome
import kotlin.math.absoluteValue

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
fun parseIntInput(rawValue: String?, allowNegative: Boolean = true, maxDigits: Int? = null): Outcome<Int, IntParseError> {
    if (rawValue.isNullOrEmpty()) return Outcome.Err(IntParseError.EMPTY)

    return try {
        val score = rawValue.toInt()
        if (score < 0 && !allowNegative) return Outcome.Err(IntParseError.NEGATIVE)
        if (maxDigits != null && score.toLong().absoluteValue.toString().length > maxDigits) {
            return Outcome.Err(IntParseError.TOO_MANY_DIGITS)
        }
        Outcome.Ok(score)
    } catch (_: NumberFormatException) {
        Outcome.Err(IntParseError.INVALID)
    }
}