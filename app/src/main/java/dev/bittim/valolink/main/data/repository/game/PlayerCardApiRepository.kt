package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.domain.model.game.PlayerCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerCardApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
) : PlayerCardRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getPlayerCard(
        uuid: String,
        providedVersion: String?,
    ): Flow<PlayerCard> {
        return gameDatabase.playerCardDao
            .getByUuid(uuid)
            .distinctUntilChanged()
            .combineTransform(
                versionRepository.get()
            ) { entity, apiVersion ->
                val version = providedVersion ?: apiVersion.version

                if (entity == null || entity.version != version) {
                    fetchPlayerCard(
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

    override suspend fun fetchPlayerCard(
        uuid: String,
        version: String,
    ) {
        val response = gameApi.getPlayerCard(uuid)
        if (response.isSuccessful) {
            gameDatabase.playerCardDao.upsert(response.body()!!.data!!.toEntity(version))
        }
    }
}