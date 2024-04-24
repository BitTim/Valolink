package dev.bittim.valolink.feature.content.data.repository.game

import androidx.room.withTransaction
import dev.bittim.valolink.feature.content.data.local.game.GameDatabase
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.feature.content.data.remote.game.GameApi
import dev.bittim.valolink.feature.content.data.remote.game.dto.VersionDto
import dev.bittim.valolink.feature.content.domain.model.Event
import dev.bittim.valolink.feature.content.domain.model.Season
import dev.bittim.valolink.feature.content.domain.model.agent.Agent
import dev.bittim.valolink.feature.content.domain.model.contract.ContentRelation
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.zip
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



    override suspend fun getSeason(uuid: String, providedVersion: String?): Flow<Season> {
        val version = providedVersion ?: getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getSeason(uuid).distinctUntilChanged().transform { entity ->
                if (entity == null || entity.version != version) {
                val response = gameApi.getSeason(uuid)
                if (response.isSuccessful) {
                    gameDatabase.dao.upsertSeason(response.body()!!.data!!.toEntity(version))
                }
                } else {
                    emit(entity)
            }
            }.map { it.toType() }
    }

    override suspend fun getAllSeasons(providedVersion: String?): Flow<List<Season>> {
        val version = providedVersion ?: getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getAllSeasons().distinctUntilChanged().transform { entities ->
            if (entities.isEmpty() || entities.any { it.version != version }) {
                val response = gameApi.getAllSeasons()
                if (response.isSuccessful) {
                    gameDatabase.withTransaction {
                        gameDatabase.dao.upsertAllSeasons(response.body()!!.data!!.map {
                            it.toEntity(version)
                        })
                    }
                }
            } else {
                emit(entities)
            }
        }.map { seasons ->
            seasons.map { it.toType() }
        }
    }



    override suspend fun getEvent(uuid: String, providedVersion: String?): Flow<Event> {
        val version = providedVersion ?: getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getEvent(uuid).distinctUntilChanged().transform { entity ->
                if (entity == null || entity.version != version) {
                val response = gameApi.getEvent(uuid)
                if (response.isSuccessful) {
                    gameDatabase.dao.upsertEvent(response.body()!!.data!!.toEntity(version))
                }
                } else {
                    emit(entity)
            }
            }.map { it.toType() }
    }

    override suspend fun getAllEvents(providedVersion: String?): Flow<List<Event>> {
        val version = providedVersion ?: getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getAllEvents().distinctUntilChanged().transform { entities ->
            if (entities.isEmpty() || entities.any { it.version != version }) {
                val response = gameApi.getAllEvents()
                if (response.isSuccessful) {
                    gameDatabase.withTransaction {
                        gameDatabase.dao.upsertAllEvents(response.body()!!.data!!.map {
                            it.toEntity(version)
                        })
                    }
                }
            } else {
                emit(entities)
            }
        }.map { events ->
            events.map { it.toType() }
        }
    }



    override suspend fun getAgent(uuid: String, providedVersion: String?): Flow<Agent> {
        val version = providedVersion ?: getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getAgent(uuid).distinctUntilChanged().transform { entity ->
            if (entity == null || entity.agent.version != version) {
                val response = gameApi.getAgent(uuid)
                if (response.isSuccessful) {
                    val agentDto = response.body()!!.data!!
                    val agent = agentDto.toEntity(version)
                    val recruitment = agentDto.recruitmentData?.toEntity(version)
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
            } else {
                emit(entity)
            }
        }.map { it.toType() }
    }

    override suspend fun getAllAgents(providedVersion: String?): Flow<List<Agent>> {
        val version = providedVersion ?: getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getAllAgents().distinctUntilChanged().transform { entities ->
            if (entities.isEmpty() || entities.any { it.agent.version != version }) {
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
                            it.toEntity(version, agent.uuid)
                        }
                    }.flatten()

                    gameDatabase.dao.upsertAllAgents(
                        roles.distinct().toSet(),
                        recruitments.distinct().toSet(),
                        agents.distinct().toSet(),
                        abilities.distinct().toSet()
                    )
                }
            } else {
                emit(entities)
            }
        }.map { agents ->
            agents.map { it.toType() }
        }
    }


    override suspend fun getContract(uuid: String, providedVersion: String?): Flow<Contract> {
        val version = providedVersion ?: getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getContract(uuid).distinctUntilChanged().transform { entity ->
            if (entity.contract.version != version) {
                val response = gameApi.getContract(uuid)
                if (response.isSuccessful) {
                    val contractDto = response.body()!!.data!!

                    val contract = contractDto.toEntity(version)
                    val content = contractDto.content.toEntity(version, contract.uuid)

                    val chapterDto = contractDto.content.chapters
                    val chapters = contractDto.content.chapters.map {
                        it.toEntity(version, content.uuid)
                    }

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

                    gameDatabase.dao.upsertContract(
                        contract,
                        content,
                        chapters.distinct().toSet(),
                        levels.distinct().toSet(),
                        rewards.distinct().toSet()
                    )
                }
            } else {
                emit(entity)
            }
        }.map {
            val relation = findRelation(version, it.content.content).firstOrNull()
            it.toType(relation, relation?.startTime, relation?.endTime)
        }
    }

    override suspend fun getAllContracts(providedVersion: String?): Flow<List<Contract>> {
        val version = providedVersion ?: getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getAllContracts().distinctUntilChanged().transform { entities ->
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
            } else {
                emit(entities)
            }
        }.map { contracts ->
            contracts.map {
                val relation = findRelation(version, it.content.content).firstOrNull()
                it.toType(relation, relation?.startTime, relation?.endTime)
            }
        }
    }

    override suspend fun getAllRecruitmentsAsContracts(providedVersion: String?): Flow<List<Contract>> {
        val version = providedVersion ?: getApiVersion()?.version
        if (version.isNullOrEmpty()) {
            return flow { }
        }

        return gameDatabase.dao.getAllRecruitments().distinctUntilChanged().transform { entities ->
            if (entities.isEmpty() || entities.any { it.recruitment.version != version }) {
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
                            it.toEntity(version, agent.uuid)
                        }
                    }.flatten()

                    gameDatabase.dao.upsertAllAgents(
                        roles.distinct().toSet(), recruitments.distinct().toSet(),
                        agents.distinct().toSet(),
                        abilities.distinct().toSet()
                    )
                }
            } else {
                emit(entities)
            }
        }.map { recruitments ->
            recruitments.map { it.toContract() }
        }
    }

    override suspend fun getAllContractsAndRecruitments(providedVersion: String?): Flow<List<Contract>> {
        return getAllRecruitmentsAsContracts().zip(getAllContracts()) { recruitments, contracts ->
            recruitments.plus(contracts)
        }
    }



    override suspend fun getAllActiveContracts(providedVersion: String?): Flow<List<Contract>> {
        return getAllContractsAndRecruitments().map { contracts -> contracts.filter { it.isActive() }.sortedBy { it.endTime } }
    }

    override suspend fun getAllAgentGears(providedVersion: String?): Flow<List<Contract>> {
        return getAllContractsAndRecruitments().map { contracts -> contracts.filter { it.isAgentGear() } }
    }

    override suspend fun getAllInactiveContracts(providedVersion: String?): Flow<List<Contract>> {
        return getAllContractsAndRecruitments().map { contracts -> contracts.filter { it.isInactive() }.sortedByDescending { it.startTime } }
    }



    private suspend fun findRelation(
        version: String,
        content: ContentEntity
    ): Flow<ContentRelation?> {
        if (content.relationType.isNullOrEmpty() || content.relationUuid.isNullOrEmpty()) {
            return flow { emit(null) }
        }

        return when (content.relationType) {
            "Agent"  -> {
                getAgent(content.relationUuid, version)
            }

            "Event"  -> {
                getEvent(content.relationUuid, version)
            }

            "Season" -> {
                getSeason(content.relationUuid, version)
            }

            else     -> flow { emit(null) }
        }
    }
}