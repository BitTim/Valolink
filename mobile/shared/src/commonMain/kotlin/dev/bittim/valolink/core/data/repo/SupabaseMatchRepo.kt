/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseMatchRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 21:20
 */

package dev.bittim.valolink.core.data.repo

import dev.bittim.valolink.core.data.remote.dto.MatchParticipantDto
import dev.bittim.valolink.core.domain.model.MatchParticipant
import dev.bittim.valolink.core.domain.repo.MatchRepo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class SupabaseMatchRepo(
    private val supabase: SupabaseClient
) : MatchRepo {
    /**
     * Inserts a match participant record.
     */
    override suspend fun insertParticipant(matchParticipant: MatchParticipant) {
        supabase.from("match_participants").insert(MatchParticipantDto.fromModel(matchParticipant))
    }
}