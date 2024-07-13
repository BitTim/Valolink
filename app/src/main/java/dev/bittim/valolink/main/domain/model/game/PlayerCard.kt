package dev.bittim.valolink.main.domain.model.game

import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardRelation
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardType

data class PlayerCard(
    val uuid: String,
    val displayName: String,
    val isHiddenIfNotOwned: Boolean,
    val themeUuid: String?,
    val displayIcon: String,
    val smallArt: String,
    val wideArt: String,
    val largeArt: String?,
) {
    fun asRewardRelation(amount: Int): RewardRelation {
        return RewardRelation(
            uuid = uuid,
            type = RewardType.PLAYER_CARD,
            amount = amount,
            displayName = displayName,
            previewIcon = largeArt ?: wideArt,
            displayIcon = displayIcon,
            background = displayIcon
        )
    }
}