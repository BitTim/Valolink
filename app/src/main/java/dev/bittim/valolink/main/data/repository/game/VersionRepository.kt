package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.data.remote.game.dto.VersionDto

interface VersionRepository {
    suspend fun getApiVersion(): VersionDto?
}