/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       SupabaseValoRankTableRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   23.06.26, 03:15
 */

package dev.bittim.valolink.core.data.repo

import dev.bittim.valolink.core.data.local.Database
import dev.bittim.valolink.core.data.remote.dto.ValoRankTableDto
import dev.bittim.valolink.core.domain.repo.ValoRankTableRepo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class SupabaseValoRankTableRepo(
    private val database: Database,
    private val supabase: SupabaseClient
) : ValoRankTableRepo {
    override suspend fun sync() {
        val tables = supabase.from("valo_rank_tables").select().decodeList<ValoRankTableDto>()
        database.valoRankTableDao().upsert(tables.map { it.toEntity() })
    }
}