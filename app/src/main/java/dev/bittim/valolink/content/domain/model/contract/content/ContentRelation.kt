/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContentRelation.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 16:03
 */

package dev.bittim.valolink.content.domain.model.contract.content

import java.time.Instant

interface ContentRelation {
    val uuid: String
    val displayName: String?
    val startTime: Instant?
    val endTime: Instant?

    fun calcRemainingDays(): Int?
}