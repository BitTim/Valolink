package dev.bittim.valolink.main.domain.model.game.buddy

import dev.bittim.valolink.main.domain.model.game.contract.RewardRelation

data class Buddy(
    val uuid: String,
    val displayName: String,
    val isHiddenIfNotOwned: Boolean,
    val themeUuid: String?,
    val displayIcon: String,
    val levels: List<BuddyLevel>,
) {
    fun asRewardRelation(amount: Int): RewardRelation {
        return RewardRelation(
            uuid = uuid,
            type = "Buddy",
            displayName = displayName,
            displayIcon = if (levels.isNotEmpty()) levels[0].displayIcon else displayIcon,
            amount = amount
        )
    }
}
