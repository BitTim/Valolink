/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContractApiRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 03:44
 */

package dev.bittim.valolink.content.data.repository.contract

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.bittim.valolink.content.data.local.ContentDatabase
import dev.bittim.valolink.content.data.local.entity.contract.LevelEntity
import dev.bittim.valolink.content.data.local.entity.contract.RewardEntity
import dev.bittim.valolink.content.data.local.relation.contract.ContractWithContentWithChaptersWithLevelsAndRewards
import dev.bittim.valolink.content.data.remote.ContentApi
import dev.bittim.valolink.content.data.repository.agent.AgentRepository
import dev.bittim.valolink.content.data.repository.buddy.BuddyRepository
import dev.bittim.valolink.content.data.repository.currency.CurrencyRepository
import dev.bittim.valolink.content.data.repository.event.EventRepository
import dev.bittim.valolink.content.data.repository.flex.FlexRepository
import dev.bittim.valolink.content.data.repository.playerCard.PlayerCardRepository
import dev.bittim.valolink.content.data.repository.playerTitle.PlayerTitleRepository
import dev.bittim.valolink.content.data.repository.season.SeasonRepository
import dev.bittim.valolink.content.data.repository.spray.SprayRepository
import dev.bittim.valolink.content.data.repository.weapon.WeaponRepository
import dev.bittim.valolink.content.data.worker.ContentSyncWorker
import dev.bittim.valolink.content.domain.model.contract.Contract
import dev.bittim.valolink.content.domain.model.contract.chapter.Level
import dev.bittim.valolink.content.domain.model.contract.content.ContentType
import dev.bittim.valolink.content.domain.model.contract.reward.RewardRelation
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
class ContractApiRepository @Inject constructor(
    private val contentDatabase: ContentDatabase,
    private val contentApi: ContentApi,
    private val workManager: WorkManager,

    private val seasonRepository: SeasonRepository,
    private val eventRepository: EventRepository,
    private val agentRepository: AgentRepository,
    private val flexRepository: FlexRepository,
    private val currencyRepository: CurrencyRepository,
    private val sprayRepository: SprayRepository,
    private val playerTitleRepository: PlayerTitleRepository,
    private val playerCardRepository: PlayerCardRepository,
    private val buddyRepository: BuddyRepository,
    private val weaponRepository: WeaponRepository,
) : ContractRepository {
    override suspend fun getByUuid(
        uuid: String,
        withRewards: Boolean,
    ): Flow<Contract?> {
        return try {
            // Get from local database
            val local =
                contentDatabase.contractsDao.getByUuid(uuid).distinctUntilChanged().flatMapLatest {
                    // If contract could not be found in database, try finding it as recruitment
                    if (it == null) contentDatabase.contractsDao.getRecruitmentByUuid(uuid)
                        .distinctUntilChanged().flatMapLatest {
                            // If it is not a recruitment either, try resolving the UUID to an agent gear
                            if (it == null) contentDatabase.contractsDao.getGearByAgent(uuid)
                                .flatMapLatest { withRelation(it, withRewards) }
                            else flowOf(it.toContract())
                        }
                    else withRelation(it, withRewards)
                }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker(uuid)

            // Return
            local
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getByUuid(uuid: String): Flow<Contract?> {
        return getByUuid(uuid, false)
    }

    override suspend fun getLevelByUuid(uuid: String): Flow<Level?> {
        return try {
            // Get from local database
            val local = contentDatabase.contractsDao.getLevelByUuid(uuid).distinctUntilChanged()
                .flatMapLatest { level ->
                    if (level == null) return@flatMapLatest flowOf(null)
                    val rewardsFlow = combine(level.rewards.map { getReward(it) }) { it.toList() }

                    combine(
                        flowOf(level), rewardsFlow, getLevelMetadata(level.level)
                    ) { combinedLevel, rewards, meta ->
                        combinedLevel.toType(rewards, meta.name)
                    }
                }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker()

            // Return
            local
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getLevelByDependency(dependency: String): Flow<Level?> {
        return try {
            // Get from local database
            val local =
                contentDatabase.contractsDao.getLevelByDependency(dependency).distinctUntilChanged()
                    .flatMapLatest { level ->
                        if (level == null) return@flatMapLatest flowOf(null)
                        val rewardsFlow =
                            combine(level.rewards.map { getReward(it) }) { it.toList() }

                        combine(
                            flowOf(level), rewardsFlow, getLevelMetadata(level.level)
                        ) { combinedLevel, rewards, meta ->
                            combinedLevel.toType(rewards, meta.name)
                        }
                    }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker()

            // Return
            local
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }


    override suspend fun getActiveContracts(withRewards: Boolean): Flow<List<Contract>> {
        return try {
            // Get current time
            val currentIsoTime = Instant.now().toString()

            // Get contracts from local database
            val localContracts =
                contentDatabase.contractsDao.getActiveByTime(currentIsoTime).distinctUntilChanged()
                    .flatMapLatest {
                        withRelation(it, withRewards)
                    }

            // Get recruitments from local database
            val localRecruitments =
                contentDatabase.contractsDao.getActiveRecruitmentsByTime(currentIsoTime)
                    .distinctUntilChanged().map { entities ->
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
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getAgentGears(withRewards: Boolean): Flow<List<Contract>> {
        return try {
            // Get from local database
            val local =
                contentDatabase.contractsDao.getAllGears().distinctUntilChanged().flatMapLatest {
                    withRelation(it, withRewards)
                }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker()

            // Return
            local
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getInactiveContracts(
        contentType: ContentType,
        withRewards: Boolean
    ): Flow<List<Contract>> {
        return try {
            // Get current time
            val currentIsoTime = Instant.now().toString()

            // Get from local database
            val local = when (contentType) {
                ContentType.SEASON -> {
                    contentDatabase.contractsDao.getInactiveSeasonsByTime(currentIsoTime)
                        .distinctUntilChanged().flatMapLatest {
                            withRelation(it, withRewards)
                        }
                }

                ContentType.EVENT -> {
                    contentDatabase.contractsDao.getInactiveEventsByTime(currentIsoTime)
                        .distinctUntilChanged().flatMapLatest {
                            withRelation(it, withRewards)
                        }
                }

                ContentType.AGENT -> {
                    contentDatabase.contractsDao.getInactiveRecruitmentsByTime(currentIsoTime)
                        .distinctUntilChanged().map { entities -> entities.map { it.toContract() } }
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
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getAll(withRewards: Boolean): Flow<List<Contract>> {
        return try {
            // Get from local database
            val local = contentDatabase.contractsDao.getAll().distinctUntilChanged().flatMapLatest {
                withRelation(it, withRewards)
            }

            // Queue worker to fetch newest data from API
            //  -> Worker will check if fetch is needed itself
            queueWorker()

            // Return
            local
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return flow { }
        }
    }

    override suspend fun getAll(): Flow<List<Contract>> {
        return getAll(false)
    }

    // --------------------------------
    //  Fetching from API
    // --------------------------------

    // -------- [ Single fetching ] --------

    override suspend fun fetch(
        uuid: String,
        version: String,
    ) {
        val response = contentApi.getContract(uuid)
        if (response.isSuccessful) {
            val contractDto = response.body()!!.data!!

            val contract = contractDto.toEntity(version)
            val content = contractDto.content.toEntity(
                contract.freeRewardScheduleUuid, version, contract.uuid
            )

            if (content.relationUuid != null) {
                when (content.relationType) {
                    "Season" -> seasonRepository.queueWorker(content.relationUuid)
                    "Event" -> eventRepository.queueWorker(content.relationUuid)
                }
            }

            val chapterDto = contractDto.content.chapters
            val chapters = contractDto.content.chapters.mapIndexed { index, chapter ->
                chapter.toEntity(
                    index, version, content.uuid
                )
            }

            var previousLevel: String? = null
            val levelDto = chapterDto.map { it.levels }.flatten()
            val levels = chapterDto.zip(chapters) { data, chapter ->
                data.levels.mapIndexed { index, level ->
                    val levelEntity = level.toEntity(
                        index, previousLevel, version, chapter.uuid
                    )

                    previousLevel = levelEntity.uuid
                    levelEntity
                }
            }

            val rewards = levelDto.zip(levels.flatten()) { data, level ->
                data.reward.toEntity(
                    version, false, level.uuid
                )
            }.plus(chapterDto.zip(chapters) { data, chapter ->
                val levelList =
                    levels.find { levelList -> levelList.any { it.chapterUuid == chapter.uuid } }
                data.freeRewards?.map {
                    it.toEntity(
                        version, true, levelList?.lastOrNull()?.uuid ?: ""
                    )
                } ?: emptyList()
            }.flatten())

            contentDatabase.contractsDao.upsert(
                contract,
                content,
                chapters.distinct().toSet(),
                levels.flatten().distinct().toSet(),
                rewards.distinct().toSet()
            )
        }
    }

    // -------- [ Bulk fetching ] --------

    override suspend fun fetchAll(version: String) {
        val response = contentApi.getAllContracts()
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
                    chapter.toEntity(
                        index, version, content.uuid
                    )
                }
            }.flatten()

            var previousLevel: String? = null
            var previousContent: String? = null
            val levelDto = chapterDto.map { it.levels }.flatten()
            val levels = chapterDto.zip(chapters) { data, chapter ->
                if (chapter.contentUuid != previousContent) previousLevel = null

                val levelEntities = data.levels.mapIndexed { index, level ->
                    val levelEntity = level.toEntity(
                        index,
                        previousLevel,
                        version,
                        chapter.uuid,
                    )

                    previousLevel = levelEntity.uuid
                    levelEntity
                }

                previousContent = chapter.contentUuid
                levelEntities
            }

            val rewards = levelDto.zip(levels.flatten()) { data, level ->
                data.reward.toEntity(
                    version, false, level.uuid
                )
            }.plus(chapterDto.zip(chapters) { data, chapter ->
                val levelList =
                    levels.find { levelList -> levelList.any { it.chapterUuid == chapter.uuid } }
                data.freeRewards?.map {
                    it.toEntity(
                        version, true, levelList?.lastOrNull()?.uuid ?: ""
                    )
                } ?: emptyList()
            }.flatten())

            contentDatabase.contractsDao.upsert(
                contracts.distinct().toSet(),
                contents.distinct().toSet(),
                chapters.distinct().toSet(),
                levels.flatten().distinct().toSet(),
                rewards.distinct().toSet()
            )
        }
    }

    // --------------------------------
    //  Query relation
    // --------------------------------

    private suspend fun withRelation(
        contractEntity: ContractWithContentWithChaptersWithLevelsAndRewards?,
        withRewards: Boolean,
    ): Flow<Contract?> {
        val content = contractEntity?.content?.content

        if (content == null || content.relationType.isNullOrEmpty() || content.relationUuid.isNullOrEmpty()) {
            return flow { emit(null) }
        }

        val relationFlow = when (content.relationType) {
            ContentType.AGENT.internalName -> agentRepository.getByUuid(content.relationUuid)
            ContentType.EVENT.internalName -> eventRepository.getByUuid(content.relationUuid)
            ContentType.SEASON.internalName -> seasonRepository.getByUuid(content.relationUuid)
            else -> null
        }

        if (relationFlow == null) return flow { emit(null) }

        val rewardsFlow = if (withRewards) {
            combine(contractEntity.content.chapters.map { chapter ->
                if (chapter.levels.isNotEmpty()) combine(chapter.levels.map { level ->
                    combine(level.rewards.map {
                        getReward(
                            it
                        )
                    }) { it.toList() }
                }) { it.toList() }
                else flow { emit(emptyList()) }
            }) { it.toList() }
        } else {
            flow { emit(emptyList()) }
        }

        val levelNamesFlow =
            flowOf(contractEntity.content.chapters.mapIndexed { chapterIndex, chapter ->
                if (chapter.levels.isNotEmpty()) List(chapter.levels.size) { levelIndex ->
                    getLevelMetadata(
                        contractEntity, chapterIndex, levelIndex
                    ).name
                }
                else emptyList()
            })

        return combine(
            flowOf(contractEntity), relationFlow, rewardsFlow, levelNamesFlow
        ) { contract, relation, rewards, levelNames ->
            contract.toType(relation, rewards, levelNames)
        }
    }

    private suspend fun withRelation(
        contractEntities: List<ContractWithContentWithChaptersWithLevelsAndRewards>,
        withRewards: Boolean,
    ): Flow<List<Contract>> {
        return combine(contractEntities.map { withRelation(it, withRewards) }) {
            it.filterNotNull().toList()
        }
    }


    private suspend fun getReward(
        reward: RewardEntity,
    ): Flow<RewardRelation?> {
        if (reward.rewardType.isEmpty() || reward.rewardUuid.isEmpty()) {
            return flow { emit(null) }
        }

        return when (reward.rewardType) {
            RewardType.TITLE.internalName -> playerTitleRepository.getByUuid(reward.rewardUuid)
                .map { it?.asRewardRelation(reward.amount) }

            RewardType.PLAYER_CARD.internalName -> playerCardRepository.getByUuid(reward.rewardUuid)
                .map { it?.asRewardRelation(reward.amount) }

            RewardType.BUDDY.internalName -> buddyRepository.getByLevelUuid(reward.rewardUuid)
                .map { it?.asRewardRelation(reward.amount) }

            RewardType.CURRENCY.internalName -> currencyRepository.getByUuid(reward.rewardUuid)
                .map { it?.asRewardRelation(reward.amount) }

            RewardType.SPRAY.internalName -> sprayRepository.getByUuid(reward.rewardUuid)
                .map { it?.asRewardRelation(reward.amount) }

            RewardType.WEAPON_SKIN.internalName -> weaponRepository.getSkinByLevelUuid(reward.rewardUuid)
                .map { it?.asRewardRelation(reward.amount, reward.rewardUuid) }

            RewardType.FLEX.internalName -> flexRepository.getByUuid(reward.rewardUuid)
                .map { it?.asRewardRelation(reward.amount) }

            else -> flow { emit(null) }
        }
    }

    private fun getLevelMetadata(
        level: LevelEntity,
    ): Flow<LevelMeta> {
        val chapterFlow = contentDatabase.contractsDao.getChapterByUuid(level.chapterUuid)
        val contentFlow = chapterFlow.flatMapLatest { chapter ->
            if (chapter == null) return@flatMapLatest flow { LevelMeta.error("Chapter not found") }
            contentDatabase.contractsDao.getContentByUuid(chapter.chapter.contentUuid)
        }

        val contractFlow = contentFlow.flatMapLatest { content ->
            if (content == null) return@flatMapLatest flow { LevelMeta.error("Content not found") }
            contentDatabase.contractsDao.getByUuid(content.content.contractUuid)
        }

        return combine(contractFlow, contentFlow, chapterFlow) { contract, content, chapter ->
            if (contract == null || content == null || chapter == null) return@combine LevelMeta.error(
                "Data not found"
            )

            val chapterIndex =
                content.chapters.indexOfFirst { it.chapter.uuid == chapter.chapter.uuid }
            val levelIndex = chapter.levels.indexOfFirst { it.level.uuid == level.uuid }

            getLevelMetadata(contract, chapterIndex, levelIndex)
        }
    }

    private fun getLevelMetadata(
        contract: ContractWithContentWithChaptersWithLevelsAndRewards,
        chapterIndex: Int,
        levelIndex: Int,
    ): LevelMeta {
        val contractName = contract.contract.displayName
        val firstEpilogueChapter = contract.content.chapters.indexOfFirst { it.chapter.isEpilogue }
        val isEpilogue = contract.content.chapters[chapterIndex].chapter.isEpilogue

        val firstChapter = if (!isEpilogue) 0 else firstEpilogueChapter
        val chapterSubset = contract.content.chapters.subList(firstChapter, chapterIndex)

        val globalLevelIndex = chapterSubset.flatMap { it.levels }.count() + levelIndex

        return LevelMeta(
            "Level ${if (isEpilogue) "E" else ""}${globalLevelIndex + 1}", contractName
        )
    }

    // ================================
    //  Queue Worker
    // ================================

    override fun queueWorker(
        uuid: String?,
    ) {
        val workRequest = OneTimeWorkRequestBuilder<ContentSyncWorker>().setInputData(
            workDataOf(
                ContentSyncWorker.KEY_TYPE to Contract::class.simpleName,
                ContentSyncWorker.KEY_UUID to uuid,
            )
        ).setConstraints(Constraints(NetworkType.CONNECTED)).build()
        workManager.enqueueUniqueWork(
            Contract::class.simpleName + ContentSyncWorker.WORK_BASE_NAME + if (!uuid.isNullOrEmpty()) "_$uuid" else "",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }

    // ================================
    //  Helper classes
    // ================================

    private data class LevelMeta(
        val name: String,
        val contractName: String,
    ) {
        companion object {
            fun error(errorDesc: String) = LevelMeta("Error", errorDesc)
        }
    }
}
