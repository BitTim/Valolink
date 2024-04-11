package dev.bittim.valolink.feature.content.data.repository

import dev.bittim.valolink.core.domain.Error
import dev.bittim.valolink.core.domain.Result
import dev.bittim.valolink.feature.content.data.remote.VersionDto
import dev.bittim.valolink.feature.content.domain.model.Season
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun getApiVersion(): Result<VersionDto, GameDataError>
    suspend fun getAllSeasons(): Flow<List<Season>>

    enum class GameDataError : Error {
        IO,
        HTTP,
        GENERAL
    }
}