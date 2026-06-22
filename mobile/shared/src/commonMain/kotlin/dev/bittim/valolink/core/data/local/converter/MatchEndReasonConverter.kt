/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchEndReasonConverter.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.05.26, 17:40
 */

package dev.bittim.valolink.core.data.local.converter

import androidx.room.TypeConverter
import dev.bittim.valolink.core.domain.model.MatchEndReason

class MatchEndReasonConverter {
    @TypeConverter
    fun toMatchEndReason(value: String?): MatchEndReason? {
        return value?.let { MatchEndReason.valueOf(it); }
    }

    @TypeConverter
    fun fromMatchEndReason(value: MatchEndReason?): String? {
        return value?.name
    }
}