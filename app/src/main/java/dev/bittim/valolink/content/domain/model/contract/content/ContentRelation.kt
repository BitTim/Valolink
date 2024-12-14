/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ContentRelation.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:30
 */

package dev.bittim.valolink.content.domain.model.contract.content

import java.time.Instant

interface ContentRelation {
    val uuid: String
    val displayName: String
    val startTime: Instant?
    val endTime: Instant?

    fun calcRemainingDays(): Int?
}