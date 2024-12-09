package dev.bittim.valolink.content.data.repository.version

import dev.bittim.valolink.content.domain.model.Version
import kotlinx.coroutines.flow.Flow

interface VersionRepository {
    suspend fun get(): Flow<Version>
}