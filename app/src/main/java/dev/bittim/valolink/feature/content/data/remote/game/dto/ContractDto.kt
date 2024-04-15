package dev.bittim.valolink.feature.content.data.remote.game.dto

import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ChapterLevelEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractContentEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity
import dev.bittim.valolink.feature.content.data.local.game.entity.contract.RewardEntity
import java.util.UUID

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
    fun toEntity(version: String) = ContractEntity(
        uuid = uuid,
        version = version,
        displayName = displayName,
        displayIcon = displayIcon,
        shipIt = shipIt,
        useLevelVPCostOverride = useLevelVPCostOverride,
        levelVPCostOverride = levelVPCostOverride,
        freeRewardScheduleUuid = freeRewardScheduleUuid,
        assetPath = assetPath
    )



    data class Content(
        val relationType: String?,
        val relationUuid: String?,
        val chapters: List<Chapter>,
        val premiumRewardScheduleUuid: String?,
        val premiumVPCost: Int
    ) {
        fun toEntity(version: String, contractUuid: String) = ContractContentEntity(
            uuid = UUID.randomUUID().toString(),
            contractUuid = contractUuid,
            version = version,
            relationType = relationType,
            relationUuid = relationUuid,
            premiumRewardScheduleUuid = premiumRewardScheduleUuid,
            premiumVPCost = premiumVPCost
        )

        data class Chapter(
            val isEpilogue: Boolean,
            val levels: List<ChapterLevel>,
            val freeRewards: List<Reward>?
        ) {
            fun toEntity(version: String, contentUuid: String) = ChapterEntity(
                uuid = UUID.randomUUID().toString(),
                contentUuid = contentUuid,
                version = version,
                isEpilogue = isEpilogue
            )

            data class ChapterLevel(
                val reward: Reward,
                val xp: Int,
                val vpCost: Int,
                val isPurchasableWithVP: Boolean,
                val doughCost: Int,
                val isPurchasableWithDough: Boolean
            ) {
                fun toEntity(version: String, chapterUuid: String) = ChapterLevelEntity(
                    uuid = UUID.randomUUID().toString(),
                    chapterUuid = chapterUuid,
                    version = version,
                    xp = xp,
                    vpCost = vpCost,
                    isPurchasableWithVP = isPurchasableWithVP,
                    doughCost = doughCost,
                    isPurchasableWithDough = isPurchasableWithDough
                )
            }

            data class Reward(
                val type: String,
                val uuid: String,
                val amount: Int,
                val isHighlighted: Boolean
            ) {
                fun toEntity(
                    version: String,
                    chapterUuid: String? = null,
                    levelUuid: String? = null
                ) = RewardEntity(
                    uuid = UUID.randomUUID().toString(),
                    chapterUuid = chapterUuid,
                    levelUuid = levelUuid,
                    version = version,
                    rewardType = type,
                    rewardUuid = uuid,
                    amount = amount,
                    isHighlighted = isHighlighted
                )
            }
        }
    }
}