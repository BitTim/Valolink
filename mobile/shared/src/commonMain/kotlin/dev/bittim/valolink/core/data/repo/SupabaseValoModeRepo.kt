/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseValoModeRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 20:21
 */

package dev.bittim.valolink.core.data.repo

import dev.bittim.valolink.core.data.local.Database
import dev.bittim.valolink.core.data.remote.dto.ValoModeDto
import dev.bittim.valolink.core.domain.model.ValoMode
import dev.bittim.valolink.core.domain.repo.ValoModeRepo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

class SupabaseValoModeRepo(
    private val database: Database,
    private val supabase: SupabaseClient
) : ValoModeRepo {
    override fun observe(uuid: Uuid, locale: String?): Flow<ValoMode?> {
        return database.valoModeDao().get(uuid)
            .map { it?.toModel(locale) }
            .flowOn(Dispatchers.IO)
    }

    override fun observeAll(locale: String?): Flow<List<ValoMode>> {
        return database.valoModeDao().get()
            .map { it.map { entity -> entity.toModel(locale) } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun sync() {
        val modes = supabase.from("valo_modes").select().decodeList<ValoModeDto>()
        database.valoModeDao().upsert(modes.map { it.toEntity() })
    }
}