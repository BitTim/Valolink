package dev.bittim.valolink.feature.main.data.local.game.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.feature.main.data.local.game.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsertCurrency(currency: CurrencyEntity)

    // --------------------------------
    //  Query
    // --------------------------------

    @Query("SELECT * FROM Currencies WHERE uuid = :uuid LIMIT 1")
    fun getCurrency(uuid: String): Flow<CurrencyEntity?>
}