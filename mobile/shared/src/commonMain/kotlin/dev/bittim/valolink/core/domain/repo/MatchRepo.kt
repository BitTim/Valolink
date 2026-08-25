/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       MatchRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 21:15
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.domain.model.MatchParticipantDraft

interface MatchRepo {
    /**
    * Inserts a match participant independently of its match.
    */
    suspend fun insertParticipant(matchParticipantDraft: MatchParticipantDraft)
}