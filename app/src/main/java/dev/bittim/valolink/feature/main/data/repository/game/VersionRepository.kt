package dev.bittim.valolink.feature.main.data.repository.game

import dev.bittim.valolink.feature.main.data.remote.game.dto.VersionDto

interface VersionRepository {
    suspend fun getApiVersion(): VersionDto?
}