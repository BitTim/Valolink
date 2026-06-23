/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoRankRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 03:08
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.domain.model.ValoRank
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface ValoRankRepo {
    fun observe(rankTable: Uuid, tier: Int, locale: String? = null): Flow<ValoRank?>
    fun observeAll(rankTable: Uuid, locale: String? = null): Flow<List<ValoRank>>

    suspend fun sync()
}