/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 22:16
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
     * @param season The season for which to retrieve activities.
     * @return The activities recorded for the user during the season.
     */
    suspend fun get(user: Uuid, season: ValoSeason): List<Activity>

    /**
     * Stores an activity record.
     *
     * @param activityDraft The activity record draft to store.
     * @param matchDraft The match draft associated with the activity or null.
     * @param matchParticipantDraft The match participant draft associated with the activity or null.
     * @return The unique identifier of the inserted activity.
     */
    suspend fun insert(activityDraft: ActivityDraft, matchDraft: MatchDraft?, matchParticipantDraft: MatchParticipantDraft?): Uuid

    @Serializable
    data class MatchActivityInsertRequest(
        @SerialName("p_activity") val activity: ActivityInputDto,
        @SerialName("p_match") val match: MatchInputDto,
        @SerialName("p_participant") val participant: MatchParticipantInputDto
    )
}