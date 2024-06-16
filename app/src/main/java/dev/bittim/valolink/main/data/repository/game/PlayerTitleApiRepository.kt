package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.domain.model.game.PlayerTitle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerTitleApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
) : PlayerTitleRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getPlayerTitle(
        uuid: String,
        providedVersion: String?,
    ): Flow<PlayerTitle> {
        return gameDatabase.playerTitleDao
            .getByUuid(uuid)
            .distinctUntilChanged()
            .combineTransform(
                versionRepository.get()
            ) { entity, apiVersion ->
                val version = providedVersion ?: apiVersion.version

                if (entity == null || entity.version != version) {
                    fetchPlayerTitle(
                        uuid,
                        version
                    )
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

    override suspend fun fetchPlayerTitle(
        uuid: String,
        version: String,
    ) {
        val response = gameApi.getPlayerTitle(uuid)
        if (response.isSuccessful) {
            gameDatabase.playerTitleDao.upsert(response.body()!!.data!!.toEntity(version))
        }
    }
}