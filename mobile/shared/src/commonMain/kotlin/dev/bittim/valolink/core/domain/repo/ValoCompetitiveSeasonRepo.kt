/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ValoCompetitiveSeasonRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 03:12
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.domain.model.ValoCompetitiveSeason
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface ValoCompetitiveSeasonRepo {
    /**
 * Observes changes to a competitive season.
 *
 * @return A flow that emits the competitive season, or null if not found.
 */
fun observe(uuid: Uuid): Flow<ValoCompetitiveSeason?>
    /**
 * Observes the competitive season associated with the given season UUID.
 *
 * @param season The UUID of the season to observe.
 * @return A Flow that emits the competitive season, or null if not found.
 */
fun observeBySeason(season: Uuid): Flow<ValoCompetitiveSeason?>

    /**
 * Synchronizes the repository with the latest competitive season data.
 */
suspend fun sync()
}