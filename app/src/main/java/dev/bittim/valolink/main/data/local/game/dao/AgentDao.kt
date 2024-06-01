package dev.bittim.valolink.main.data.local.game.dao

import androidx.room.Dao
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.main.data.local.game.entity.agent.AbilityEntity
import dev.bittim.valolink.main.data.local.game.entity.agent.AgentEntity
import dev.bittim.valolink.main.data.local.game.entity.agent.RoleEntity
import dev.bittim.valolink.main.data.local.game.relation.agent.AgentWithRoleAndAbilities
import kotlinx.coroutines.flow.Flow

@Dao
interface AgentDao {
    // --------------------------------
    //  Upsert
    // --------------------------------

    @Transaction
    @Upsert
    suspend fun upsertAgent(
        role: RoleEntity,
        agent: AgentEntity,
        abilities: Set<AbilityEntity>,
    )

    @Transaction
    @Upsert
    suspend fun upsertAllAgents(
        roles: Set<RoleEntity>,
        agents: Set<AgentEntity>,
        abilities: Set<AbilityEntity>,
    )

    // --------------------------------
    //  Query
    // --------------------------------

    @Transaction
    @Query("SELECT * FROM Agents WHERE uuid = :uuid LIMIT 1")
    fun getAgent(uuid: String): Flow<AgentWithRoleAndAbilities?>

    @Transaction
    @Query("SELECT * FROM Agents")
    fun getAllAgents(): Flow<List<AgentWithRoleAndAbilities>>

    @Query("SELECT uuid, version FROM Agents WHERE isBaseContent = TRUE")
    fun getAllBaseAgentUuids(): Flow<Map<@MapColumn(columnName = "uuid") String, @MapColumn(columnName = "version") String>>
}