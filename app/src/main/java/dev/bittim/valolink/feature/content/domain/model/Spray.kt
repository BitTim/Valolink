package dev.bittim.valolink.feature.content.domain.model

import dev.bittim.valolink.feature.content.domain.model.contract.RewardRelation

data class Spray(
    val uuid: String,
    val displayName: String,
    val themeUuid: String?,
    val hideIfNotOwned: Boolean,
    val displayIcon: String,
    val fullIcon: String?,
    val fullTransparentIcon: String?,
    val animationPng: String?,
    val animationGif: String?
) {
    fun asRewardRelation(): RewardRelation {
        return RewardRelation(
            uuid = uuid,
            type = "Spray",
            displayName = displayName,
            displayIcon = animationPng ?: animationGif ?: fullTransparentIcon ?: displayIcon
        )
    }
}
