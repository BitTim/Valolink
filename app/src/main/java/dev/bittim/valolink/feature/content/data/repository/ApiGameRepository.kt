package dev.bittim.valolink.feature.content.data.repository

import androidx.room.withTransaction
import dev.bittim.valolink.core.domain.Result
import dev.bittim.valolink.feature.content.data.local.GameDatabase
import dev.bittim.valolink.feature.content.data.mapper.toSeason
import dev.bittim.valolink.feature.content.data.mapper.toSeasonEntity
import dev.bittim.valolink.feature.content.data.remote.GameApi
import dev.bittim.valolink.feature.content.data.remote.VersionDto
import dev.bittim.valolink.feature.content.domain.model.Season
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import retrofit2.HttpException
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

    override suspend fun getAllSeasons(): Flow<List<Season>> {
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

        // Get Flow from cache
        return gameDatabase.dao.getAllSeasons().onEach { entities ->
            // If cache is invalid, fetch from API and put into cache
            if (entities.isEmpty() || entities.any { it.version != version }) {
                val seasonsResponse = gameApi.getSeasons()
                if (seasonsResponse.isSuccessful &&
                    seasonsResponse.body() != null &&
                    seasonsResponse.body()!!.data != null
                ) {
                    gameDatabase.withTransaction {
                        gameDatabase.dao.upsertAll(seasonsResponse.body()!!.data!!.map {
                            it.toSeasonEntity(
                                version
                            )
                        })
                    }
                }
            }
        }.map { entities ->
            entities.map { it.toSeason() }
        }
    }
}