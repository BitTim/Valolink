/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       CurrencyDao.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.content.data.local.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
	// --------------------------------
	//  Upsert
	// --------------------------------

	@Upsert
	suspend fun upsert(currency: CurrencyEntity)

	@Transaction
	@Upsert
	suspend fun upsert(currencies: Set<CurrencyEntity>)

	// --------------------------------
	//  Query
	// --------------------------------

	@Query("SELECT * FROM Currencies WHERE uuid = :uuid LIMIT 1")
	fun getByUuid(uuid: String): Flow<CurrencyEntity?>

	@Query("SELECT * FROM Currencies")
	fun getAll(): Flow<List<CurrencyEntity>>
}