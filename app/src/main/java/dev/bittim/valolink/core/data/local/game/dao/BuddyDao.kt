package dev.bittim.valolink.core.data.local.game.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.core.data.local.game.entity.buddy.BuddyEntity
import dev.bittim.valolink.core.data.local.game.entity.buddy.BuddyLevelEntity
import dev.bittim.valolink.core.data.local.game.relation.buddy.BuddyWithLevels
import kotlinx.coroutines.flow.Flow

@Dao
interface BuddyDao {
	// --------------------------------
	//  Upsert
	// --------------------------------

	@Transaction
	@Upsert
	suspend fun upsert(
		buddy: BuddyEntity,
		levels: Set<BuddyLevelEntity>,
	)

	@Transaction
	@Upsert
	suspend fun upsert(
		buddy: Set<BuddyEntity>,
		levels: Set<BuddyLevelEntity>,
	)

	// --------------------------------
	//  Query
	// --------------------------------

	@Transaction
	@Query("SELECT * FROM Buddies")
	fun getAll(): Flow<List<BuddyWithLevels>>

	@Transaction
	@Query("SELECT * FROM Buddies WHERE uuid = :uuid LIMIT 1")
	fun getByUuid(uuid: String): Flow<BuddyWithLevels?>

	@Transaction
	@Query(
		"""
        SELECT * FROM Buddies
        WHERE uuid = (
            SELECT buddy FROM BuddyLevels
            WHERE uuid = :levelUuid
        ) LIMIT 1
    """
	)
	fun getByLevelUuid(levelUuid: String): Flow<BuddyWithLevels?>
}