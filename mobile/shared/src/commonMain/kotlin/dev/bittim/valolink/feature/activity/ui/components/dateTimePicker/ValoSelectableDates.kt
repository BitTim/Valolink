/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoSelectableDates.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.06.26, 02:13
 */

package dev.bittim.valolink.feature.activity.ui.components.dateTimePicker

import androidx.compose.material3.SelectableDates
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant


object ValoSelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val ep1StartDate = Instant.fromEpochMilliseconds(EP1_START_TIME)
            .toLocalDateTime(TimeZone.UTC).date
            .atStartOfDayIn(TimeZone.UTC)
            .toEpochMilliseconds()

        return utcTimeMillis <= Clock.System.now().toEpochMilliseconds() && utcTimeMillis >= ep1StartDate
    }

    override fun isSelectableYear(year: Int): Boolean {
        val ep1Year = Instant.fromEpochMilliseconds(EP1_START_TIME)
            .toLocalDateTime(TimeZone.UTC).year
        val currentYear = Clock.System.now().toLocalDateTime(TimeZone.UTC).year
        return year in ep1Year..currentYear
    }

    const val EP1_START_TIME = 1590994800000 // 01 Jun 2020 07:00:00 (+0000)
}