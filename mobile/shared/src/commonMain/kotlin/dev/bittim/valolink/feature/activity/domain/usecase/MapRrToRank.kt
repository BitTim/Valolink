/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MapRrToRank.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 03:30
 */

package dev.bittim.valolink.feature.activity.domain.usecase

import dev.bittim.valolink.core.domain.model.Rank
import dev.bittim.valolink.core.domain.repo.ValoCompetitiveSeasonRepo
import dev.bittim.valolink.core.domain.repo.ValoRankRepo
import dev.bittim.valolink.core.domain.repo.ValoSeasonRepo
import kotlinx.coroutines.flow.firstOrNull
import kotlin.math.floor
import kotlin.time.Instant

class MapRrToRank(
    private val valoSeasonRepo: ValoSeasonRepo,
    private val valoCompetitiveSeasonRepo: ValoCompetitiveSeasonRepo,
    private val valoRankRepo: ValoRankRepo
) {
    suspend operator fun invoke(rr: Int, time: Instant, locale: String? = null): Rank? {
        val season = valoSeasonRepo.observe(time, locale).firstOrNull() ?: return null
        val competitiveSeason = valoCompetitiveSeasonRepo.observeBySeason(season.uuid).firstOrNull() ?: return null
        val ranks = valoRankRepo.observeAll(competitiveSeason.rankTable, locale).firstOrNull()?.filter {
            !it.division.contains("UNRANKED") && !it.division.contains("INVALID")
        } ?: return null

        val tierOffset = ranks.firstOrNull()?.tier ?: return null
        val relativeTier = floor(rr.toDouble() / RR_PER_RANK).toInt()
        val calculatedTier = relativeTier + tierOffset
        val calculatedRr = rr - relativeTier * RR_PER_RANK
        val rank = ranks.firstOrNull { it.tier == calculatedTier } ?: return null

        return Rank(
            rank = rank,
            rr = calculatedRr
        )
    }

    companion object {
        const val RR_PER_RANK = 100
    }
}