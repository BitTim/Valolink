/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseValoRankRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 03:10
 */

package dev.bittim.valolink.core.data.repo

import dev.bittim.valolink.core.data.local.Database
import dev.bittim.valolink.core.data.remote.dto.ValoRankDto
import dev.bittim.valolink.core.domain.model.ValoRank
import dev.bittim.valolink.core.domain.repo.ValoRankRepo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

class SupabaseValoRankRepo(
    private val database: Database,
    private val supabase: SupabaseClient
) : ValoRankRepo {
    override fun observe(
        rankTable: Uuid,
        tier: Int,
        locale: String?
    ): Flow<ValoRank?> {
        return database.valoRankDao().get(rankTable, tier)
            .map { it?.toModel(locale) }
            .flowOn(Dispatchers.IO)
    }

    override fun observeAll(rankTable: Uuid, locale: String?): Flow<List<ValoRank>> {
        return database.valoRankDao().get(rankTable)
            .map { it.map { entity -> entity.toModel(locale) } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun sync() {
        val ranks = supabase.from("valo_ranks").select().decodeList<ValoRankDto>()
        database.valoRankDao().upsert(ranks.map { it.toEntity() })
    }
}