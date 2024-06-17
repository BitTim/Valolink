package dev.bittim.valolink.main.data.repository.game

import dev.bittim.valolink.main.domain.model.game.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun getByUuid(
        uuid: String,
        providedVersion: String? = null,
    ): Flow<Currency>

    suspend fun getAll(providedVersion: String?): Flow<List<Currency>>

    suspend fun fetch(
        uuid: String,
        version: String,
    )

    suspend fun fetchAll(version: String)

    fun queueWorker(version: String, uuid: String? = null)
}