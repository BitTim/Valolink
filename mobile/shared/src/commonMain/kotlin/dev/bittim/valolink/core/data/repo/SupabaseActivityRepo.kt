/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseActivityRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 21:37
 */

package dev.bittim.valolink.core.data.repo

import dev.bittim.valolink.core.data.remote.dto.ActivityDto
import dev.bittim.valolink.core.data.remote.dto.ActivityInputDto
import dev.bittim.valolink.core.data.remote.dto.MatchInputDto
import dev.bittim.valolink.core.data.remote.dto.MatchParticipantInputDto
import dev.bittim.valolink.core.domain.model.*
import dev.bittim.valolink.core.domain.repo.ActivityRepo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlin.uuid.Uuid

class SupabaseActivityRepo(
    private val supabase: SupabaseClient
): ActivityRepo {
    /**
     * Retrieves a user's activities recorded during the specified season.
     *
     * @param user The user's unique identifier.
     * @param season The season whose time range filters the activities.
     * @return The activities recorded for the user within the season.
     */
    override suspend fun get(
        user: Uuid,
        season: ValoSeason
    ): List<Activity> {
        return supabase.from("activities").select {
            filter {
                ActivityDto::userId eq user
                ActivityDto::time gte season.startTime
                ActivityDto::time lte season.endTime
            }
        }.decodeList<ActivityDto>().map { it.toModel() }
    }

    override suspend fun insert(activityDraft: ActivityDraft, matchDraft: MatchDraft?, matchParticipantDraft: MatchParticipantDraft?): Uuid {
        val activityInput = ActivityInputDto.fromModel(activityDraft)

        return when (activityDraft.type) {
            ActivityType.MATCH -> {
                if (matchDraft == null || matchParticipantDraft == null) throw IllegalArgumentException("Match and match participant draft must be provided for match activity")

                val matchInput = MatchInputDto.fromModel(matchDraft)
                val participantInput = MatchParticipantInputDto.fromModel(matchParticipantDraft)
                val request = ActivityRepo.MatchActivityInsertRequest(activityInput, matchInput, participantInput)

                supabase.postgrest.rpc("insert_match_activity", request).decodeAs<Uuid>()
            }
            ActivityType.RR_REFUND, ActivityType.XP_CORRECTION -> {
                supabase.from("activities").insert(activityInput) { select() }.decodeSingle<ActivityDto>().id
            }
        }
    }
}