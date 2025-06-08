/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       Event.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 16:03
 */

package dev.bittim.valolink.content.domain.model

import dev.bittim.valolink.content.domain.model.contract.content.ContentRelation
import java.time.Instant
import java.time.temporal.ChronoUnit

data class Event(
    override val uuid: String,
    override val displayName: String?,
    val shortDisplayName: String?,
    override val startTime: Instant,
    override val endTime: Instant,
) : ContentRelation {
    override fun calcRemainingDays(): Int? {
        val days = Instant.now().until(
            endTime,
            ChronoUnit.DAYS
        ).toInt()
        return if (days < 0) null else days
    }
}
