package dev.bittim.valolink.feature.content.data.repository

import dev.bittim.valolink.core.domain.Error
import dev.bittim.valolink.feature.content.data.remote.game.dto.VersionDto
import dev.bittim.valolink.feature.content.domain.model.Season
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun getApiVersion(): VersionDto?
    suspend fun getAllSeasons(): Flow<List<Season>>

    enum class GameDataError : Error {
        IO,
        HTTP,
        GENERAL
    }

    suspend fun getAllContracts(): Flow<List<Contract>>
}