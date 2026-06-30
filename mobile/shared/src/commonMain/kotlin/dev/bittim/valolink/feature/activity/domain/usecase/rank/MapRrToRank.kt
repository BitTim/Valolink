/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MapRrToRank.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.06.26, 13:40
 */

package dev.bittim.valolink.feature.activity.domain.usecase.rank

import dev.bittim.valolink.core.domain.model.Rank
import dev.bittim.valolink.core.domain.repo.ValoCompetitiveSeasonRepo
import dev.bittim.valolink.core.domain.repo.ValoRankRepo
import dev.bittim.valolink.core.domain.repo.ValoSeasonRepo
import dev.bittim.valolink.feature.activity.domain.constants.RankConstants
import kotlinx.coroutines.flow.firstOrNull
import kotlin.math.floor
import kotlin.time.Instant

class MapRrToRank(
    private val valoSeasonRepo: ValoSeasonRepo,
    private val valoCompetitiveSeasonRepo: ValoCompetitiveSeasonRepo,
    private val valoRankRepo: ValoRankRepo
) {
    /**
     * Maps an RR value to the matching rank for the active season at the given time.
     *
     * @param rr The RR value to resolve, or `null` to return the tier-0 rank.
     * @param time The instant used to determine the active season.
     * @param locale Optional locale for rank data retrieval.
     * @return The resolved `Rank`, or `null` if the season, rank table, or matching rank cannot be found.
     */
    suspend operator fun invoke(rr: Int?, time: Instant, locale: String? = null): Rank? {
        val season = valoSeasonRepo.observe(time, locale).firstOrNull() ?: return null
        val competitiveSeason = valoCompetitiveSeasonRepo.observeBySeason(season.uuid).firstOrNull() ?: return null

        val ranks = valoRankRepo.observeAll(competitiveSeason.rankTable, locale).firstOrNull()
        val filteredRanks = ranks?.filter {
            !it.division.contains("UNRANKED") && !it.division.contains("INVALID")
        } ?: return null

        if (rr == null) return ranks.firstOrNull { it.tier == 0 }?.let {
            Rank(rank = it, rr = 0)
        }

        val tierOffset = filteredRanks.minOfOrNull { it.tier } ?: return null
        val relativeTier = floor(rr.toDouble() / RankConstants.RR_PER_RANK).toInt()
        val calculatedTier = relativeTier + tierOffset
        val calculatedRr = rr - relativeTier * RankConstants.RR_PER_RANK

        val actualTier = calculatedTier.coerceAtMost(filteredRanks.lastOrNull()?.tier ?: 0)
        val rank = filteredRanks.firstOrNull { it.tier == actualTier } ?: return null

        return Rank(
            rank = rank,
            rr = calculatedRr
        )
    }
}