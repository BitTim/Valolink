/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       RankApiRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   20.04.25, 03:29
 */

package dev.bittim.valolink.content.data.repository.rank

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.content.data.local.ContentDatabase
import dev.bittim.valolink.content.data.remote.ContentApi
import dev.bittim.valolink.content.data.worker.ContentSyncWorker
import dev.bittim.valolink.content.domain.model.rank.Rank
import dev.bittim.valolink.content.domain.model.rank.RankTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class RankApiRepository @Inject constructor(
    private val contentDatabase: ContentDatabase,
    private val contentApi: ContentApi,
    private val workManager: WorkManager,
) : RankRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getByTier(rankTable: String, tier: Int): Flow<Rank?> {
        return try {
            // Get from local database
            val local =
                contentDatabase.rankDao.getByTier(rankTable, tier).distinctUntilChanged()
                    .map { it?.toType() }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker()

            // Return
            local
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getByUuid(uuid: String): Flow<Rank?> {
        return try {
            // Get from local database
            val local =
                contentDatabase.rankDao.getByUuid(uuid).distinctUntilChanged().map { it?.toType() }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker(uuid)

            // Return
            local
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getTableByUuid(uuid: String): Flow<RankTable?> {
        return try {
            // Get from local database
            val local =
                contentDatabase.rankDao.getTableByUuid(uuid).distinctUntilChanged()
                    .map { it?.toType() }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker(uuid)

            // Return
            local
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    // -------- [ Bulk queries ] --------

    override suspend fun getAll(): Flow<List<Rank>> {
        return try {
            // Get from local database
            val local = contentDatabase.rankDao.getAll().distinctUntilChanged()
                .map { entities -> entities.map { it.toType() } }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker()

            // Return
            local
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getAllByTable(rankTable: String): Flow<List<Rank>> {
        return try {
            // Get from local database
            val local = contentDatabase.rankDao.getAllByTable(rankTable).distinctUntilChanged()
                .map { entities -> entities.map { it.toType() } }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker()

            // Return
            local
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getAllByLatestTable(): Flow<List<Rank>> {
        return try {
            // Get from local database
            val local = contentDatabase.rankDao.getAllByLatestTable().distinctUntilChanged()
                .map { entities -> entities.map { it.toType() } }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker()

            // Return
            local
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getAllRankTables(): Flow<List<RankTable>> {
        return try {
            // Get from local database
            val local = contentDatabase.rankDao.getAllTables().distinctUntilChanged()
                .map { entities -> entities.map { it.toType() } }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker()

            // Return
            local
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    // --------------------------------
    //  Fetching from API
    // --------------------------------

    // -------- [ Single fetching ] --------

    override suspend fun fetch(uuid: String, version: String) {
        val response = contentApi.getRankTable(uuid)
        if (response.isSuccessful) {
            val rankTable = response.body()!!.data!!
            val ranks = rankTable.tiers.filter { !it.division.contains("INVALID") }
                .map { it.toEntity(rankTable.uuid, version) }

            contentDatabase.rankDao.upsert(rankTable.toEntity(version), ranks.distinct().toSet())
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchAll(version: String) {
        val response = contentApi.getAllRankTables()
        if (response.isSuccessful) {
            val rankTables = response.body()!!.data!!
            val ranks = rankTables.map { rankTable ->
                rankTable.tiers.filter { !it.division.contains("INVALID") }
                    .map { it.toEntity(rankTable.uuid, version) }
            }.flatten()

            contentDatabase.rankDao.upsert(
                rankTables.map { it.toEntity(version) }.distinct().toSet(),
                ranks.distinct().toSet()
            )
        }
    }

    // ================================
    //  Queue Worker
    // ================================

    override fun queueWorker(uuid: String?) {
        val workRequest = OneTimeWorkRequestBuilder<ContentSyncWorker>().setInputData(
            workDataOf(
                ContentSyncWorker.KEY_TYPE to Rank::class.simpleName,
                ContentSyncWorker.KEY_UUID to uuid,
            )
        ).setConstraints(Constraints(NetworkType.CONNECTED)).build()
        workManager.enqueueUniqueWork(
            Rank::class.simpleName + ContentSyncWorker.WORK_BASE_NAME + if (!uuid.isNullOrEmpty()) "_$uuid" else "",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }
}
