package dev.bittim.valolink.content.domain.model

import dev.bittim.valolink.content.domain.model.contract.reward.RewardRelation
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType

data class Spray(
    val uuid: String,
    val displayName: String,
    val themeUuid: String?,
    val hideIfNotOwned: Boolean,
    val displayIcon: String,
    val fullIcon: String?,
    val fullTransparentIcon: String?,
    val animationPng: String?,
    val animationGif: String?,
) {
    fun asRewardRelation(amount: Int): RewardRelation {
        return RewardRelation(
            uuid = uuid,
            type = RewardType.SPRAY,
            amount = amount,
            displayName = displayName,
            previewImages = listOf(
                (animationPng ?: animationGif ?: fullTransparentIcon ?: displayIcon) to null
            ),
            displayIcon = displayIcon,
            background = displayIcon,
        )
    }
}
