package dev.bittim.valolink.main.data.repository.game

import androidx.room.withTransaction
import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.domain.model.game.agent.Agent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class AgentApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
) : AgentRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getAgent(
        uuid: String,
        providedVersion: String?,
    ): Flow<Agent> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.agentDao.getByUuid(uuid).distinctUntilChanged().transform { agent ->
            if (agent == null || agent.agent.version != version) {
                fetchAgent(
                    uuid,
                    version
                )
            } else {
                emit(agent)
            }
        }.map {
            it.toType()
        }
    }

    // -------- [ Bulk queries ] --------

    override suspend fun getAllAgents(providedVersion: String?): Flow<List<Agent>> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.agentDao.getAll().distinctUntilChanged().transform { agents ->
            if (agents.isEmpty() || agents.any { it.agent.version != version }) {
                fetchAgents(version)
            } else {
                emit(agents)
            }
        }.map { agents ->
            agents.map { it.toType() }
        }
    }

    override suspend fun getAllBaseAgentUuids(providedVersion: String?): Flow<List<String>> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.agentDao.getBase().distinctUntilChanged().transform { agentMaps ->
                if (agentMaps.isEmpty() || agentMaps.any { it.value != version }) {
                    fetchAgents(version)
                } else {
                    emit(agentMaps.keys.toList())
                }
            }
    }

    // --------------------------------
    //  Fetching from API
    // --------------------------------

    // -------- [ Single fetching ] --------

    override suspend fun fetchAgent(
        uuid: String,
        version: String,
    ) {
        val response = gameApi.getAgent(uuid)
        if (response.isSuccessful) {
            val agentDto = response.body()!!.data!!
            val agent = agentDto.toEntity(version)
            val recruitment = agentDto.recruitmentData?.toEntity(version)
            val role = agentDto.role.toEntity(version)
            val abilities = agentDto.abilities.map {
                it.toEntity(
                    version,
                    agent.uuid
                )
            }

            gameDatabase.withTransaction {
                if (recruitment != null) {
                    gameDatabase.contractsDao.upsertRecruitment(
                        recruitment
                    )
                }

                gameDatabase.agentDao.upsert(
                    role,
                    agent,
                    abilities.distinct().toSet()
                )
            }
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchAgents(version: String) {
        val response = gameApi.getAllAgents()
        if (response.isSuccessful) {
            val agentDto = response.body()!!.data!!
            val agents = agentDto.map { it.toEntity(version) }

            val recruitments = agentDto.mapNotNull { data ->
                data.recruitmentData?.toEntity(version)
            }

            val roles = agentDto.map {
                it.role.toEntity(version)
            }

            val abilities = agentDto.zip(agents) { data, agent ->
                data.abilities.map {
                    it.toEntity(
                        version,
                        agent.uuid
                    )
                }
            }.flatten()

            gameDatabase.withTransaction {
                gameDatabase.contractsDao.upsertRecruitment(
                    recruitments.distinct().toSet()
                )

                gameDatabase.agentDao.upsert(
                    roles.distinct().toSet(),
                    agents.distinct().toSet(),
                    abilities.distinct().toSet()
                )
            }
        }
    }
}