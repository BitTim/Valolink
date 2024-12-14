/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Buddy.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.domain.model.buddy

import dev.bittim.valolink.content.domain.model.contract.reward.RewardRelation
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType

data class Buddy(
    val uuid: String,
    val displayName: String,
    val isHiddenIfNotOwned: Boolean,
    val themeUuid: String?,
    val displayIcon: String,
    val levels: List<BuddyLevel>,
) {
    fun asRewardRelation(amount: Int): RewardRelation {
        val actualAmount = if (amount < 1) 1 else amount
        val actualDisplayIcon = levels.firstOrNull()?.displayIcon ?: displayIcon

        return RewardRelation(
            uuid = uuid,
            type = RewardType.BUDDY,
            amount = actualAmount,
            displayName = displayName,
            previewImages = listOf(actualDisplayIcon to null),
            displayIcon = actualDisplayIcon,
        )
    }

    companion object {
        val EMPTY = Buddy(
            uuid = "",
            displayName = "",
            isHiddenIfNotOwned = false,
            themeUuid = null,
            displayIcon = "",
            levels = emptyList(),
        )
    }
}
