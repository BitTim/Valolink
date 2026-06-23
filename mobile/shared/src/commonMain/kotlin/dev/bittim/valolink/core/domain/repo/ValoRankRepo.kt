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
    /**
 * Observes the ValoRank for a given rank table and tier.
 *
 * @param locale Optional locale for scoping the observation.
 * @return A stream emitting the ValoRank for the specified rank table and tier, or null if not available.
 */
fun observe(rankTable: Uuid, tier: Int, locale: String? = null): Flow<ValoRank?>
    /**
 * Observes all ValoRank entries for a given rank table.
 *
 * @return A Flow that emits lists of ValoRank entries.
 */
fun observeAll(rankTable: Uuid, locale: String? = null): Flow<List<ValoRank>>

    /**
 * Synchronizes the repository's data.
 */
suspend fun sync()
}