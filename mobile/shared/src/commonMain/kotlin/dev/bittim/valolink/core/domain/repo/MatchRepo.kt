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
    /**
 * Inserts a match together with its participant.
 *
 * @param match The match to insert.
 * @param matchParticipant The participant associated with the match.
 */
suspend fun insert(match: Match, matchParticipant: MatchParticipant)
    /**
 * Inserts a match participant independently of its match.
 */
suspend fun insertParticipant(matchParticipant: MatchParticipant)
}