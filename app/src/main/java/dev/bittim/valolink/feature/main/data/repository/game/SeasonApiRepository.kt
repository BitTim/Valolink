package dev.bittim.valolink.feature.main.data.repository.game

import androidx.room.withTransaction
import dev.bittim.valolink.feature.main.data.local.game.GameDatabase
import dev.bittim.valolink.feature.main.data.remote.game.GameApi
import dev.bittim.valolink.feature.main.domain.model.game.Season
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class SeasonApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository
) : SeasonRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getSeason(uuid: String, providedVersion: String?): Flow<Season> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.seasonDao.getSeason(uuid).distinctUntilChanged().transform { season ->
            if (season == null || season.version != version) {
                fetchSeason(uuid, version)
            } else {
                emit(season)
            }
        }.map { it.toType() }
    }

    // -------- [ Bulk queries ] --------

    override suspend fun getAllSeasons(providedVersion: String?): Flow<List<Season>> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.seasonDao.getAllSeasons().distinctUntilChanged().transform { seasons ->
            if (seasons.isEmpty() || seasons.any { it.version != version }) {
                fetchSeasons(version)
            } else {
                emit(seasons)
            }
        }.map { seasons ->
            seasons.map { it.toType() }
        }
    }

    // --------------------------------
    //  Fetching from API
    // --------------------------------

    // -------- [ Single fetching ] --------

    override suspend fun fetchSeason(uuid: String, version: String) {
        val response = gameApi.getSeason(uuid)
        if (response.isSuccessful) {
            gameDatabase.withTransaction {
                gameDatabase.seasonDao.upsertSeason(response.body()!!.data!!.toEntity(version))
            }
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchSeasons(version: String) {
        val response = gameApi.getAllSeasons()
        if (response.isSuccessful) {
            gameDatabase.withTransaction {
                gameDatabase.seasonDao.upsertAllSeasons(response.body()!!.data!!.map {
                    it.toEntity(version)
                })
            }
        }
    }
}