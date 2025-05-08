/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.content.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.content.data.local.entity.rank.RankEntity
import dev.bittim.valolink.content.data.local.entity.rank.RankTableEntity
import dev.bittim.valolink.content.data.local.relation.rank.RankTableWithRanks
import kotlinx.coroutines.flow.Flow

@Dao
interface RankDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Transaction
    @Upsert
    suspend fun upsert(rankTable: RankTableEntity, rank: RankEntity)

    @Transaction
    @Upsert
    suspend fun upsert(rankTable: RankTableEntity, ranks: Set<RankEntity>)

    @Transaction
    @Upsert
    suspend fun upsert(rankTables: Set<RankTableEntity>, ranks: Set<RankEntity>)

    // --------------------------------
    //  Query
    // --------------------------------

    @Transaction
    @Query("SELECT * FROM Ranks WHERE uuid = :uuid LIMIT 1")
    fun getByUuid(uuid: String): Flow<RankEntity?>

    @Transaction
    @Query("SELECT * FROM Ranks WHERE tier = :tier AND rankTable = :rankTable LIMIT 1")
    fun getByTier(rankTable: String, tier: Int): Flow<RankEntity?>

    @Transaction
    @Query("SELECT * FROM RankTables WHERE uuid = :uuid LIMIT 1")
    fun getTableByUuid(uuid: String): Flow<RankTableWithRanks?>

    @Transaction
    @Query("SELECT * FROM Ranks ORDER BY tier ASC")
    fun getAll(): Flow<List<RankEntity>>

    @Transaction
    @Query("SELECT * FROM Ranks WHERE rankTable = :rankTable ORDER BY tier ASC")
    fun getAllByTable(rankTable: String): Flow<List<RankEntity>>

    @Transaction
    @Query(
        """
        SELECT Ranks.*
        FROM Ranks
        INNER JOIN (
            SELECT uuid
            FROM RankTables
            ORDER BY iteration DESC
            LIMIT 1
        ) AS LatestRankTable ON Ranks.rankTable = LatestRankTable.uuid
        ORDER BY Ranks.tier ASC
    """
    )
    fun getAllByLatestTable(): Flow<List<RankEntity>>

    @Transaction
    @Query("SELECT * FROM RankTables ORDER BY iteration ASC")
    fun getAllTables(): Flow<List<RankTableWithRanks>>

    @Transaction
    @Query("SELECT * FROM RankTables ORDER BY iteration DESC LIMIT 1")
    fun getLatestTable(): Flow<RankTableWithRanks?>
}
