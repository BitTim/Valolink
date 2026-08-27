/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseActivityRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   27.08.26, 18:29
 */

package dev.bittim.valolink.core.data.repo

import dev.bittim.valolink.core.data.remote.dto.*
import dev.bittim.valolink.core.domain.model.*
import dev.bittim.valolink.core.domain.repo.ActivityRepo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
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
    ): List<ActivityWithMatchDto> {
        return supabase.from("activities").select(columns = Columns.raw(
            "*, match_participants(*, matches(*))"
        )) {
            filter {
                ActivityWithMatchDto::userId eq user
                ActivityWithMatchDto::time gte season.startTime
                ActivityWithMatchDto::time lte season.endTime
            }
        }.decodeList<ActivityWithMatchDto>()
    }

    /**
     * Inserts an activity and returns its identifier.
     *
     * @param activityDraft The activity to insert.
     * @param matchDraft The match details required for a match activity.
     * @param matchParticipantDraft The participant details required for a match activity.
     * @return The UUID of the inserted activity.
     * @throws IllegalArgumentException If a match activity lacks match or participant details.
     */
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