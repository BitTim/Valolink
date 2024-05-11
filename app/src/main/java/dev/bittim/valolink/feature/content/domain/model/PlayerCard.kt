package dev.bittim.valolink.feature.content.domain.model

import dev.bittim.valolink.feature.content.domain.model.contract.RewardRelation

data class PlayerCard(
    val uuid: String,
    val displayName: String,
    val isHiddenIfNotOwned: Boolean,
    val themeUuid: String?,
    val displayIcon: String,
    val smallArt: String,
    val wideArt: String,
    val largeArt: String
) {
    fun asRewardRelation(): RewardRelation {
        return RewardRelation(
            uuid = uuid,
            type = "Card",
            displayName = displayName,
            displayIcon = displayIcon,
        )
    }
}