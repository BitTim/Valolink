/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 16:55
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.domain.model.Match
import dev.bittim.valolink.core.domain.model.MatchParticipant

interface MatchRepo {
    suspend fun insert(match: Match, matchParticipant: MatchParticipant)
    suspend fun insertParticipant(matchParticipant: MatchParticipant)
}