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

import dev.bittim.valolink.core.domain.model.MatchParticipant

interface MatchRepo {
    /**
 * Inserts a participant into a match.
 *
 * @param matchParticipant The participant to insert.
 */
suspend fun insertParticipant(matchParticipant: MatchParticipant)
}