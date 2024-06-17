package dev.bittim.valolink.main.data.repository.game

import androidx.room.withTransaction
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.data.worker.game.AgentSyncWorker
import dev.bittim.valolink.main.data.worker.game.BuddySyncWorker
import dev.bittim.valolink.main.domain.model.game.agent.Agent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AgentApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
    private val workManager: WorkManager,
) : AgentRepository {
    // --------------------------------
    //  Query from Database
    // --------------------------------

    // -------- [ Single queries ] --------

    override suspend fun getByUuid(
        uuid: String,
        providedVersion: String?,
    ): Flow<Agent> {
        return gameDatabase.agentDao.getByUuid(uuid).distinctUntilChanged().combineTransform(
            versionRepository.get()
        ) { agent, apiVersion ->
            val version = providedVersion ?: apiVersion.version

            if (agent == null || agent.agent.version != version) {
                queueWorker(version, uuid)
            } else {
                emit(agent)
            }
        }.map {
            it.toType()
        }
    }

    // -------- [ Bulk queries ] --------

    override suspend fun getAll(providedVersion: String?): Flow<List<Agent>> {
        return gameDatabase.agentDao.getAll().distinctUntilChanged().combineTransform(
            versionRepository.get()
        ) { agents, apiVersion ->
            val version = providedVersion ?: apiVersion.version

            if (agents.isEmpty() || agents.any { it.agent.version != version }) {
                queueWorker(version)
            } else {
                emit(agents)
            }
        }.map { agents ->
            agents.map { it.toType() }
        }
    }

    override suspend fun getAllBaseAgentUuids(providedVersion: String?): Flow<List<String>> {
        return gameDatabase.agentDao.getBase().distinctUntilChanged().combineTransform(
            versionRepository.get()
        ) { agentMaps, apiVersion ->
            val version = providedVersion ?: apiVersion.version

            if (agentMaps.isEmpty() || agentMaps.any { it.value != version }) {
                queueWorker(version)
            } else {
                emit(agentMaps.keys.toList())
            }
        }
    }

    // --------------------------------
    //  Fetching from API
    // --------------------------------

    // -------- [ Single fetching ] --------

    override suspend fun fetch(
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

    override suspend fun fetchAll(version: String) {
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

    // ================================
    //  Queue Worker
    // ================================

    override fun queueWorker(version: String, uuid: String?) {
        val workRequest = OneTimeWorkRequestBuilder<AgentSyncWorker>()
            .setInputData(
                workDataOf(
                    AgentSyncWorker.KEY_AGENT_UUID to uuid,
                    AgentSyncWorker.KEY_VERSION to version
                )
            )
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()
        workManager.enqueueUniqueWork(
            AgentSyncWorker.WORK_NAME,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            workRequest
        )
    }
}