/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseActivityRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 16:53
 */

package dev.bittim.valolink.core.data.repo

import dev.bittim.valolink.core.data.remote.dto.ActivityDto
import dev.bittim.valolink.core.domain.model.Activity
import dev.bittim.valolink.core.domain.model.ValoSeason
import dev.bittim.valolink.core.domain.repo.ActivityRepo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlin.uuid.Uuid

class SupabaseActivityRepo(
    private val supabase: SupabaseClient
): ActivityRepo {
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
}