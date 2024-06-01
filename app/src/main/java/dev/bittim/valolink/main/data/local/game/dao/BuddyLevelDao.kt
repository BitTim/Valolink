package dev.bittim.valolink.main.data.local.game.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.bittim.valolink.main.data.local.game.entity.buddy.BuddyLevelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BuddyLevelDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Upsert
    suspend fun upsertBuddyLevel(buddyLevel: BuddyLevelEntity)

    // --------------------------------
    //  Query
    // --------------------------------

    @Query("SELECT * FROM BuddyLevels WHERE uuid = :uuid LIMIT 1")
    fun getBuddyLevel(uuid: String): Flow<BuddyLevelEntity?>
}