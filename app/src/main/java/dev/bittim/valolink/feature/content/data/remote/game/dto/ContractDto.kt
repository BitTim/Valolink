package dev.bittim.valolink.feature.content.data.remote.game.dto

import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.LevelEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.RewardEntity
import java.util.UUID

data class ContractDto(
    val uuid: String,
    val displayName: String,
    val displayIcon: String?,
    val shipIt: Boolean,
    val useLevelVPCostOverride: Boolean,
    val levelVPCostOverride: Int,
    val freeRewardScheduleUuid: String, val content: ContentDto,
    val assetPath: String
) {
    fun toEntity(version: String): ContractEntity {
        return ContractEntity(
            uuid,
            version,
            displayName,
            displayIcon,
            shipIt,
            useLevelVPCostOverride,
            levelVPCostOverride,
            freeRewardScheduleUuid,
            assetPath
        )
    }



    data class ContentDto(
        val relationType: String?,
        val relationUuid: String?, val chapters: List<ChapterDto>,
        val premiumRewardScheduleUuid: String?,
        val premiumVPCost: Int
    ) {
        fun toEntity(uuid: String, version: String, contractUuid: String): ContentEntity {
            return ContentEntity(
                uuid,
                contractUuid,
                version,
                relationType,
                relationUuid,
                premiumRewardScheduleUuid,
                premiumVPCost
            )
        }



        data class ChapterDto(
            val isEpilogue: Boolean,
            val levels: List<ChapterLevelDto>,
            val freeRewards: List<RewardDto>?
        ) {
            fun toEntity(index: Int, version: String, contentUuid: String): ChapterEntity {
                return ChapterEntity(
                    contentUuid + "_" + index, contentUuid, version, isEpilogue
                )
            }



            data class ChapterLevelDto(
                val reward: RewardDto,
                val xp: Int,
                val vpCost: Int,
                val isPurchasableWithVP: Boolean,
                val doughCost: Int,
                val isPurchasableWithDough: Boolean
            ) {
                fun toEntity(index: Int, version: String, chapterUuid: String): LevelEntity {
                    return LevelEntity(
                        chapterUuid + "_" + index,
                        chapterUuid,
                        version,
                        xp,
                        vpCost,
                        isPurchasableWithVP,
                        doughCost,
                        isPurchasableWithDough
                    )
                }
            }

            data class RewardDto(
                val type: String,
                val uuid: String,
                val amount: Int,
                val isHighlighted: Boolean
            ) {
                fun toEntity(
                    version: String,
                    chapterUuid: String? = null,
                    levelUuid: String? = null
                ): RewardEntity {
                    return RewardEntity(
                        chapterUuid ?: levelUuid ?: UUID.randomUUID().toString(),
                        levelUuid,
                        chapterUuid,
                        version,
                        type,
                        uuid,
                        amount,
                        isHighlighted
                    )
                }
            }
        }
    }
}