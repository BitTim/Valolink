/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponApiRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.content.data.repository.weapon

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.content.data.local.ContentDatabase
import dev.bittim.valolink.content.data.remote.ContentApi
import dev.bittim.valolink.content.data.remote.dto.weapon.WeaponDto
import dev.bittim.valolink.content.data.worker.ContentSyncWorker
import dev.bittim.valolink.content.domain.model.weapon.Weapon
import dev.bittim.valolink.content.domain.model.weapon.skins.WeaponSkin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class WeaponApiRepository @Inject constructor(
    private val contentDatabase: ContentDatabase,
    private val contentApi: ContentApi,
    private val workManager: WorkManager,
) : WeaponRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getByUuid(uuid: String): Flow<Weapon?> {
        return try {
            // Get from local database
            val local =
                contentDatabase.weaponDao.getByUuid(uuid).distinctUntilChanged()
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

    override suspend fun getSkinByLevelUuid(
        levelUuid: String,
    ): Flow<WeaponSkin?> {
        return try {
            // Get from local database
            val local =
                contentDatabase.weaponDao.getSkinByLevelUuid(levelUuid).distinctUntilChanged()
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

    // -------- [ Bulk queries ] --------

    override suspend fun getAll(): Flow<List<Weapon>> {
        return try {
            // Get from local database
            val local = contentDatabase.weaponDao.getAll().distinctUntilChanged().map { entities ->
                entities.map { it.toType() }
            }

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

    override suspend fun fetch(
        uuid: String,
        version: String,
    ) {
        val response = contentApi.getWeapon(uuid)
        if (response.isSuccessful) {
            upsertDto(
                listOf(response.body()!!.data!!), version
            )
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchAll(version: String) {
        val response = contentApi.getAllWeapons()
        if (response.isSuccessful) {
            upsertDto(
                response.body()!!.data!!, version
            )
        }
    }

    // --------------------------------
    //  Private
    // --------------------------------

    private suspend fun upsertDto(
        data: List<WeaponDto>,
        version: String,
    ) {
        // General weapon data
        val weaponDto = data
        val weapons = weaponDto.map { it.toEntity(version) }

        // Weapon Stats
        val statsDto = weaponDto.map { it.weaponStats }
        val stats = statsDto.zip(weapons) { stat, weapon ->
            stat?.toEntity(
                version, weapon.uuid
            )
        }.filterNotNull()

        val adsStatsDto = statsDto.map { it?.adsStats }
        val adsStats = adsStatsDto.zip(stats) { adsStat, stat ->
            adsStat?.toEntity(
                version, stat.uuid
            )
        }.filterNotNull()

        val airBurstStatsDto = statsDto.map { it?.airBurstStats }
        val airBurstStats = airBurstStatsDto.zip(stats) { airBurstStat, stat ->
            airBurstStat?.toEntity(
                version, stat.uuid
            )
        }.filterNotNull()

        val altShotgunStatsDto = statsDto.map { it?.altShotgunStats }
        val altShotgunStats = altShotgunStatsDto.zip(stats) { altShotgunStat, stat ->
            altShotgunStat?.toEntity(
                version, stat.uuid
            )
        }.filterNotNull()

        val damageRangeDto = statsDto.flatMap { it?.damageRanges ?: emptyList() }
        val damageRanges = damageRangeDto.zip(stats) { damageRange, stat ->
            damageRange.toEntity(
                version, stat.uuid
            )
        }

        // Shop Data
        val shopDataDto = weaponDto.map { it.shopData }
        val shopDataList = shopDataDto.zip(weapons) { shopData, weapon ->
            shopData?.toEntity(
                version, weapon.uuid
            )
        }.filterNotNull()

        val gridPositionDto = shopDataDto.map { it?.gridPosition }
        val gridPositions = gridPositionDto.zip(shopDataList) { gridPosition, shopData ->
            gridPosition?.toEntity(
                version, shopData.uuid
            )
        }.filterNotNull()

        // Skins
        val skinDto = weaponDto.map { it.skins }
        val skins = skinDto.zip(weapons) { skin, weapon ->
            skin.map {
                it.toEntity(
                    version, weapon.uuid
                )
            }
        }.flatten()

        val skinChromaDto = skinDto.flatMap { skin ->
            skin.map { it.chromas }
        }
        val skinChromas = skinChromaDto.zip(skins) { chroma, skin ->
            chroma.mapIndexed { index, rawChroma ->
                rawChroma.toEntity(
                    version, skin.uuid, index
                )
            }
        }.flatten()

        val skinLevelDto = skinDto.flatMap { level ->
            level.map { it.levels }
        }
        val skinLevels = skinLevelDto.zip(skins) { level, skin ->
            level.mapIndexed { index, rawLevel ->
                rawLevel.toEntity(
                    version, skin.uuid, index
                )
            }
        }.flatten()

        contentDatabase.weaponDao.upsert(
            weapons.distinct().toSet(),
            stats.distinct().toSet(),
            adsStats.distinct().toSet(),
            altShotgunStats.distinct().toSet(),
            airBurstStats.distinct().toSet(),
            damageRanges.distinct().toSet(),
            shopDataList.distinct().toSet(),
            gridPositions.distinct().toSet(),
            skins.distinct().toSet(),
            skinChromas.distinct().toSet(),
            skinLevels.distinct().toSet(),
        )
    }

    // ================================
    //  Queue Worker
    // ================================

    override fun queueWorker(
        uuid: String?,
    ) {
        val workRequest = OneTimeWorkRequestBuilder<ContentSyncWorker>().setInputData(
            workDataOf(
                ContentSyncWorker.KEY_TYPE to Weapon::class.simpleName,
                ContentSyncWorker.KEY_UUID to uuid,
            )
        ).setConstraints(Constraints(NetworkType.CONNECTED)).build()
        workManager.enqueueUniqueWork(
            Weapon::class.simpleName + ContentSyncWorker.WORK_BASE_NAME + if (!uuid.isNullOrEmpty()) "_$uuid" else "",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }
}