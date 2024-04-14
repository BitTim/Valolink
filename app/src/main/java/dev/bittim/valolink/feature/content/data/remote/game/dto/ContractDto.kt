package dev.bittim.valolink.feature.content.data.remote.game.dto

import dev.bittim.valolink.feature.content.data.local.game.entity.contract.ContractEntity

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
    data class Content(
        val relationType: String?,
        val relationUuid: String?,
        val chapters: List<Chapter>,
        val premiumRewardScheduleUuid: String?,
        val premiumVPCost: Int
    ) {
        data class Chapter(
            val isEpilogue: Boolean,
            val levels: List<Level>,
            val freeRewards: List<Reward>?
        ) {
            data class Level(
                val reward: Reward,
                val xp: Int,
                val vpCost: Int,
                val isPurchasableWithVP: Boolean,
                val doughCost: Int,
                val isPurchasableWithDough: Boolean
            )

            data class Reward(
                val type: String,
                val uuid: String,
                val amount: Int,
                val isHighlighted: Boolean
            )
        }
    }


    override fun toEntity(version: String): ContractEntity {
        return ContractEntity(
            uuid = uuid,
            version = version,
            displayName = displayName,
            displayIcon = displayIcon,
            shipIt = shipIt,
            useLevelVPCostOverride = useLevelVPCostOverride,
            levelVPCostOverride = levelVPCostOverride,
            freeRewardScheduleUuid = freeRewardScheduleUuid,
            content = ContractEntity.Content(
                relationType = content.relationType,
                relationUuid = content.relationUuid,
                chapters = content.chapters.map { chapter ->
                    ContractEntity.Content.Chapter(
                        isEpilogue = chapter.isEpilogue,
                        levels = chapter.levels.map { level ->
                            ContractEntity.Content.Chapter.Level(
                                reward = ContractEntity.Content.Chapter.Reward(
                                    type = level.reward.type,
                                    uuid = level.reward.uuid,
                                    amount = level.reward.amount,
                                    isHighlighted = level.reward.isHighlighted
                                ),
                                xp = level.xp,
                                vpCost = level.vpCost,
                                isPurchasableWithVP = level.isPurchasableWithVP,
                                doughCost = level.doughCost,
                                isPurchasableWithDough = level.isPurchasableWithDough
                            )
                        },
                        freeRewards = chapter.freeRewards?.map { reward ->
                            ContractEntity.Content.Chapter.Reward(
                                type = reward.type,
                                uuid = reward.uuid,
                                amount = reward.amount,
                                isHighlighted = reward.isHighlighted
                            )
                        }
                    )
                },
                premiumRewardScheduleUuid = content.premiumRewardScheduleUuid,
                premiumVPCost = content.premiumVPCost
            ),
            assetPath = assetPath
        )
    }
}