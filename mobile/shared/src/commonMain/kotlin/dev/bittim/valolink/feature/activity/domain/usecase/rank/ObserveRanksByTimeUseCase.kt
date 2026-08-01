/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ObserveRanksByTimeUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.06.26, 13:23
 */

package dev.bittim.valolink.feature.activity.domain.usecase.rank

import dev.bittim.valolink.core.domain.model.ValoRank
import dev.bittim.valolink.core.domain.repo.ValoCompetitiveSeasonRepo
import dev.bittim.valolink.core.domain.repo.ValoRankRepo
import dev.bittim.valolink.core.domain.repo.ValoSeasonRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlin.time.Instant

class ObserveRanksByTimeUseCase(
    private val valoSeasonRepo: ValoSeasonRepo,
    private val valoCompetitiveSeasonRepo: ValoCompetitiveSeasonRepo,
    private val valoRankRepo: ValoRankRepo
) {
    /**
     * Observes the ranks associated with the season and competitive season at a specified time.
     *
     * @param time The point in time used to select the season.
     * @param locale The optional locale for localized season and rank data.
     * @return A stream of distinct rank lists for the selected competitive season.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(time: Instant, locale: String? = null): Flow<List<ValoRank>> {
        return valoSeasonRepo.observe(time, locale).distinctUntilChanged().filterNotNull().flatMapLatest { season ->
            valoCompetitiveSeasonRepo.observeBySeason(season.uuid).distinctUntilChanged().filterNotNull().flatMapLatest { competitiveSeason ->
                valoRankRepo.observeAll(competitiveSeason.rankTable, locale).distinctUntilChanged()
            }
        }
    }
}