package dev.bittim.valolink.feature.content.data.repository.game

import dev.bittim.valolink.feature.content.data.remote.game.dto.VersionDto

interface VersionRepository {
    suspend fun getApiVersion(): VersionDto?
}