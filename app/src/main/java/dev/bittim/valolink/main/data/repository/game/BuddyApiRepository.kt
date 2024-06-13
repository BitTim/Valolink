package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.domain.model.game.buddy.Buddy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class BuddyApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
) : BuddyRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getByUuid(
        uuid: String,
        providedVersion: String?,
    ): Flow<Buddy> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.buddyDao.getByUuid(uuid).distinctUntilChanged().transform { entity ->
            if (entity == null || entity.buddy.version != version) {
                fetch(
                    uuid,
                    version
                )
            } else {
                emit(entity)
            }
        }.map { it.toType() }
    }

    override suspend fun getByLevelUuid(
        levelUuid: String,
        providedVersion: String?,
    ): Flow<Buddy> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.buddyDao
            .getByLevelUuid(levelUuid)
            .distinctUntilChanged()
            .transform { entity ->
                if (entity == null || entity.buddy.version != version) {
                    fetchAll(version)
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
        val response = gameApi.getBuddy(uuid)
        if (response.isSuccessful) {
            val buddyDto = response.body()!!.data!!

            val buddy = buddyDto.toEntity(version)
            val levels = buddyDto.levels.map {
                it.toEntity(
                    version,
                    buddy.uuid
                )
            }

            gameDatabase.buddyDao.upsert(
                buddy,
                levels.distinct().toSet()
            )
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchAll(version: String) {
        val response = gameApi.getAllBuddies()
        if (response.isSuccessful) {
            val buddyDto = response.body()!!.data!!

            val buddies = buddyDto.map { it.toEntity(version) }
            val levels = buddyDto.zip(buddies) { level, buddy ->
                level.levels.map {
                    it.toEntity(
                        version,
                        buddy.uuid
                    )
                }
            }.flatten()

            gameDatabase.buddyDao.upsert(
                buddies.distinct().toSet(),
                levels.distinct().toSet()
            )
        }
    }
}