package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.domain.model.game.Spray
import kotlinx.coroutines.flow.Flow

interface SprayRepository {
    suspend fun getSpray(
        uuid: String,
        providedVersion: String? = null,
    ): Flow<Spray>

    suspend fun fetchSpray(
        uuid: String,
        version: String,
    )
}