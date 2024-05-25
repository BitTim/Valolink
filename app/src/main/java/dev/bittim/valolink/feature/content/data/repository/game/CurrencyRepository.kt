package dev.bittim.valolink.feature.content.data.repository.game

import dev.bittim.valolink.feature.content.domain.model.game.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun getCurrency(uuid: String, providedVersion: String? = null): Flow<Currency>

    suspend fun fetchCurrency(uuid: String, version: String)
}