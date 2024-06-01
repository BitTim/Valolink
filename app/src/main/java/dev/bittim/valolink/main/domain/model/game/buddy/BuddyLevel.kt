package dev.bittim.valolink.main.domain.model.game.buddy

import dev.bittim.valolink.main.domain.model.game.contract.RewardRelation

data class BuddyLevel(
    val uuid: String,
    val hideIfNotOwned: Boolean,
    val displayName: String,
    val displayIcon: String,
) {
    fun asRewardRelation(amount: Int): RewardRelation {
        return RewardRelation(
            uuid = uuid,
            type = "Buddy",
            displayName = displayName,
            displayIcon = displayIcon,
            amount = amount
        )
    }
}
