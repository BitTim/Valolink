/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoSeasonRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 04:05
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.domain.model.ValoSeason
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

interface ValoSeasonRepo {
    fun observe(uuid: Uuid, locale: String? = null): Flow<ValoSeason?>
    fun observe(time: Instant, locale: String? = null): Flow<ValoSeason?>
    fun observeAll(locale: String? = null): Flow<List<ValoSeason>>

    suspend fun sync()
}