package dev.bittim.valolink.feature.content.data.repository.game

import dev.bittim.valolink.feature.content.domain.model.Spray
import kotlinx.coroutines.flow.Flow

interface SprayRepository {
    suspend fun getSpray(uuid: String, providedVersion: String? = null): Flow<Spray>

    suspend fun fetchSpray(uuid: String, version: String)
}