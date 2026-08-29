/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       RankFilter.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 19:52
 */

package dev.bittim.valolink.feature.activity.domain.logic

import dev.bittim.valolink.core.domain.model.ValoRank

object RankFilter {
    /**
     * Filters out ranks with unranked or invalid divisions.
     *
     * @param ranks The ranks to filter.
     * @return The ranks whose divisions do not contain `UNRANKED` or `INVALID`.
     */
    fun filterUnrankedAndInvalid(ranks: List<ValoRank>): List<ValoRank> {
        return ranks.filter {
            !it.division.contains("UNRANKED") && !it.division.contains("INVALID")
        }
    }
}