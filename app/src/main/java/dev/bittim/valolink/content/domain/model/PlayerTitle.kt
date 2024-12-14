/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       PlayerTitle.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:30
 */

package dev.bittim.valolink.content.domain.model

import dev.bittim.valolink.content.domain.model.contract.reward.RewardRelation
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType

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
