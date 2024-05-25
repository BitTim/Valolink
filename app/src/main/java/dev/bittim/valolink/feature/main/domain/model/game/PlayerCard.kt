package dev.bittim.valolink.feature.main.domain.model.game

import dev.bittim.valolink.feature.main.domain.model.game.contract.RewardRelation

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
    fun asRewardRelation(amount: Int): RewardRelation {
        return RewardRelation(
            uuid = uuid,
            type = "Card",
            displayName = displayName,
            displayIcon = largeArt,
            amount = amount
        )
    }
}