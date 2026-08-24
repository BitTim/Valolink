/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseMatchRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   19.08.26, 17:56
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
    override suspend fun upsert(
        match: Match,
        matchParticipant: MatchParticipant
    ) {
        supabase.from("matches").upsert(MatchDto.fromModel(match))
        upsertParticipant(matchParticipant)
    }

    override suspend fun upsertParticipant(matchParticipant: MatchParticipant) {
        supabase.from("match_participants").upsert(MatchParticipantDto.fromModel(matchParticipant))
    }
}