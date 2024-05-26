package dev.bittim.valolink.feature.main.data.repository.game

import dev.bittim.valolink.feature.main.domain.model.game.Season
import kotlinx.coroutines.flow.Flow

interface SeasonRepository {
    suspend fun getSeason(uuid: String, providedVersion: String? = null): Flow<Season>
    suspend fun getAllSeasons(providedVersion: String? = null): Flow<List<Season>>

    suspend fun fetchSeason(uuid: String, version: String)
    suspend fun fetchSeasons(version: String)
}