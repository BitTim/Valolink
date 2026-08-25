/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   25.08.26, 16:55
 */

package dev.bittim.valolink.core.domain.repo

import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.ValoSeason
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
 * @param activity The activity record to store.
 */
suspend fun insert(activity: Activity)
}