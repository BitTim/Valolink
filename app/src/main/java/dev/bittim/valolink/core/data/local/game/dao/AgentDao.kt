package dev.bittim.valolink.core.data.local.game.dao

import androidx.room.Dao
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.core.data.local.game.entity.agent.AbilityEntity
import dev.bittim.valolink.core.data.local.game.entity.agent.AgentEntity
import dev.bittim.valolink.core.data.local.game.entity.agent.RoleEntity
import dev.bittim.valolink.core.data.local.game.relation.agent.AgentWithRoleAndAbilities
import kotlinx.coroutines.flow.Flow

@Dao
interface AgentDao {
	// --------------------------------
	//  Upsert
	// --------------------------------

	@Transaction
	@Upsert
	suspend fun upsert(
		role: RoleEntity,
		agent: AgentEntity,
		abilities: Set<AbilityEntity>,
	)

	@Transaction
	@Upsert
	suspend fun upsert(
		roles: Set<RoleEntity>,
		agents: Set<AgentEntity>,
		abilities: Set<AbilityEntity>,
	)

	// --------------------------------
	//  Query
	// --------------------------------

	@Transaction
	@Query("SELECT * FROM Agents WHERE uuid = :uuid LIMIT 1")
	fun getByUuid(uuid: String): Flow<AgentWithRoleAndAbilities?>

	@Transaction
	@Query("SELECT * FROM Agents")
	fun getAll(): Flow<List<AgentWithRoleAndAbilities>>

	@Query("SELECT uuid, version FROM Agents WHERE isBaseContent = TRUE")
	fun getBase(): Flow<Map<@MapColumn(columnName = "uuid") String, @MapColumn(columnName = "version") String>>
}