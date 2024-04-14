package dev.bittim.valolink.feature.content.data.remote.game.dto

import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterLevelEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.RewardEntity

data class ContractDto(
    val uuid: String,
    val displayName: String,
    val displayIcon: String?,
    val shipIt: Boolean,
    val useLevelVPCostOverride: Boolean,
    val levelVPCostOverride: Int,
    val freeRewardScheduleUuid: String,
    val content: Content,
    val assetPath: String
) : GameDto<ContractEntity>() {
    override fun toEntity(version: String): ContractEntity {
        val contract = ContractEntity(
            uuid = uuid,
            version = version,
            displayName = displayName,
            displayIcon = displayIcon,
            shipIt = shipIt,
            useLevelVPCostOverride = useLevelVPCostOverride,
            levelVPCostOverride = levelVPCostOverride,
            freeRewardScheduleUuid = freeRewardScheduleUuid,
            content = content.toEntity(version, uuid),
            assetPath = assetPath
        )
    }



    data class Content(
        val relationType: String?,
        val relationUuid: String?,
        val chapters: List<Chapter>,
        val premiumRewardScheduleUuid: String?,
        val premiumVPCost: Int
    ) {
        fun toEntity(version: String, contractUuid: String): Map<ContractEntity.Content, List<Map<ChapterEntity, Pair<List<Map<ChapterLevelEntity, RewardEntity>>, List<RewardEntity>?>>>> {
            val content = ContractEntity.Content(
                relationType = relationType,
                relationUuid = relationUuid,
                premiumRewardScheduleUuid = premiumRewardScheduleUuid,
                premiumVPCost = premiumVPCost
            )

            val chapters = chapters.map {
                it.toEntity(
                    version = version,
                    contractUuid = contractUuid
                )
            }

            return mapOf(Pair(content, chapters))
        }

        data class Chapter(
            val isEpilogue: Boolean,
            val levels: List<ChapterLevel>,
            val freeRewards: List<Reward>?
        ) {
            fun toEntity(version: String, contractUuid: String): Map<ChapterEntity, Pair<List<Map<ChapterLevelEntity, RewardEntity>>, List<RewardEntity>?>> {
                val chapter = ChapterEntity(
                    id = 0,
                    contractUuid = contractUuid,
                    version = version,
                    isEpilogue = isEpilogue
                )

                val levels = levels.map {
                    it.toEntity(
                        version,
                        chapter.id
                    )
                }

                val freeRewards = freeRewards?.map {
                    it.toEntity(
                        version = version,
                        chapterId = chapter.id
                    )
                }

                return mapOf(Pair(chapter, Pair(levels, freeRewards)))
            }

            data class ChapterLevel(
                val reward: Reward,
                val xp: Int,
                val vpCost: Int,
                val isPurchasableWithVP: Boolean,
                val doughCost: Int,
                val isPurchasableWithDough: Boolean
            ) {
                fun toEntity(
                    version: String,
                    chapterId: Int
                ): Map<ChapterLevelEntity, RewardEntity> {
                    val level = ChapterLevelEntity(
                        id = 0,
                        chapterId = chapterId,
                        version = version,
                        xp = xp,
                        vpCost = vpCost,
                        isPurchasableWithVP = isPurchasableWithVP,
                        doughCost = doughCost,
                        isPurchasableWithDough = isPurchasableWithDough
                    )

                    val reward = reward.toEntity(
                        version = version,
                        levelId = level.id
                    )

                    return mapOf(Pair(level, reward))
                }
            }

            data class Reward(
                val type: String,
                val uuid: String,
                val amount: Int,
                val isHighlighted: Boolean
            ) {
                fun toEntity(version: String, chapterId: Int? = null, levelId: Int? = null) =
                    RewardEntity(
                        id = 0,
                        chapterId = chapterId,
                        levelId = levelId,
                        version = version,
                        type = type,
                        uuid = uuid,
                        amount = amount,
                        isHighlighted = isHighlighted
                    )
            }
        }
    }
}