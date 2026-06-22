/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 16:53
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.ValoSeason
import kotlin.uuid.Uuid

interface ActivityRepo {
    suspend fun get(user: Uuid, season: ValoSeason): List<Activity>
}