/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseMatchRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 16:55
 */

package dev.bittim.valolink.core.data.repo

import dev.bittim.valolink.core.data.remote.dto.MatchDto
import dev.bittim.valolink.core.data.remote.dto.MatchParticipantDto
import dev.bittim.valolink.core.domain.model.Match
import dev.bittim.valolink.core.domain.model.MatchParticipant
import dev.bittim.valolink.core.domain.repo.MatchRepo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class SupabaseMatchRepo(
    private val supabase: SupabaseClient
) : MatchRepo {
    /**
     * Inserts a match and its participant into the data store.
     *
     * @param match The match to insert.
     * @param matchParticipant The participant associated with the match.
     */
    override suspend fun insert(
        match: Match,
        matchParticipant: MatchParticipant
    ) {
        supabase.from("matches").insert(MatchDto.fromModel(match))
        insertParticipant(matchParticipant)
    }

    override suspend fun insertParticipant(matchParticipant: MatchParticipant) {
        supabase.from("match_participants").insert(MatchParticipantDto.fromModel(matchParticipant))
    }
}