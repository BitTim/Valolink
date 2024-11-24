package dev.bittim.valolink.main.data.local.user.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.main.data.local.user.entity.UserAgentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAgentDao {
	// --------------------------------
	//  Upsert
	// --------------------------------

	@Upsert
	suspend fun upsert(agent: UserAgentEntity)

	@Transaction
	@Upsert
	suspend fun upsert(agent: Set<UserAgentEntity>)

	// --------------------------------
	//  Query
	// --------------------------------

	@Query("SELECT * FROM UserAgents WHERE uuid = :uuid LIMIT 1")
	fun getByUuid(uuid: String): Flow<UserAgentEntity?>

	@Query("SELECT * FROM UserAgents WHERE user = :uid")
	fun getByUser(uid: String): Flow<List<UserAgentEntity>>

	@Query("SELECT * FROM UserAgents WHERE user = :uid AND agent = :agent LIMIT 1")
	fun getByUserAndAgent(
		uid: String,
		agent: String,
	): Flow<UserAgentEntity?>

	@Query("SELECT updatedAt FROM UserAgents WHERE uuid = :uuid LIMIT 1")
	fun getUpdatedAtByUuid(uuid: String): Flow<String?>

	@Query("SELECT * FROM UserAgents WHERE isSynced = false ORDER BY updatedAt ASC")
	fun getSyncQueue(): Flow<List<UserAgentEntity?>>

	// --------------------------------
	//  Delete
	// --------------------------------

	@Delete
	suspend fun delete(agent: UserAgentEntity)

	@Query("DELETE FROM UserAgents WHERE uuid = :uuid")
	suspend fun deleteByUuid(uuid: String)

	@Query("DELETE FROM UserAgents WHERE user = :uid AND agent = :agent")
	fun deleteByUserAndAgent(uid: String, agent: String)

	@Query("DELETE FROM UserAgents WHERE user = :uid")
	suspend fun deleteByUser(uid: String)

	@Query("DELETE FROM UserAgents")
	suspend fun deleteAll()
}