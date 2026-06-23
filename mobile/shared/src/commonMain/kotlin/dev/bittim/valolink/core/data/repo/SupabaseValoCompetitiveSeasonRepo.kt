/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseValoCompetitiveSeasonRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 03:04
 */

package dev.bittim.valolink.core.data.repo

import dev.bittim.valolink.core.data.local.Database
import dev.bittim.valolink.core.data.remote.dto.ValoCompetitiveSeasonDto
import dev.bittim.valolink.core.domain.model.ValoCompetitiveSeason
import dev.bittim.valolink.core.domain.repo.ValoCompetitiveSeasonRepo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

class SupabaseValoCompetitiveSeasonRepo(
    private val database: Database,
    private val supabase: SupabaseClient
) : ValoCompetitiveSeasonRepo {
    override fun observe(uuid: Uuid): Flow<ValoCompetitiveSeason?> {
        return database.valoCompetitiveSeasonDao().get(uuid)
            .map { it?.toModel() }
            .flowOn(Dispatchers.IO)
    }

    override fun observeBySeason(season: Uuid): Flow<ValoCompetitiveSeason?> {
        return database.valoCompetitiveSeasonDao().getBySeason(season)
            .map { it?.toModel() }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun sync() {
        val competitiveSeason = supabase.from("valo_competitive_seasons").select().decodeList<ValoCompetitiveSeasonDto>()
        database.valoCompetitiveSeasonDao().upsert(competitiveSeason.map { it.toEntity() })
    }
}