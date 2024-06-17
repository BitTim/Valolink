package dev.bittim.valolink.main.data.repository.game

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.data.remote.game.dto.weapon.WeaponDto
import dev.bittim.valolink.main.data.worker.game.SeasonSyncWorker
import dev.bittim.valolink.main.data.worker.game.WeaponSyncWorker
import dev.bittim.valolink.main.domain.model.game.weapon.skins.WeaponSkin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeaponApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
    private val workManager: WorkManager
) : WeaponRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getSkinByLevelUuid(
        levelUuid: String,
        providedVersion: String?,
    ): Flow<WeaponSkin> {

        return gameDatabase.weaponDao
            .getSkinByLevelUuid(levelUuid)
            .distinctUntilChanged()
            .combineTransform(
                versionRepository.get()
            ) { entity, apiVersion ->
                val version = providedVersion ?: apiVersion.version

                if (entity == null || entity.weaponSkin.version != version) {
                    queueWorker(version)
                } else {
                    emit(entity)
                }
            }
            .map { it.toType() }
    }

    // --------------------------------
    //  Fetching from API
    // --------------------------------

    // -------- [ Single fetching ] --------

    override suspend fun fetch(
        uuid: String,
        version: String,
    ) {
        val response = gameApi.getWeapon(uuid)
        if (response.isSuccessful) {
            upsertDto(
                listOf(response.body()!!.data!!),
                version
            )
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchAll(version: String) {
        val response = gameApi.getAllWeapons()
        if (response.isSuccessful) {
            upsertDto(
                response.body()!!.data!!,
                version
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
                version,
                weapon.uuid
            )
        }.filterNotNull()

        val adsStatsDto = statsDto.map { it?.adsStats }
        val adsStats = adsStatsDto.zip(stats) { adsStat, stat ->
            adsStat?.toEntity(
                version,
                stat.uuid
            )
        }.filterNotNull()

        val airBurstStatsDto = statsDto.map { it?.airBurstStats }
        val airBurstStats = airBurstStatsDto.zip(stats) { airBurstStat, stat ->
            airBurstStat?.toEntity(
                version,
                stat.uuid
            )
        }.filterNotNull()

        val altShotgunStatsDto = statsDto.map { it?.altShotgunStats }
        val altShotgunStats = altShotgunStatsDto.zip(stats) { altShotgunStat, stat ->
            altShotgunStat?.toEntity(
                version,
                stat.uuid
            )
        }.filterNotNull()

        val damageRangeDto = statsDto.flatMap { it?.damageRanges ?: listOf() }
        val damageRanges = damageRangeDto.zip(stats) { damageRange, stat ->
            damageRange.toEntity(
                version,
                stat.uuid
            )
        }

        // Shop Data
        val shopDataDto = weaponDto.map { it.shopData }
        val shopDataList = shopDataDto.zip(weapons) { shopData, weapon ->
            shopData?.toEntity(
                version,
                weapon.uuid
            )
        }.filterNotNull()

        val gridPositionDto = shopDataDto.map { it?.gridPosition }
        val gridPositions = gridPositionDto.zip(shopDataList) { gridPosition, shopData ->
            gridPosition?.toEntity(
                version,
                shopData.uuid
            )
        }.filterNotNull()

        // Skins
        val skinDto = weaponDto.map { it.skins }
        val skins = skinDto.zip(weapons) { skin, weapon ->
            skin.map {
                it.toEntity(
                    version,
                    weapon.uuid
                )
            }
        }.flatten()

        val skinChromaDto = skinDto.flatMap { skin ->
            skin.map { it.chromas }
        }
        val skinChromas = skinChromaDto.zip(skins) { chroma, skin ->
            chroma.map {
                it.toEntity(
                    version,
                    skin.uuid
                )
            }
        }.flatten()

        val skinLevelDto = skinDto.flatMap { level ->
            level.map { it.levels }
        }
        val skinLevels = skinLevelDto.zip(skins) { level, skin ->
            level.map {
                it.toEntity(
                    version,
                    skin.uuid
                )
            }
        }.flatten()

        gameDatabase.weaponDao.upsert(
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

    override fun queueWorker(version: String, uuid: String?) {
        val workRequest = OneTimeWorkRequestBuilder<WeaponSyncWorker>()
            .setInputData(
                workDataOf(
                    WeaponSyncWorker.KEY_WEAPON_UUID to uuid,
                    WeaponSyncWorker.KEY_VERSION to version
                )
            )
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()
        workManager.enqueueUniqueWork(
            WeaponSyncWorker.WORK_NAME,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            workRequest
        )
    }
}