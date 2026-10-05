/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       FindRankTableByTimeUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   26.09.26, 17:36
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.model.ValoRankTable
import dev.bittim.valolink.core.domain.repo.ValoCompetitiveSeasonRepo
import dev.bittim.valolink.core.domain.repo.ValoSeasonRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Instant

class FindRankTableByTimeUseCase(
    private val valoSeasonRepo: ValoSeasonRepo,
    private val valoCompetitiveSeasonRepo: ValoCompetitiveSeasonRepo,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(rankTables: List<ValoRankTable>, time: Instant, locale: String? = null): Flow<ValoRankTable?> {
        return valoSeasonRepo.observe(time, locale).distinctUntilChanged().flatMapLatest { season ->
            if (season == null) return@flatMapLatest flowOf(null)

            valoCompetitiveSeasonRepo.observeBySeason(season.uuid).distinctUntilChanged().flatMapLatest { competitiveSeason ->
                if (competitiveSeason == null) return@flatMapLatest flowOf(null)

                flowOf(rankTables.find { it.uuid == competitiveSeason.rankTable })
            }
        }
    }
}