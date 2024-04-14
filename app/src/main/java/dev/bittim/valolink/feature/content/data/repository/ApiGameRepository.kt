package dev.bittim.valolink.feature.content.data.repository

import androidx.room.withTransaction
import dev.bittim.valolink.feature.content.data.local.game.GameDatabase
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.data.remote.game.GameApi
import dev.bittim.valolink.feature.content.data.remote.game.GameApiResponse
import dev.bittim.valolink.feature.content.data.remote.game.dto.GameDto
import dev.bittim.valolink.feature.content.data.remote.game.dto.VersionDto
import dev.bittim.valolink.feature.content.domain.model.Season
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import retrofit2.Response
import javax.inject.Inject

class ApiGameRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi
) : GameRepository {
    override suspend fun getApiVersion(): VersionDto? {
        val versionResponse = try {
            gameApi.getVersion()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return if (versionResponse.isSuccessful &&
            versionResponse.body() != null &&
            versionResponse.body()!!.data != null
        ) {
            versionResponse.body()!!.data!!
        } else {
            null
        }
    }

    private suspend fun <T, E : GameEntity<T>, D : GameDto<E>> getWithCache(
        uuid: String,
        cacheQuery: (String) -> Flow<E>,
        cacheUpsert: suspend (String, E) -> Unit,
        remoteQuery: suspend (String) -> Response<GameApiResponse<D>>
    ): Flow<T> {
        // Get current remote API version
        val version = getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return cacheQuery.invoke(uuid).onEach { entity ->
            if (entity.version != version) {
                val response = remoteQuery.invoke(uuid)

                if (response.isSuccessful &&
                    response.body() != null &&
                    response.body()!!.data != null
                ) {
                    gameDatabase.withTransaction {
                        cacheUpsert.invoke(
                            uuid,
                            response.body()!!.data!!.toEntity(version)
                        )
                    }
                }
            }
        }.map {
            it.toType()
        }
    }

    private suspend fun <T, E : GameEntity<T>, D : GameDto<E>> getAllWithCache(
        cacheQuery: () -> Flow<List<E>>,
        cacheUpsert: suspend (List<E>) -> Unit,
        remoteQuery: suspend () -> Response<GameApiResponse<List<D>>>
    ): Flow<List<T>> {
        val version = getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return cacheQuery.invoke().onEach { entities ->
            if (entities.isEmpty() || entities.any { it.version != version }) {
                val response = remoteQuery.invoke()
                if (response.isSuccessful &&
                    response.body() != null &&
                    response.body()!!.data != null
                ) {
                    gameDatabase.withTransaction {
                        cacheUpsert.invoke(response.body()!!.data!!.map {
                            it.toEntity(version)
                        })
                    }
                }
            }
        }.map { entities ->
            entities.map { it.toType() }
        }
    }

    override suspend fun getAllSeasons(): Flow<List<Season>> {
        return getAllWithCache(
            gameDatabase.dao::getAllSeasons,
            gameDatabase.dao::upsertAllSeasons,
            gameApi::getSeasons
        )
    }
}