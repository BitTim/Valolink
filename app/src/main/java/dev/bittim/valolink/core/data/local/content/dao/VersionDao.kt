package dev.bittim.valolink.core.data.local.content.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.core.data.local.content.entity.VersionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VersionDao {
	// --------------------------------
	//  Upsert
	// --------------------------------

	@Upsert
	suspend fun upsert(version: VersionEntity)

	// --------------------------------
	//  Query
	// --------------------------------

	@Query("SELECT * FROM Version WHERE id = 0 LIMIT 1")
	fun get(): Flow<VersionEntity?>
}