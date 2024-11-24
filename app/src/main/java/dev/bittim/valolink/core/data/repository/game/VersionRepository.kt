package dev.bittim.valolink.core.data.repository.game

import dev.bittim.valolink.main.domain.model.game.Version
import kotlinx.coroutines.flow.Flow

interface VersionRepository {
    suspend fun get(): Flow<Version>
}