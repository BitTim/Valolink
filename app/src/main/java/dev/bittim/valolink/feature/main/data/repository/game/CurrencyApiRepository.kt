package dev.bittim.valolink.feature.main.data.repository.game

import dev.bittim.valolink.feature.main.data.local.game.GameDatabase
import dev.bittim.valolink.feature.main.data.remote.game.GameApi
import dev.bittim.valolink.feature.main.domain.model.game.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class CurrencyApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository
) : CurrencyRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getCurrency(uuid: String, providedVersion: String?): Flow<Currency> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.currencyDao.getCurrency(uuid).distinctUntilChanged()
            .transform { entity ->
                if (entity == null || entity.version != version) {
                    fetchCurrency(uuid, version)
                } else {
                    emit(entity)
                }
            }.map { it.toType() }
    }

    // --------------------------------
    //  Fetching from API
    // --------------------------------

    // -------- [ Single fetching ] --------

    override suspend fun fetchCurrency(uuid: String, version: String) {
        val response = gameApi.getCurrency(uuid)
        if (response.isSuccessful) {
            gameDatabase.currencyDao.upsertCurrency(response.body()!!.data!!.toEntity(version))
        }
    }
}