/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       FilterRanksUseCase.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.08.26, 12:13
 */

package dev.bittim.valolink.feature.activity.domain.usecase.rank

import dev.bittim.valolink.core.domain.model.ValoRank

class FilterRanksUseCase {
    operator fun invoke(ranks: List<ValoRank>): List<ValoRank> {
        return ranks.filter {
            !it.division.contains("UNRANKED") && !it.division.contains("INVALID")
        }
    }
}