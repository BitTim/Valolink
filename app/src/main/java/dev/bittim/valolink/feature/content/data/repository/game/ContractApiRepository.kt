package dev.bittim.valolink.feature.content.data.repository.game

import dev.bittim.valolink.feature.content.data.local.game.GameDatabase
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.feature.content.data.remote.game.GameApi
import dev.bittim.valolink.feature.content.domain.model.contract.ContentRelation
import dev.bittim.valolink.feature.content.domain.model.contract.Contract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import java.time.Instant
import javax.inject.Inject

class ContractApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
    private val seasonRepository: SeasonRepository,
    private val eventRepository: EventRepository,
    private val agentRepository: AgentRepository
) : ContractRepository {
    override suspend fun getContract(uuid: String, providedVersion: String?): Flow<Contract?> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.contractsDao.getContract(uuid).distinctUntilChanged()
            .transform { contract ->
                if (contract == null || contract.contract.version != version) {
                    fetchContract(uuid, version)
                } else {
                    emit(contract)
                }
            }.map {
                it.toType(getRelation(version, it.content.content).firstOrNull())
            }
    }

    override suspend fun getRecruitmentAsContract(
        uuid: String, providedVersion: String?
    ): Flow<Contract?> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.contractsDao.getRecruitment(uuid).distinctUntilChanged()
            .transform { recruitment ->
                if (recruitment == null || recruitment.recruitment.version != version) {
                    agentRepository.fetchAgents(version)
                } else {
                    emit(recruitment)
                }
            }.map {
                it.toContract()
            }
    }



    override suspend fun getActiveContracts(providedVersion: String?): Flow<List<Contract>> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""
        val currentIsoTime = Instant.now().toString()

        return gameDatabase.contractsDao.getActiveContracts(currentIsoTime).distinctUntilChanged()
            .transform { contracts ->
                if (contracts.isEmpty() || contracts.any { it.contract.version != version }) {
                    fetchContracts(version)
                } else {
                    emit(contracts)
                }
            }.map { contracts ->
                contracts.map {
                    it.toType(getRelation(version, it.content.content).firstOrNull())
                }
            }.combine(gameDatabase.contractsDao.getActiveRecruitments(currentIsoTime)
                .distinctUntilChanged().map { recruitments ->
                    recruitments.map { it.toContract() }
                }) { contracts, recruitments ->
                (contracts + recruitments).sortedBy { it.content.relation?.endTime }
            }
    }

    override suspend fun getAgentGears(providedVersion: String?): Flow<List<Contract>> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""

        return gameDatabase.contractsDao.getAgentGears().distinctUntilChanged().transform { gears ->
            if (gears.isEmpty() || gears.any { it.contract.version != version }) {
                agentRepository.fetchAgents(version)
            } else {
                emit(gears)
            }
        }.map { gears ->
            gears.map {
                it.toType(getRelation(version, it.content.content).firstOrNull())
            }
        }
    }

    override suspend fun getInactiveContracts(
        contentType: String, providedVersion: String?
    ): Flow<List<Contract>> {
        val version = providedVersion ?: versionRepository.getApiVersion()?.version ?: ""
        val currentIsoTime = Instant.now().toString()

        return when (contentType) {
            "Season"      -> {
                gameDatabase.contractsDao.getInactiveSeasonContracts(currentIsoTime)
                    .distinctUntilChanged().transform { seasons ->
                        if (seasons.isEmpty() || seasons.any { it.contract.version != version }) {
                            seasonRepository.fetchSeasons(version)
                            fetchContracts(version)
                        } else {
                            emit(seasons)
                        }
                    }.map { entities ->
                        entities.map {
                            it.toType(getRelation(version, it.content.content).firstOrNull())
                        }
                    }
            }

            "Event"       -> {
                gameDatabase.contractsDao.getInactiveEventContracts(currentIsoTime)
                    .distinctUntilChanged().transform { events ->
                        if (events.isEmpty() || events.any { it.contract.version != version }) {
                            eventRepository.fetchEvents(version)
                            fetchContracts(version)
                        } else {
                            emit(events)
                        }
                    }.map { entities ->
                        entities.map {
                            it.toType(getRelation(version, it.content.content).firstOrNull())
                        }
                    }
            }

            "Recruitment" -> {
                gameDatabase.contractsDao.getInactiveRecruitments(currentIsoTime)
                    .distinctUntilChanged().transform { recruitments ->
                        if (recruitments.isEmpty() || recruitments.any { it.recruitment.version != version }) {
                            fetchContracts(version)
                        } else {
                            emit(recruitments)
                        }
                    }.map { entities ->
                        entities.map { it.toContract() }
                    }
            }

            else          -> flow { }
        }
    }

    // --------------------------------
    //  Fetching from API
    // --------------------------------

    // -------- [ Single fetching ] --------

    override suspend fun fetchContract(uuid: String, version: String) {
        val response = gameApi.getContract(uuid)
        if (response.isSuccessful) {
            val contractDto = response.body()!!.data!!

            val contract = contractDto.toEntity(version)
            val content = contractDto.content.toEntity(
                contract.freeRewardScheduleUuid, version, contract.uuid
            )

            if (content.relationUuid != null) {
                when (content.relationType) {
                    "Season" -> seasonRepository.fetchSeason(content.relationUuid, version)
                    "Event"  -> eventRepository.fetchEvent(content.relationUuid, version)
                }
            }

            val chapterDto = contractDto.content.chapters
            val chapters = contractDto.content.chapters.mapIndexed { index, chapter ->
                chapter.toEntity(index, version, content.uuid)
            }

            val levelDto = chapterDto.map { it.levels }.flatten()
            val levels = chapterDto.zip(chapters) { data, chapter ->
                data.levels.mapIndexed { index, level ->
                    level.toEntity(index, version, chapter.uuid)
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

            gameDatabase.contractsDao.upsertContract(
                contract,
                content,
                chapters.distinct().toSet(),
                levels.distinct().toSet(),
                rewards.distinct().toSet()
            )
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchContracts(version: String) {
        val response = gameApi.getAllContracts()
        if (response.isSuccessful) {
            val contractDto = response.body()!!.data!!
            val contracts = contractDto.map { it.toEntity(version) }

            val contents = contractDto.zip(contracts) { data, contract ->
                data.content.toEntity(
                    contract.freeRewardScheduleUuid, version, contract.uuid
                )
            }

            val chapterDto = contractDto.map { it.content.chapters }.flatten()
            val chapters = contractDto.zip(contents) { data, content ->
                data.content.chapters.mapIndexed { index, chapter ->
                    chapter.toEntity(index, version, content.uuid)
                }
            }.flatten()

            val levelDto = chapterDto.map { it.levels }.flatten()
            val levels = chapterDto.zip(chapters) { data, chapter ->
                data.levels.mapIndexed { index, level ->
                    level.toEntity(index, version, chapter.uuid)
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

            gameDatabase.contractsDao.upsertMultipleContracts(
                contracts.distinct().toSet(),
                contents.distinct().toSet(),
                chapters.distinct().toSet(),
                levels.distinct().toSet(),
                rewards.distinct().toSet()
            )
        }
    }

    // --------------------------------
    //  Query relation
    // --------------------------------

    private suspend fun getRelation(
        version: String, content: ContentEntity
    ): Flow<ContentRelation?> {
        if (content.relationType.isNullOrEmpty() || content.relationUuid.isNullOrEmpty()) {
            return flow { emit(null) }
        }

        return when (content.relationType) {
            "Agent"  -> {
                agentRepository.getAgent(content.relationUuid, version)
            }

            "Event"  -> {
                eventRepository.getEvent(content.relationUuid, version)
            }

            "Season" -> {
                seasonRepository.getSeason(content.relationUuid, version)
            }

            else     -> flow { emit(null) }
        }
    }
}