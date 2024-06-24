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
import dev.bittim.valolink.main.data.worker.game.GameSyncWorker
import dev.bittim.valolink.main.domain.model.game.contract.Contract
import dev.bittim.valolink.main.domain.model.game.contract.content.ContentRelation
import dev.bittim.valolink.main.domain.model.game.contract.content.ContentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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
    private val workManager: WorkManager,
) : ContractRepository {
    override suspend fun getByUuid(
        uuid: String,
    ): Flow<Contract?> {
        return try {
            // Get from local database
            val local = gameDatabase.contractsDao
                .getByUuid(uuid)
                .distinctUntilChanged()
                .map {
                    it?.toType(getRelation(it.content.content).firstOrNull())
                }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker(uuid)

            // Return
            local
        } catch (e: Exception) {
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getRecruitmentAsContract(
        uuid: String,
    ): Flow<Contract?> {
        return try {
            // Get from local database
            val local = gameDatabase.contractsDao
                .getRecruitmentByUuid(uuid)
                .distinctUntilChanged()
                .map { it?.toContract() }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            agentRepository.queueWorker()

            // Return
            local
        } catch (e: Exception) {
            e.printStackTrace()
            return flow { }
        }
    }



    override suspend fun getActiveContracts(): Flow<List<Contract>> {
        return try {
            // Get current time
            val currentIsoTime = Instant.now().toString()

            // Get contracts from local database
            val localContracts = gameDatabase.contractsDao
                .getActiveByTime(currentIsoTime)
                .distinctUntilChanged()
                .map { entities ->
                    entities.map {
                        it.toType(getRelation(it.content.content).firstOrNull())
                    }
                }

            // Get recruitments from local database
            val localRecruitments = gameDatabase.contractsDao
                .getActiveRecruitmentsByTime(currentIsoTime)
                .distinctUntilChanged()
                .map { entities ->
                    entities.map { it.toContract() }
                }

            // Combine contracts and recruitments
            val local = localContracts.combine(localRecruitments) { contracts, recruitments ->
                (contracts + recruitments).sortedBy { it.content.relation?.endTime }
            }

            // Queue workers to fetch newest data from API
            //  -> Workers will check if fetch is needed itself
            queueWorker()
            agentRepository.queueWorker()

            // Return
            local
        } catch (e: Exception) {
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getAgentGears(): Flow<List<Contract>> {
        return try {
            // Get from local database
            val local = gameDatabase.contractsDao
                .getAllGears()
                .distinctUntilChanged()
                .map { entities ->
                    entities.map {
                        it.toType(getRelation(it.content.content).firstOrNull())
                    }
                }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker()

            // Return
            local
        } catch (e: Exception) {
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getInactiveContracts(
        contentType: ContentType,
    ): Flow<List<Contract>> {
        return try {
            // Get current time
            val currentIsoTime = Instant.now().toString()

            // Get from local database
            val local = when (contentType) {
                ContentType.SEASON  -> {
                    gameDatabase.contractsDao
                        .getInactiveSeasonsByTime(currentIsoTime)
                        .distinctUntilChanged()
                        .map { entities -> entities.map { it.toType(getRelation(it.content.content).firstOrNull()) } }
                }

                ContentType.EVENT   -> {
                    gameDatabase.contractsDao
                        .getInactiveEventsByTime(currentIsoTime)
                        .distinctUntilChanged()
                        .map { entities -> entities.map { it.toType(getRelation(it.content.content).firstOrNull()) } }
                }

                ContentType.RECRUIT -> {
                    gameDatabase.contractsDao
                        .getInactiveRecruitmentsByTime(currentIsoTime)
                        .distinctUntilChanged()
                        .map { entities -> entities.map { it.toContract() } }
                }
            }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker()
            seasonRepository.queueWorker()
            eventRepository.queueWorker()
            agentRepository.queueWorker()

            // Return
            local
        } catch (e: Exception) {
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getAll(): Flow<List<Contract>> {
        return try {
            // Get from local database
            val local = gameDatabase.contractsDao
                .getAll()
                .distinctUntilChanged()
                .map { entities ->
                    entities.map {
                        it.toType(getRelation(it.content.content).firstOrNull())
                    }
                }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker()

            // Return
            local
        } catch (e: Exception) {
            e.printStackTrace()
            return flow { }
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
                    "Season" -> seasonRepository.queueWorker(content.relationUuid)
                    "Event"  -> eventRepository.queueWorker(content.relationUuid)
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

    override suspend fun fetchAll(version: String) {
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
        content: ContentEntity,
    ): Flow<ContentRelation?> {
        if (content.relationType.isNullOrEmpty() || content.relationUuid.isNullOrEmpty()) {
            return flow { emit(null) }
        }

        return when (content.relationType) {
            "Agent"  -> agentRepository.getByUuid(content.relationUuid)
            "Event"  -> eventRepository.getByUuid(content.relationUuid)
            "Season" -> seasonRepository.getByUuid(content.relationUuid)
            else     -> flow { emit(null) }
        }
    }

    // ================================
    //  Queue Worker
    // ================================

    override fun queueWorker(
        uuid: String?,
    ) {
        val workRequest = OneTimeWorkRequestBuilder<GameSyncWorker>()
            .setInputData(
                workDataOf(
                    GameSyncWorker.KEY_TYPE to Contract::class.simpleName,
                    GameSyncWorker.KEY_UUID to uuid,
                )
            )
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()
        workManager.enqueueUniqueWork(
            Contract::class.simpleName + GameSyncWorker.WORK_BASE_NAME + if (!uuid.isNullOrEmpty()) "_$uuid" else "",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }
}