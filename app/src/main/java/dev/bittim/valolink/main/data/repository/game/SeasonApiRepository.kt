package dev.bittim.valolink.main.data.repository.game

import androidx.room.withTransaction
import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.domain.model.game.Season
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SeasonApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
) : SeasonRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getSeason(
        uuid: String,
        providedVersion: String?,
    ): Flow<Season> {
        return gameDatabase.seasonDao.getByUuid(uuid).distinctUntilChanged().combineTransform(
            versionRepository.get()
        ) { season, apiVersion ->
            val version = providedVersion ?: apiVersion.version

            if (season == null || season.version != version) {
                fetchSeason(
                    uuid,
                    version
                )
            } else {
                emit(season)
            }
        }.map { it.toType() }
    }

    // -------- [ Bulk queries ] --------

    override suspend fun getAllSeasons(providedVersion: String?): Flow<List<Season>> {
        return gameDatabase.seasonDao.getAll().distinctUntilChanged().combineTransform(
            versionRepository.get()
        ) { seasons, apiVersion ->
            val version = providedVersion ?: apiVersion.version

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

    override suspend fun fetchSeason(
        uuid: String,
        version: String,
    ) {
        val response = gameApi.getSeason(uuid)
        if (response.isSuccessful) {
            gameDatabase.withTransaction {
                gameDatabase.seasonDao.upsert(response.body()!!.data!!.toEntity(version))
            }
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchSeasons(version: String) {
        val response = gameApi.getAllSeasons()
        if (response.isSuccessful) {
            gameDatabase.withTransaction {
                gameDatabase.seasonDao.upsert(response.body()!!.data!!.map {
                    it.toEntity(version)
                })
            }
        }
    }
}