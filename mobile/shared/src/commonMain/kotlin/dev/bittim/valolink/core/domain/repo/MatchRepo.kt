/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.08.26, 17:01
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.domain.model.Match
import dev.bittim.valolink.core.domain.model.MatchParticipant

interface MatchRepo {
    suspend fun upsert(match: Match, matchParticipant: MatchParticipant)
    suspend fun upsertParticipant(matchParticipant: MatchParticipant)
}