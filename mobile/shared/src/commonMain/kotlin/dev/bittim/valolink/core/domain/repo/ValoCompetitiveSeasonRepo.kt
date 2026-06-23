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
    fun observe(uuid: Uuid): Flow<ValoCompetitiveSeason?>
    fun observeBySeason(season: Uuid): Flow<ValoCompetitiveSeason?>

    suspend fun sync()
}