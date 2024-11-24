package dev.bittim.valolink.core.data.local.game.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.core.data.local.game.entity.SeasonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeasonDao {
	// --------------------------------
	//  Upsert
	// --------------------------------

	@Upsert
	suspend fun upsert(season: SeasonEntity)

	@Upsert
	suspend fun upsert(seasons: List<SeasonEntity>)

	// --------------------------------
	//  Query
	// --------------------------------

	@Query("SELECT * FROM Seasons WHERE uuid = :uuid LIMIT 1")
	fun getByUuid(uuid: String): Flow<SeasonEntity?>

	@Query("SELECT * FROM Seasons ORDER BY startTime DESC")
	fun getAll(): Flow<List<SeasonEntity>>
}