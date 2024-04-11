package dev.bittim.valolink.feature.content.data.repository

import androidx.room.withTransaction
import dev.bittim.valolink.core.domain.Result
import dev.bittim.valolink.feature.content.data.local.game.GameDatabase
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity
import dev.bittim.valolink.feature.content.data.remote.game.GameApi
import dev.bittim.valolink.feature.content.data.remote.game.GameApiResponse
import dev.bittim.valolink.feature.content.data.remote.game.dto.GameDto
import dev.bittim.valolink.feature.content.data.remote.game.dto.VersionDto
import dev.bittim.valolink.feature.content.domain.model.Map
import dev.bittim.valolink.feature.content.domain.model.Season
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class ApiGameRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi
) : GameRepository {
    override suspend fun getApiVersion(): Result<VersionDto, GameRepository.GameDataError> {
        val versionResponse = try {
            gameApi.getVersion()
        } catch (e: IOException) {
            e.printStackTrace()
            return Result.Error(GameRepository.GameDataError.IO)
        } catch (e: HttpException) {
            e.printStackTrace()
            return Result.Error(GameRepository.GameDataError.HTTP)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(GameRepository.GameDataError.GENERAL)
        }

        return if (versionResponse.isSuccessful &&
            versionResponse.body() != null &&
            versionResponse.body()!!.data != null
        ) {
            Result.Success(versionResponse.body()!!.data!!)
        } else {
            Result.Error(GameRepository.GameDataError.GENERAL)
        }
    }

    private suspend fun <T, E : GameEntity<T>, D : GameDto<E>> getWithCache(
        cacheQuery: () -> Flow<List<E>>,
        cacheUpsert: suspend (List<E>) -> Unit,
        remoteQuery: suspend () -> Response<GameApiResponse<List<D>>>
    ): Flow<List<T>> {
        // Get current remote API version
        val versionResult = getApiVersion()
        if (versionResult !is Result.Success) {
            return flow {
                emit(listOf())
            }
        }

        val version = versionResult.data.version
        if (version.isEmpty()) {
            return flow {
                emit(listOf())
            }
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
        return getWithCache(
            gameDatabase.dao::getAllSeasons,
            gameDatabase.dao::upsertAllSeasons,
            gameApi::getSeasons
        )
    }

    override suspend fun getAllMaps(): Flow<List<Map>> {
        return getWithCache(
            gameDatabase.dao::getAllMaps,
            gameDatabase.dao::upsertAllMaps,
            gameApi::getMaps
        )
    }
}