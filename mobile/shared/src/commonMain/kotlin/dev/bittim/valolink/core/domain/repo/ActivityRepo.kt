/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 21:31
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.data.remote.dto.ActivityInputDto
import dev.bittim.valolink.core.data.remote.dto.MatchInputDto
import dev.bittim.valolink.core.data.remote.dto.MatchParticipantInputDto
import dev.bittim.valolink.core.domain.model.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

interface ActivityRepo {
    /**
 * Retrieves activities for a user during a season.
 *
 * @param user The user's unique identifier.
 * @param season The season to retrieve activities for.
 * @return The activities associated with the user during the season.
 */
suspend fun get(user: Uuid, season: ValoSeason): List<Activity>
    /**
 * Inserts an activity and its optional match and participant details.
 *
 * @param activityDraft The activity data to insert.
 * @param matchDraft Optional match data to insert with the activity.
 * @param matchParticipantDraft Optional participant data to insert with the match.
 * @return The generated identifier for the inserted activity.
 */
suspend fun insert(activityDraft: ActivityDraft, matchDraft: MatchDraft?, matchParticipantDraft: MatchParticipantDraft?): Uuid

    @Serializable
    data class MatchActivityInsertRequest(
        @SerialName("p_activity") val activity: ActivityInputDto,
        @SerialName("p_match") val match: MatchInputDto,
        @SerialName("p_participant") val participant: MatchParticipantInputDto
    )
}