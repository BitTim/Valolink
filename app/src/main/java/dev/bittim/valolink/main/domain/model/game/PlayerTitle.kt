package dev.bittim.valolink.main.domain.model.game

import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardRelation
import dev.bittim.valolink.main.domain.model.game.contract.reward.RewardType

data class PlayerTitle(
    val uuid: String,
    val displayName: String?,
    val titleText: String?,
    val isHiddenIfNotOwned: Boolean,
) {
    fun asRewardRelation(amount: Int): RewardRelation {
        return RewardRelation(
            uuid = uuid,
            type = RewardType.TITLE,
            amount = amount,
            displayName = displayName ?: "No title",
            previewImages = listOf("https://static.wikia.nocookie.net/valorant/images/5/5d/Player_Title_image.png/revision/latest?cb=20210104061536" to null), // TODO: Replace with local resource
            displayIcon = "https://static.wikia.nocookie.net/valorant/images/5/5d/Player_Title_image.png/revision/latest?cb=20210104061536", // TODO: Replace with local resource
        )
    }
}
