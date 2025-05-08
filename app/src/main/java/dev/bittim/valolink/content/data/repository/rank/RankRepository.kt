/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.content.data.repository.rank

import dev.bittim.valolink.content.data.repository.ContentRepository
import dev.bittim.valolink.content.domain.model.rank.Rank
import dev.bittim.valolink.content.domain.model.rank.RankTable
import kotlinx.coroutines.flow.Flow

interface RankRepository : ContentRepository<Rank> {
    suspend fun getByTier(rankTable: String, tier: Int): Flow<Rank?>
    suspend fun getTableByUuid(uuid: String): Flow<RankTable?>

    suspend fun getAllByTable(rankTable: String): Flow<List<Rank>>
    suspend fun getAllByLatestTable(): Flow<List<Rank>>

    suspend fun getAllRankTables(): Flow<List<RankTable>>
}
