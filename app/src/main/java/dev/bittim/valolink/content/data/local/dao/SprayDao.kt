package dev.bittim.valolink.content.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.content.data.local.entity.SprayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SprayDao {
	// --------------------------------
	//  Upsert
	// --------------------------------

	@Upsert
	suspend fun upsert(spray: SprayEntity)

	@Transaction
	@Upsert
	suspend fun upsert(sprays: Set<SprayEntity>)

	// --------------------------------
	//  Query
	// --------------------------------

	@Query("SELECT * FROM Sprays WHERE uuid = :uuid LIMIT 1")
	fun getByUuid(uuid: String): Flow<SprayEntity?>

	@Query("SELECT * FROM Sprays")
	fun getAll(): Flow<List<SprayEntity>>
}