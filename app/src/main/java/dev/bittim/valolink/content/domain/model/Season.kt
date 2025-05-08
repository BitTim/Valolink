/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Season.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.content.domain.model

import dev.bittim.valolink.content.domain.model.contract.content.ContentRelation
import java.time.Instant
import java.time.temporal.ChronoUnit

data class Season(
    override val uuid: String,
    override val displayName: String,
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