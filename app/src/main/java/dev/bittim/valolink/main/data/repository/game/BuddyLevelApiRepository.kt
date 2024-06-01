package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.domain.model.game.buddy.BuddyLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class BuddyLevelApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
) : BuddyLevelRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getBuddyLevel(
        uuid: String,
        providedVersion: String?,
    ): Flow<BuddyLevel> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.buddyLevelDao
            .getBuddyLevel(uuid)
            .distinctUntilChanged()
            .transform { entity ->
                if (entity == null || entity.version != version) {
                    fetchBuddyLevel(
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

    override suspend fun fetchBuddyLevel(
        uuid: String,
        version: String,
    ) {
        val response = gameApi.getBuddyLevel(uuid)
        if (response.isSuccessful) {
            gameDatabase.buddyLevelDao.upsertBuddyLevel(response.body()!!.data!!.toEntity(version))
        }
    }
}