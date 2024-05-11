package dev.bittim.valolink.feature.content.domain.model

import dev.bittim.valolink.feature.content.domain.model.contract.RewardRelation

data class PlayerTitle(
    val uuid: String,
    val displayName: String,
    val titleText: String,
    val isHiddenIfNotOwned: Boolean
) {
    fun asRewardRelation(): RewardRelation {
        return RewardRelation(
            uuid = uuid,
            type = "Title",
            displayName = displayName,
            displayIcon = "https://static.wikia.nocookie.net/valorant/images/5/5d/Player_Title_image.png/revision/latest?cb=20210104061536", // TODO: Replace with local resource
        )
    }
}
