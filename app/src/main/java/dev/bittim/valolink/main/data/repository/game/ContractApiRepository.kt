package dev.bittim.valolink.main.data.repository.game

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.main.data.local.game.GameDatabase
import dev.bittim.valolink.main.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.main.data.remote.game.GameApi
import dev.bittim.valolink.main.data.worker.game.BuddySyncWorker
import dev.bittim.valolink.main.data.worker.game.ContractSyncWorker
import dev.bittim.valolink.main.domain.model.game.contract.content.ContentRelation
import dev.bittim.valolink.main.domain.model.game.contract.content.ContentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class ContractApiRepository @Inject constructor(
    private val gameDatabase: GameDatabase,
    private val gameApi: GameApi,
    private val versionRepository: VersionRepository,
    private val seasonRepository: SeasonRepository,
    private val eventRepository: EventRepository,
    private val agentRepository: AgentRepository,
    private val workManager: WorkManager
) : ContractRepository {
    override suspend fun getContract(
        uuid: String,
        providedVersion: String?,
    ): Flow<dev.bittim.valolink.main.domain.model.game.contract.Contract?> {

        return gameDatabase.contractsDao
            .getByUuid(uuid)
            .distinctUntilChanged()
            .combineTransform(
                versionRepository.get()
            ) { contract, apiVersion ->
                val version = providedVersion ?: apiVersion.version

                if (contract == null || contract.contract.version != version) {
                    queueWorker(
                        version,
                        uuid
                    )
                } else {
                    emit(
                        Pair(
                            contract,
                            version
                        )
                    )
                }
            }
            .map {
                it.first.toType(
                    getRelation(
                        it.second, // Version
                        it.first.content.content
                    ).firstOrNull()
                )
            }
    }

    override suspend fun getRecruitmentAsContract(
        uuid: String,
        providedVersion: String?,
    ): Flow<dev.bittim.valolink.main.domain.model.game.contract.Contract?> {

        return gameDatabase.contractsDao
            .getRecruitmentByUuid(uuid)
            .distinctUntilChanged()
            .combineTransform(
                versionRepository.get()
            ) { recruitment, apiVersion ->
                val version = providedVersion ?: apiVersion.version

                if (recruitment == null || recruitment.recruitment.version != version) {
                    agentRepository.queueWorker(version)
                } else {
                    emit(recruitment)
                }
            }
            .map {
                it.toContract()
            }
    }



    override suspend fun getActiveContracts(providedVersion: String?): Flow<List<dev.bittim.valolink.main.domain.model.game.contract.Contract>> {

        val currentIsoTime = Instant.now().toString()

        return gameDatabase.contractsDao
            .getActiveByTime(currentIsoTime)
            .distinctUntilChanged()
            .combineTransform(
                versionRepository.get()
            ) { contracts, apiVersion ->
                val version = providedVersion ?: apiVersion.version

                if (contracts.isEmpty() || contracts.any { it.contract.version != version }) {
                    queueWorker(version)
                } else {
                    emit(
                        Pair(
                            contracts,
                            version
                        )
                    )
                }
            }
            .map { contractsVersionPair ->
                contractsVersionPair.first.map {
                    it.toType(
                        getRelation(
                            contractsVersionPair.second,
                            it.content.content
                        ).firstOrNull()
                    )
                }
            }
            .combine(gameDatabase.contractsDao
                         .getActiveRecruitmentsByTime(currentIsoTime)
                         .distinctUntilChanged()
                         .map { recruitments ->
                             recruitments.map { it.toContract() }
                         }) { contracts, recruitments ->
                (contracts + recruitments).sortedBy { it.content.relation?.endTime }
            }
    }

    override suspend fun getAgentGears(providedVersion: String?): Flow<List<dev.bittim.valolink.main.domain.model.game.contract.Contract>> {
        return gameDatabase.contractsDao.getAllGears().distinctUntilChanged().combineTransform(
            versionRepository.get()
        ) { gears, apiVersion ->
            val version = providedVersion ?: apiVersion.version

            if (gears.isEmpty() || gears.any { it.contract.version != version }) {
                agentRepository.queueWorker(version)
            } else {
                emit(
                    Pair(
                        gears,
                        version
                    )
                )
            }
        }.map { gearsVersionPair ->
            gearsVersionPair.first.map {
                it.toType(
                    getRelation(
                        gearsVersionPair.second,
                        it.content.content
                    ).firstOrNull()
                )
            }
        }
    }

    override suspend fun getInactiveContracts(
        contentType: ContentType,
        providedVersion: String?,
    ): Flow<List<dev.bittim.valolink.main.domain.model.game.contract.Contract>> {
        val currentIsoTime = Instant.now().toString()

        return when (contentType) {
            ContentType.SEASON  -> {
                gameDatabase.contractsDao
                    .getInactiveSeasonsByTime(currentIsoTime)
                    .distinctUntilChanged()
                    .combineTransform(
                        versionRepository.get()
                    ) { seasons, apiVersion ->
                        val version = providedVersion ?: apiVersion.version

                        if (seasons.isEmpty() || seasons.any { it.contract.version != version }) {
                            seasonRepository.queueWorker(version)
                            queueWorker(version)
                        } else {
                            emit(
                                Pair(
                                    seasons,
                                    version
                                )
                            )
                        }
                    }
                    .map { entitiesVersionPair ->
                        entitiesVersionPair.first.map {
                            it.toType(
                                getRelation(
                                    entitiesVersionPair.second,
                                    it.content.content
                                ).firstOrNull()
                            )
                        }
                    }
            }

            ContentType.EVENT   -> {
                gameDatabase.contractsDao
                    .getInactiveEventsByTime(currentIsoTime)
                    .distinctUntilChanged()
                    .combineTransform(
                        versionRepository.get()
                    ) { events, apiVersion ->
                        val version = providedVersion ?: apiVersion.version

                        if (events.isEmpty() || events.any { it.contract.version != version }) {
                            eventRepository.queueWorker(version)
                            queueWorker(version)
                        } else {
                            emit(
                                Pair(
                                    events,
                                    version
                                )
                            )
                        }
                    }
                    .map { entitiesVersionMap ->
                        entitiesVersionMap.first.map {
                            it.toType(
                                getRelation(
                                    entitiesVersionMap.second,
                                    it.content.content
                                ).firstOrNull()
                            )
                        }
                    }
            }

            ContentType.RECRUIT -> {
                gameDatabase.contractsDao
                    .getInactiveRecruitmentsByTime(currentIsoTime)
                    .distinctUntilChanged()
                    .combineTransform(
                        versionRepository.get()
                    ) { recruitments, apiVersion ->
                        val version = providedVersion ?: apiVersion.version

                        if (recruitments.isEmpty() || recruitments.any { it.recruitment.version != version }) {
                            queueWorker(version)
                        } else {
                            emit(recruitments)
                        }
                    }
                    .map { entities ->
                        entities.map { it.toContract() }
                    }
            }
        }
    }

    // --------------------------------
    //  Fetching from API
    // --------------------------------

    // -------- [ Single fetching ] --------

    override suspend fun fetchContract(
        uuid: String,
        version: String,
    ) {
        val response = gameApi.getContract(uuid)
        if (response.isSuccessful) {
            val contractDto = response.body()!!.data!!

            val contract = contractDto.toEntity(version)
            val content = contractDto.content.toEntity(
                contract.freeRewardScheduleUuid,
                version,
                contract.uuid
            )

            if (content.relationUuid != null) {
                when (content.relationType) {
                    "Season" -> seasonRepository.fetch(
                        content.relationUuid,
                        version
                    )

                    "Event" -> eventRepository.fetch(
                        content.relationUuid,
                        version
                    )
                }
            }

            val chapterDto = contractDto.content.chapters
            val chapters = contractDto.content.chapters.mapIndexed { index, chapter ->
                chapter.toEntity(
                    index,
                    version,
                    content.uuid
                )
            }

            val levelDto = chapterDto.map { it.levels }.flatten()
            val levels = chapterDto.zip(chapters) { data, chapter ->
                data.levels.mapIndexed { index, level ->
                    level.toEntity(
                        index,
                        version,
                        chapter.uuid
                    )
                }
            }.flatten()

            val rewards = levelDto.zip(levels) { data, level ->
                data.reward.toEntity(
                    version,
                    levelUuid = level.uuid
                )
            }.plus(chapterDto.zip(chapters) { data, chapter ->
                data.freeRewards?.map {
                    it.toEntity(
                        version,
                        chapterUuid = chapter.uuid
                    )
                }.orEmpty()
            }.flatten())

            gameDatabase.contractsDao.upsert(
                contract,
                content,
                chapters.distinct().toSet(),
                levels.distinct().toSet(),
                rewards.distinct().toSet()
            )
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchAllContracts(version: String) {
        val response = gameApi.getAllContracts()
        if (response.isSuccessful) {
            val contractDto = response.body()!!.data!!
            val contracts = contractDto.map { it.toEntity(version) }

            val contents = contractDto.zip(contracts) { data, contract ->
                data.content.toEntity(
                    contract.freeRewardScheduleUuid,
                    version,
                    contract.uuid
                )
            }

            val chapterDto = contractDto.map { it.content.chapters }.flatten()
            val chapters = contractDto.zip(contents) { data, content ->
                data.content.chapters.mapIndexed { index, chapter ->
                    chapter.toEntity(
                        index,
                        version,
                        content.uuid
                    )
                }
            }.flatten()

            val levelDto = chapterDto.map { it.levels }.flatten()
            val levels = chapterDto.zip(chapters) { data, chapter ->
                data.levels.mapIndexed { index, level ->
                    level.toEntity(
                        index,
                        version,
                        chapter.uuid
                    )
                }
            }.flatten()

            val rewards = levelDto.zip(levels) { data, level ->
                data.reward.toEntity(
                    version,
                    levelUuid = level.uuid
                )
            }.plus(chapterDto.zip(chapters) { data, chapter ->
                data.freeRewards?.map {
                    it.toEntity(
                        version,
                        chapterUuid = chapter.uuid
                    )
                }.orEmpty()
            }.flatten())

            gameDatabase.contractsDao.upsert(
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
        version: String,
        content: ContentEntity,
    ): Flow<ContentRelation?> {
        if (content.relationType.isNullOrEmpty() || content.relationUuid.isNullOrEmpty()) {
            return flow { emit(null) }
        }

        return when (content.relationType) {
            "Agent"  -> {
                agentRepository.getByUuid(
                    content.relationUuid,
                    version
                )
            }

            "Event"  -> {
                eventRepository.getByUuid(
                    content.relationUuid,
                    version
                )
            }

            "Season" -> {
                seasonRepository.getByUuid(
                    content.relationUuid,
                    version
                )
            }

            else     -> flow { emit(null) }
        }
    }

    // ================================
    //  Queue worker
    // ================================

    override fun queueWorker(version: String, uuid: String?) {
        val workRequest = OneTimeWorkRequestBuilder<ContractSyncWorker>()
            .setInputData(
                workDataOf(
                    ContractSyncWorker.KEY_CONTRACT_UUID to uuid,
                    ContractSyncWorker.KEY_VERSION to version
                )
            )
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()
        workManager.enqueueUniqueWork(
            ContractSyncWorker.WORK_NAME,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            workRequest
        )
    }
}