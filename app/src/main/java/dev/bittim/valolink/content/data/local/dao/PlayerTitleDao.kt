package dev.bittim.valolink.content.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.content.data.local.entity.PlayerTitleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerTitleDao {
	// --------------------------------
	//  Upsert
	// --------------------------------

	@Upsert
	suspend fun upsert(playerTitle: PlayerTitleEntity)

	@Transaction
	@Upsert
	suspend fun upsert(playerTitles: Set<PlayerTitleEntity>)

	// --------------------------------
	//  Query
	// --------------------------------

	@Query("SELECT * FROM PlayerTitles WHERE uuid = :uuid LIMIT 1")
	fun getByUuid(uuid: String): Flow<PlayerTitleEntity?>

	@Query("SELECT * FROM PlayerTitles")
	fun getAll(): Flow<List<PlayerTitleEntity>>
}