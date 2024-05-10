package dev.bittim.valolink.feature.content.data.local.game.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.bittim.valolink.feature.content.data.local.game.entity.agent.RecruitmentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.LevelEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.RewardEntity
import dev.bittim.valolink.feature.content.data.local.game.relation.agent.RecruitmentWithAgentWithRoleAndAbilities
import dev.bittim.valolink.feature.content.data.local.game.relation.contract.ContractWithContentWithChaptersWithLevelsAndRewards
import kotlinx.coroutines.flow.Flow

@Dao
interface ContractsDao {
    // --------------------------------
    //  Upserts
    // --------------------------------

    @Transaction
    @Upsert
    suspend fun upsertContract(
        contract: ContractEntity,
        content: ContentEntity,
        chapters: Set<ChapterEntity>,
        levels: Set<LevelEntity>,
        rewards: Set<RewardEntity>
    )



    @Upsert
    suspend fun upsertAgentRecruitment(
        recruitment: RecruitmentEntity
    )



    @Transaction
    @Upsert
    suspend fun upsertMultipleContracts(
        contracts: Set<ContractEntity>,
        contents: Set<ContentEntity>,
        chapters: Set<ChapterEntity>,
        levels: Set<LevelEntity>,
        rewards: Set<RewardEntity>
    )



    @Transaction
    @Upsert
    suspend fun upsertMultipleAgentRecruitments(
        recruitments: Set<RecruitmentEntity>
    )

    // --------------------------------
    //  Queries
    // --------------------------------

    // -------- [ Single Object ] --------

    @Query("SELECT * FROM Contracts WHERE uuid = :uuid LIMIT 1")
    fun getContract(uuid: String): Flow<ContractWithContentWithChaptersWithLevelsAndRewards?>



    @Query("SELECT * FROM AgentRecruitments WHERE uuid = :uuid LIMIT 1")
    fun getRecruitment(uuid: String): Flow<RecruitmentWithAgentWithRoleAndAbilities?>

    // -------- [ Active ] --------

    @Query(
        """
        SELECT Contracts.* FROM Contracts
        INNER JOIN ContractContents ON Contracts.uuid = ContractContents.contractUuid
        INNER JOIN (
            SELECT uuid, startTime, endTime FROM Seasons
            UNION ALL
            SELECT uuid, startTime, endTime FROM Events
        ) AS Relation ON ContractContents.relationUuid = Relation.uuid
            AND datetime(Relation.startTime) < datetime(:currentIsoTime)
            AND datetime(Relation.endTime) > datetime(:currentIsoTime)
        ORDER BY endTime ASC
    """
    )
    fun getActiveContracts(currentIsoTime: String): Flow<List<ContractWithContentWithChaptersWithLevelsAndRewards>>



    @Query(
        """
        SELECT * FROM AgentRecruitments
        WHERE datetime(startDate) < datetime(:currentIsoTime)
            AND datetime(endDate) > datetime(:currentIsoTime)
        ORDER BY endDate ASC
    """
    )
    fun getActiveRecruitments(currentIsoTime: String): Flow<List<RecruitmentWithAgentWithRoleAndAbilities>>

    // -------- [ Inactive ] --------

    @Query(
        """
        SELECT Contracts.* FROM Contracts
        INNER JOIN ContractContents ON Contracts.uuid = ContractContents.contractUuid
        INNER JOIN Seasons ON ContractContents.relationType = "Season"
            AND ContractContents.relationUuid = Seasons.uuid
            AND datetime(Seasons.startTime) < datetime(:currentIsoTime)
            AND datetime(Seasons.endTime) < datetime(:currentIsoTime)
        ORDER BY endTime DESC
    """
    )
    fun getInactiveSeasonContracts(currentIsoTime: String): Flow<List<ContractWithContentWithChaptersWithLevelsAndRewards>>



    @Query(
        """
        SELECT Contracts.* FROM Contracts
        INNER JOIN ContractContents ON Contracts.uuid = ContractContents.contractUuid
        INNER JOIN Events ON ContractContents.relationType = "Event"
            AND ContractContents.relationUuid = Events.uuid
            AND datetime(Events.startTime) < datetime(:currentIsoTime)
            AND datetime(Events.endTime) < datetime(:currentIsoTime)
        ORDER BY endTime DESC
    """
    )
    fun getInactiveEventContracts(currentIsoTime: String): Flow<List<ContractWithContentWithChaptersWithLevelsAndRewards>>



    @Query(
        """
        SELECT * FROM AgentRecruitments
        WHERE datetime(startDate) < datetime(:currentIsoTime)
            AND datetime(endDate) < datetime(:currentIsoTime)
        ORDER BY endDate DESC
    """
    )
    fun getInactiveRecruitments(currentIsoTime: String): Flow<List<RecruitmentWithAgentWithRoleAndAbilities>>

    // -------- [ Agent Gears ] --------

    @Query(
        """
        SELECT Contracts.* FROM Contracts
        INNER JOIN ContractContents ON Contracts.uuid = ContractContents.contractUuid
        INNER JOIN Agents ON ContractContents.relationType = "Agent"
            AND ContractContents.relationUuid == Agents.uuid
        ORDER BY displayName ASC
    """
    )
    fun getAgentGears(): Flow<List<ContractWithContentWithChaptersWithLevelsAndRewards>>
}