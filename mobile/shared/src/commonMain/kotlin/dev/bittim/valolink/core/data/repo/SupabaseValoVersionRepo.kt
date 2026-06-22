/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseValoVersionRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   07.06.26, 19:51
 */

package dev.bittim.valolink.core.data.repo

import dev.bittim.valolink.core.data.local.Database
import dev.bittim.valolink.core.data.local.entity.ValoVersionEntity
import dev.bittim.valolink.core.data.remote.dto.ValoVersionDto
import dev.bittim.valolink.core.domain.model.ValoVersion
import dev.bittim.valolink.core.domain.repo.ValoVersionRepo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class SupabaseValoVersionRepo(
    private val database: Database,
    private val supabase: SupabaseClient
) : ValoVersionRepo {
    override fun observe(): Flow<ValoVersion?> {
        return database.valoVersionDao().observe()
            .map { it?.toModel() }
            .flowOn(Dispatchers.IO)
    }

    @OptIn(SupabaseExperimental::class)
    override fun observeRemote(): Flow<ValoVersion> {
        return supabase.from("valo_version")
            .selectSingleValueAsFlow(ValoVersionDto::id) {
                ValoVersionDto::id eq 0
            }
            .map { it.toModel() }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun upsert(version: ValoVersion) {
        database.valoVersionDao().upsert(ValoVersionEntity.fromModel(version))
    }
}