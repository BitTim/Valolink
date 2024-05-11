package dev.bittim.valolink.feature.content.domain.model.buddy

import dev.bittim.valolink.feature.content.domain.model.contract.RewardRelation

data class BuddyLevel(
    val uuid: String, val hideIfNotOwned: Boolean, val displayName: String, val displayIcon: String
) {
    fun asRewardRelation(): RewardRelation {
        return RewardRelation(
            uuid = uuid, type = "Buddy", displayName = displayName, displayIcon = displayIcon
        )
    }
}
