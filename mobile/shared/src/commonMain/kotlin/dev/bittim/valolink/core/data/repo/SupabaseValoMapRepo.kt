/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseValoMapRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   08.06.26, 20:21
 */

package dev.bittim.valolink.core.data.repo

import dev.bittim.valolink.core.data.local.Database
import dev.bittim.valolink.core.data.remote.dto.ValoMapDto
import dev.bittim.valolink.core.domain.model.SimpleValoMap
import dev.bittim.valolink.core.domain.repo.ValoMapRepo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

class SupabaseValoMapRepo(
    private val database: Database,
    private val supabase: SupabaseClient
) : ValoMapRepo {
    override fun observe(
        uuid: Uuid,
        locale: String?
    ): Flow<SimpleValoMap?> {
        return database.valoMapDao().get(uuid)
            .map { it?.toSimpleModel(locale) }
            .flowOn(Dispatchers.IO)
    }

    override fun observeAll(locale: String?): Flow<List<SimpleValoMap>> {
        return database.valoMapDao().get()
            .map { it.map { entity -> entity.toSimpleModel(locale) } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun sync() {
        val maps = supabase.from("valo_maps").select().decodeList<ValoMapDto>()
        database.valoMapDao().upsert(maps.map { it.toEntity() })
    }
}