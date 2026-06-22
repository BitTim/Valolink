/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseValoSeasonRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 04:16
 */

package dev.bittim.valolink.core.data.repo

import dev.bittim.valolink.core.data.local.Database
import dev.bittim.valolink.core.data.remote.dto.ValoSeasonDto
import dev.bittim.valolink.core.domain.model.ValoSeason
import dev.bittim.valolink.core.domain.repo.ValoSeasonRepo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlin.time.Instant
import kotlin.uuid.Uuid

class SupabaseValoSeasonRepo(
    private val database: Database,
    private val supabase: SupabaseClient
) : ValoSeasonRepo {
    override fun observe(
        uuid: Uuid,
        locale: String?
    ): Flow<ValoSeason?> {
        return database.valoSeasonDao().get(uuid)
            .map { it?.toModel(locale) }
            .flowOn(Dispatchers.IO)
    }

    override fun observe(
        time: Instant,
        locale: String?
    ): Flow<ValoSeason?> {
        return database.valoSeasonDao().get(time)
            .map { it?.toModel(locale) }
            .flowOn(Dispatchers.IO)
    }

    override fun observeAll(locale: String?): Flow<List<ValoSeason>> {
        return database.valoSeasonDao().get()
            .map { it.map { entity -> entity.toModel(locale) } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun sync() {
        val seasons = supabase.from("valo_seasons").select().decodeList<ValoSeasonDto>()
        database.valoSeasonDao().upsert(seasons.map { it.toEntity() })
    }
}