package dev.bittim.valolink.main.domain.model.game.buddy

import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardRelation
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardType

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
            type = RewardType.BUDDY,
            amount = amount,
            displayName = displayName,
            previewImage = if (levels.isNotEmpty()) levels[0].displayIcon else displayIcon,
            displayIcon = if (levels.isNotEmpty()) levels[0].displayIcon else displayIcon,
        )
    }
}
