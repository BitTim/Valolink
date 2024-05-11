package dev.bittim.valolink.feature.content.data.repository.game

import dev.bittim.valolink.feature.content.data.local.game.GameDatabase
import dev.bittim.valolink.feature.content.data.remote.game.GameApi
import dev.bittim.valolink.feature.content.domain.model.Spray
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class SprayApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository
) : SprayRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getSpray(uuid: String, providedVersion: String?): Flow<Spray> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.sprayDao.getSpray(uuid).distinctUntilChanged().transform { entity ->
                if (entity == null || entity.version != version) {
                    fetchSpray(uuid, version)
                } else {
                    emit(entity)
                }
            }.map { it.toType() }
    }

    // --------------------------------
    //  Fetching from API
    // --------------------------------

    // -------- [ Single fetching ] --------

    override suspend fun fetchSpray(uuid: String, version: String) {
        val response = gameApi.getSpray(uuid)
        if (response.isSuccessful) {
            gameDatabase.sprayDao.upsertSpray(response.body()!!.data!!.toEntity(version))
        }
    }
}