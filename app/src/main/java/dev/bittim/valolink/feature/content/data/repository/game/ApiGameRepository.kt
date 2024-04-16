package dev.bittim.valolink.feature.content.data.repository.game

import androidx.room.withTransaction
import dev.bittim.valolink.feature.content.data.local.game.GameDatabase
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.feature.content.data.remote.game.GameApi
import dev.bittim.valolink.feature.content.data.remote.game.dto.VersionDto
import dev.bittim.valolink.feature.content.domain.model.Event
import dev.bittim.valolink.feature.content.domain.model.Season
import dev.bittim.valolink.feature.content.domain.model.agent.Agent
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import dev.bittim.valolink.feature.content.domain.model.contract.ContractRelation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import javax.inject.Inject

class ApiGameRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi
) : GameRepository {
    override suspend fun getApiVersion(): VersionDto? {
        val versionResponse = try {
            gameApi.getVersion()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return if (versionResponse.isSuccessful &&
            versionResponse.body() != null &&
            versionResponse.body()!!.data != null
        ) {
            versionResponse.body()!!.data!!
        } else {
            null
        }
    }



    override suspend fun getSeason(uuid: String): Flow<Season> {
        val version = getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getSeason(uuid).onEach { entity ->
            if (entity.isEmpty() || entity.firstOrNull()?.version != version) {
                val response = gameApi.getSeason(uuid)
                if (response.isSuccessful) {
                    gameDatabase.dao.upsertSeason(response.body()!!.data!!.toEntity(version))
                }
            }
        }.map { it.first().toType() }.retry(1)
    }

    override suspend fun getAllSeasons(): Flow<List<Season>> {
        val version = getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getAllSeasons().onEach { entities ->
            if (entities.isEmpty() || entities.any { it.version != version }) {
                val response = gameApi.getAllSeasons()
                if (response.isSuccessful) {
                    gameDatabase.withTransaction {
                        gameDatabase.dao.upsertAllSeasons(response.body()!!.data!!.map {
                            it.toEntity(version)
                        })
                    }
                }
            }
        }.map { seasons ->
            seasons.map { it.toType() }
        }
    }



    override suspend fun getEvent(uuid: String): Flow<Event> {
        val version = getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getEvent(uuid).onEach { entity ->
            if (entity.isEmpty() || entity.firstOrNull()?.version != version) {
                val response = gameApi.getEvent(uuid)
                if (response.isSuccessful) {
                    gameDatabase.dao.upsertEvent(response.body()!!.data!!.toEntity(version))
                }
            }
        }.map { it.first().toType() }.retry(1)
    }

    override suspend fun getAllEvents(): Flow<List<Event>> {
        val version = getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getAllEvents().onEach { entities ->
            if (entities.isEmpty() || entities.any { it.version != version }) {
                val response = gameApi.getAllEvents()
                if (response.isSuccessful) {
                    gameDatabase.withTransaction {
                        gameDatabase.dao.upsertAllEvents(response.body()!!.data!!.map {
                            it.toEntity(version)
                        })
                    }
                }
            }
        }.map { events ->
            events.map { it.toType() }
        }
    }
    

    
    override suspend fun getAllContracts(): Flow<List<Contract>> {
        val version = getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getAllContracts().onEach { entities ->
            if (entities.isEmpty() || entities.any { it.contract.version != version }) {
                val response = gameApi.getAllContracts()
                if (response.isSuccessful) {
                    val contractDto = response.body()!!.data!!
                    val contracts = contractDto.map { it.toEntity(version) }

                    val contents = contractDto.zip(contracts) { data, contract ->
                        data.content.toEntity(version, contract.uuid)
                    }

                    val chapterDto = contractDto.map { it.content.chapters }.flatten()
                    val chapters = contractDto.zip(contents) { data, content ->
                        data.content.chapters.map {
                            it.toEntity(version, content.uuid)
                        }
                    }.flatten()

                    val levelDto = chapterDto.map { it.levels }.flatten()
                    val levels = chapterDto.zip(chapters) { data, chapter ->
                        data.levels.map {
                            it.toEntity(version, chapter.uuid)
                        }
                    }.flatten()

                    val rewards = levelDto.zip(levels) { data, level ->
                        data.reward.toEntity(version, levelUuid = level.uuid)
                    }.plus(
                        chapterDto.zip(chapters) { data, chapter ->
                            data.freeRewards?.map {
                                it.toEntity(version, chapterUuid = chapter.uuid)
                            }.orEmpty()
                        }.flatten()
                    )

                    gameDatabase.dao.upsertAllContracts(
                        contracts.distinct().toSet(),
                        contents.distinct().toSet(),
                        chapters.distinct().toSet(),
                        levels.distinct().toSet(),
                        rewards.distinct().toSet()
                    )
                }
            }
        }.map { contracts ->
            contracts.map { it.toType(findRelation(it.content.content).firstOrNull()) }
        }
    }



    override suspend fun getAgent(uuid: String): Flow<Agent> {
        val version = getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getAgent(uuid).onEach { entity ->
            if (entity.isEmpty() || entity.firstOrNull()?.agent?.version != version) {
                val response = gameApi.getAgent(uuid)
                if (response.isSuccessful) {
                    val agentDto = response.body()!!.data!!
                    val agent = agentDto.toEntity(version)
                    val recruitment = agentDto.recruitmentData?.toEntity(version, agent.uuid)
                    val role = agentDto.role.toEntity(version)
                    val abilities = agentDto.abilities.map {
                        it.toEntity(version, agent.uuid)
                    }

                    gameDatabase.withTransaction {
                        gameDatabase.dao.upsertAgent(
                            role, agent, abilities.distinct().toSet()
                        )

                        if (recruitment != null) {
                            gameDatabase.dao.upsertAgentRecruitment(
                                recruitment
                            )
                        }
                    }
                }
            }
        }.map {
            it.first().toType()
        }.retry(1)
    }

    override suspend fun getAllAgents(): Flow<List<Agent>> {
        val version = getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getAllAgents().onEach { entities ->
            if (entities.isEmpty() || entities.any { it.agent.version != version }) {
                val response = gameApi.getAllAgents()
                if (response.isSuccessful) {
                    val agentDto = response.body()!!.data!!
                    val agents = agentDto.map { it.toEntity(version) }

                    val recruitments = agentDto.zip(agents) { data, agent ->
                        data.recruitmentData?.toEntity(version, agent.uuid)
                    }.filterNotNull()

                    val roles = agentDto.map {
                        it.role.toEntity(version)
                    }

                    val abilities = agentDto.zip(agents) { data, agent ->
                        data.abilities.map {
                            it.toEntity(version, agent.uuid)
                        }
                    }.flatten()

                    gameDatabase.dao.upsertAllAgents(
                        roles.distinct().toSet(),
                        agents.distinct().toSet(),
                        recruitments.distinct().toSet(),
                        abilities.distinct().toSet()
                    )
                }
            }
        }.map { agents ->
            agents.map { it.toType() }
        }
    }



    private suspend fun findRelation(content: ContentEntity): Flow<ContractRelation?> {
        if (content.relationType.isNullOrEmpty() || content.relationUuid.isNullOrEmpty()) {
            return flow { emit(null) }
        }

        return when (content.relationType) {
            "Agent"  -> {
                getAgent(content.relationUuid)
            }

            "Event"  -> {
                getEvent(content.relationUuid)
            }

            "Season" -> {
                getSeason(content.relationUuid)
            }

            else     -> flow { emit(null) }
        }
    }
}