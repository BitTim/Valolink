/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.08.26, 17:01
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.ValoSeason
import kotlin.uuid.Uuid

interface ActivityRepo {
    suspend fun get(user: Uuid, season: ValoSeason): List<Activity>
    suspend fun upsert(activity: Activity)
}